package com.hotent.base.cache.aspect;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

import org.apache.commons.lang.SerializationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.hotent.base.cache.CacheManager;
import com.hotent.base.cache.ICache;
import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.cache.annotation.CachePut;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.cache.annotation.FirstCache;
import com.hotent.base.cache.annotation.SecondaryCache;
import com.hotent.base.cache.expression.CacheOperationExpressionEvaluator;
import com.hotent.base.cache.setting.CacheSetting;
import com.hotent.base.cache.setting.FirstCacheSetting;
import com.hotent.base.cache.setting.SecondaryCacheSetting;
import com.hotent.base.cache.support.CacheOperationInvoker;
import com.hotent.base.cache.support.KeyGenerator;
import com.hotent.base.cache.support.SimpleKeyGenerator;
import com.hotent.base.util.BeanUtils;

/**
 * 缓存拦截，用于注册方法信息
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月16日
 */
@Aspect
@Component
public class CacheAspect {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String CACHE_KEY_ERROR_MESSAGE = "缓存Key %s 不能为NULL";
    private static final String CACHE_NAME_ERROR_MESSAGE = "缓存名称不能为NULL";

    /**
     * SpEL表达式计算器
     */
    private final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();

    @Autowired
    private CacheManager cacheManager;

    @Autowired(required = false)
    private KeyGenerator keyGenerator = new SimpleKeyGenerator();

    @Pointcut("@annotation(com.hotent.base.cache.annotation.Cacheable)")
    public void cacheablePointcut() {
    }

    @Pointcut("@annotation(com.hotent.base.cache.annotation.CacheEvict)")
    public void cacheEvictPointcut() {
    }

    @Pointcut("@annotation(com.hotent.base.cache.annotation.CachePut)")
    public void cachePutPointcut() {
    }

    @Around("cacheablePointcut()")
    public Object cacheablePointcut(ProceedingJoinPoint joinPoint) throws Throwable {
        CacheOperationInvoker aopAllianceInvoker = getCacheOperationInvoker(joinPoint);

        // 获取method
        Method method = this.getSpecificmethod(joinPoint);
        // 获取注解
        Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);

        try {
            // 执行查询缓存方法
            return executeCacheable(aopAllianceInvoker, cacheable, method, joinPoint.getArgs(), joinPoint.getTarget());
        } catch (SerializationException e) {
            // 如果是序列化异常需要先删除原有缓存
            String[] cacheNames = cacheable.cacheNames();
            // 删除缓存
            delete(cacheNames, cacheable.key(), cacheable.pureKey(), method, joinPoint.getArgs(), joinPoint.getTarget());

            // 忽略操作缓存过程中遇到的异常
            if (cacheable.ignoreException()) {
                logger.warn(e.getMessage(), e);
                return aopAllianceInvoker.invoke();
            }
            throw e;
        } catch (Exception e) {
            // 忽略操作缓存过程中遇到的异常
            if (cacheable.ignoreException()) {
                logger.warn(e.getMessage(), e);
                return aopAllianceInvoker.invoke();
            }
            throw e;
        }
    }

    @Around("cacheEvictPointcut()")
    public Object cacheEvictPointcut(ProceedingJoinPoint joinPoint) throws Throwable {
        CacheOperationInvoker aopAllianceInvoker = getCacheOperationInvoker(joinPoint);

        // 获取method
        Method method = this.getSpecificmethod(joinPoint);
        // 获取注解
        CacheEvict cacheEvict = AnnotationUtils.findAnnotation(method, CacheEvict.class);

        try {
            // 执行查询缓存方法
            return executeEvict(aopAllianceInvoker, cacheEvict, method, joinPoint.getArgs(), joinPoint.getTarget());
        } catch (Exception e) {
            // 忽略操作缓存过程中遇到的异常
            if (cacheEvict.ignoreException()) {
                logger.warn(e.getMessage(), e);
                return aopAllianceInvoker.invoke();
            }
            throw e;
        }
    }

    @Around("cachePutPointcut()")
    public Object cachePutPointcut(ProceedingJoinPoint joinPoint) throws Throwable {
        CacheOperationInvoker aopAllianceInvoker = getCacheOperationInvoker(joinPoint);

        // 获取method
        Method method = this.getSpecificmethod(joinPoint);
        // 获取注解
        CachePut cacheEvict = AnnotationUtils.findAnnotation(method, CachePut.class);

        try {
            // 执行查询缓存方法
            return executePut(aopAllianceInvoker, cacheEvict, method, joinPoint.getArgs(), joinPoint.getTarget());
        } catch (Exception e) {
            // 忽略操作缓存过程中遇到的异常
            if (cacheEvict.ignoreException()) {
                logger.warn(e.getMessage(), e);
                return aopAllianceInvoker.invoke();
            }
            throw e;
        }
    }

    /**
     * 执行Cacheable切面
     *
     * @param invoker   缓存注解的回调方法
     * @param cacheable {@link Cacheable}
     * @param method    {@link Method}
     * @param args      注解方法参数
     * @param target    target
     * @return {@link Object}
     */
    private Object executeCacheable(CacheOperationInvoker invoker, Cacheable cacheable,
                                    Method method, Object[] args, Object target) {

        // 解析SpEL表达式获取cacheName和key
        String[] cacheNames = cacheable.cacheNames();
        Assert.notEmpty(cacheable.cacheNames(), CACHE_NAME_ERROR_MESSAGE);
        String cacheName = cacheNames[0];
        Object key = cacheable.key();
        if(!cacheable.pureKey()) {
        	key = generateKey(cacheable.key(), method, args, target);
        }
        Assert.notNull(key, String.format(CACHE_KEY_ERROR_MESSAGE, cacheable.key()));

        // 从注解中获取缓存配置
        FirstCache firstCache = cacheable.firstCache();
        // 处理过期时间
        int firstCacheExpireTime = getFirstCacheExpireTime(firstCache, method, args, target);
        SecondaryCache secondaryCache = cacheable.secondaryCache();
        // 处理过期时间
        int secondaryCacheExpireTime = getSecondaryCacheExpireTime(secondaryCache, method, args, target);
        FirstCacheSetting firstCacheSetting = new FirstCacheSetting(firstCache.initialCapacity(), firstCache.maximumSize(),
        		firstCacheExpireTime, firstCache.timeUnit(), firstCache.expireMode());

        SecondaryCacheSetting secondaryCacheSetting = new SecondaryCacheSetting(secondaryCacheExpireTime,
                secondaryCache.preloadTime(), secondaryCache.timeUnit(), secondaryCache.forceRefresh(),
                secondaryCache.isAllowNullValue(), secondaryCache.magnification());

        CacheSetting cacheSetting = new CacheSetting(firstCacheSetting, secondaryCacheSetting,
                cacheable.depict());

        // 通过cacheName和缓存配置获取Cache
        ICache cache = cacheManager.getCache(cacheName, cacheSetting);

        // 通Cache获取值
        return cache.get(key, () -> invoker.invoke());
    }

    /**
     * 执行 CacheEvict 切面
     *
     * @param invoker    缓存注解的回调方法
     * @param cacheEvict {@link CacheEvict}
     * @param method     {@link Method}
     * @param args       注解方法参数
     * @param target     target
     * @return {@link Object}
     */
    private Object executeEvict(CacheOperationInvoker invoker, CacheEvict cacheEvict,
                                Method method, Object[] args, Object target) {

        // 解析SpEL表达式获取cacheName和key
        String[] cacheNames = cacheEvict.cacheNames();
        Assert.notEmpty(cacheEvict.cacheNames(), CACHE_NAME_ERROR_MESSAGE);
        // 判断是否删除所有缓存数据
        if (cacheEvict.allEntries()) {
            // 删除所有缓存数据（清空）
            for (String cacheName : cacheNames) {
                Collection<ICache> caches = cacheManager.getCache(cacheName);
                if (CollectionUtils.isEmpty(caches)) {
                    // 如果没有找到Cache就新建一个默认的
                    ICache cache = cacheManager.getCache(cacheName,
                            new CacheSetting(new FirstCacheSetting(), new SecondaryCacheSetting(), "默认缓存配置（清除时生成）"));
                    cache.clear();
                } else {
                    for (ICache cache : caches) {
                        cache.clear();
                    }
                }
            }
        } else {
            // 删除指定key
            delete(cacheNames, cacheEvict.key(), cacheEvict.pureKey(), method, args, target);
        }

        // 执行方法
        return invoker.invoke();
    }

    /**
     * 删除执行缓存名称上的指定key
     *
     * @param cacheNames 缓存名称
     * @param keySpEL    key的SpEL表达式
     * @param method     {@link Method}
     * @param args       参数列表
     * @param target     目标类
     */
    private void delete(String[] cacheNames, String keySpEL, boolean pureKey, Method method, Object[] args, Object target) {
    	Object key = keySpEL;
    	if(!pureKey) {
    		key = generateKey(keySpEL, method, args, target);
    	}
        Assert.notNull(key, String.format(CACHE_KEY_ERROR_MESSAGE, keySpEL));
        for (String cacheName : cacheNames) {
            Collection<ICache> caches = cacheManager.getCache(cacheName);
            if (CollectionUtils.isEmpty(caches)) {
                // 如果没有找到Cache就新建一个默认的
                ICache cache = cacheManager.getCache(cacheName,
                        new CacheSetting(new FirstCacheSetting(), new SecondaryCacheSetting(), "默认缓存配置（删除时生成）"));
                cache.evict(key);
            } else {
                for (ICache cache : caches) {
                    cache.evict(key);
                }
            }
        }
    }

    /**
     * 执行 CachePut 切面
     *
     * @param invoker  缓存注解的回调方法
     * @param cachePut {@link CachePut}
     * @param method   {@link Method}
     * @param args     注解方法参数
     * @param target   target
     * @return {@link Object}
     */
    private Object executePut(CacheOperationInvoker invoker, CachePut cachePut, Method method, Object[] args, Object target) {


        String[] cacheNames = cachePut.cacheNames();
        Assert.notEmpty(cachePut.cacheNames(), CACHE_NAME_ERROR_MESSAGE);
        Object key = cachePut.key();
        if(!cachePut.pureKey()) {
        	// 解析SpEL表达式获取 key
        	key = generateKey(cachePut.key(), method, args, target);
        }
        Assert.notNull(key, String.format(CACHE_KEY_ERROR_MESSAGE, cachePut.key()));

        // 从解决中获取缓存配置
        FirstCache firstCache = cachePut.firstCache();
        // 处理过期时间
        int firstCacheExpireTime = getFirstCacheExpireTime(firstCache, method, args, target);
        SecondaryCache secondaryCache = cachePut.secondaryCache();
        // 处理过期时间
        int secondaryCacheExpireTime = getSecondaryCacheExpireTime(secondaryCache, method, args, target);
        FirstCacheSetting firstCacheSetting = new FirstCacheSetting(firstCache.initialCapacity(), firstCache.maximumSize(),
        		firstCacheExpireTime, firstCache.timeUnit(), firstCache.expireMode());

        SecondaryCacheSetting secondaryCacheSetting = new SecondaryCacheSetting(secondaryCacheExpireTime,
                secondaryCache.preloadTime(), secondaryCache.timeUnit(), secondaryCache.forceRefresh(),
                secondaryCache.isAllowNullValue(), secondaryCache.magnification());

        CacheSetting cacheSetting = new CacheSetting(firstCacheSetting, secondaryCacheSetting,
                cachePut.depict());

        // 指定调用方法获取缓存值
        Object result = invoker.invoke();

        for (String cacheName : cacheNames) {
            // 通过cacheName和缓存配置获取Cache
            ICache cache = cacheManager.getCache(cacheName, cacheSetting);
            cache.put(key, result);
        }

        return result;
    }

    private CacheOperationInvoker getCacheOperationInvoker(ProceedingJoinPoint joinPoint) {
        return () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable ex) {
                throw new CacheOperationInvoker.ThrowableWrapperException(ex);
            }
        };
    }

    /**
     * 解析SpEL表达式，获取注解上的key属性值
     *
     * @return Object
     */
    private Object generateKey(String keySpEl, Method method, Object[] args, Object target) {
        // 获取注解上的key属性值
        Class<?> targetClass = getTargetClass(target);
        if (StringUtils.hasText(keySpEl)) {
            EvaluationContext evaluationContext = evaluator.createEvaluationContext(method, args, target,
                    targetClass, CacheOperationExpressionEvaluator.NO_RESULT);

            AnnotatedElementKey methodCacheKey = new AnnotatedElementKey(method, targetClass);
            // 兼容传null值得情况
            Object keyValue = evaluator.key(keySpEl, methodCacheKey, evaluationContext);
            return Objects.isNull(keyValue) ? "null" : keyValue;
        }
        return this.keyGenerator.generate(target, method, args);
    }
    
    /**
     * 获取firstCache的过期时间
     * <p>优先获取expireTimeExp所计算出来的过期时间，没有或者计算出错时获取expireTime的值。</p>
     * @param firstCache
     * @param method
     * @param args
     * @param target
     * @return
     */
    private int getFirstCacheExpireTime(FirstCache firstCache, Method method, Object[] args, Object target) {
    	if(firstCache==null) {
    		return 0;
    	}
    	int expireTime = firstCache.expireTime();
    	Object expireTimeByExp = generateExpireTime(firstCache.expireTimeExp(), method, args, target);
    	if(BeanUtils.isNotEmpty(expireTimeByExp)) {
    		if(expireTimeByExp instanceof String) {
    			try{
    				int parseInt = Integer.parseInt(((String)expireTimeByExp));
    				if(parseInt > -1) {
    					expireTime = parseInt;
    				}
    			}
    			catch(Exception ex) {
    				logger.warn("解析方法：{}上的expireTimeExp：{}时出错了", method.getName(), firstCache.expireTimeExp());
    			}
    		}
    		else if(expireTimeByExp instanceof Integer) {
    			expireTime = (Integer)expireTimeByExp;
    		}
    	}
    	return expireTime;
    }
    
    /**
     * 获取secondaryCache的过期时间
     * <p>优先获取expireTimeExp所计算出来的过期时间，没有或者计算出错时获取expireTime的值。</p>
     * @param secondaryCache
     * @param method
     * @param args
     * @param target
     * @return
     */
    private int getSecondaryCacheExpireTime(SecondaryCache secondaryCache, Method method, Object[] args, Object target) {
    	if(secondaryCache==null) {
    		return 0;
    	}
    	int expireTime = secondaryCache.expireTime();
    	Object expireTimeByExp = generateExpireTime(secondaryCache.expireTimeExp(), method, args, target);
    	if(BeanUtils.isNotEmpty(expireTimeByExp)) {
    		if(expireTimeByExp instanceof String) {
    			try{
    				int parseInt = Integer.parseInt(((String)expireTimeByExp));
    				if(parseInt > -1) {
    					expireTime = parseInt;
    				}
    			}
    			catch(Exception ex) {
    				logger.warn("解析方法：{}上的expireTimeExp：{}时出错了", method.getName(), secondaryCache.expireTimeExp());
    			}
    		}
    		else if(expireTimeByExp instanceof Integer) {
    			expireTime = (Integer)expireTimeByExp;
    		}
    	}
    	return expireTime;
    }
    
    /**
     * 解析SpEL表达式，获取注解上的expireTimeExp属性值
     * @param expireTimeExp
     * @param method
     * @param args
     * @param target
     * @return
     */
    private Object generateExpireTime(String expireTimeExp, Method method, Object[] args, Object target) {
    	// 获取注解上的key属性值
        Class<?> targetClass = getTargetClass(target);
        if (StringUtils.hasText(expireTimeExp)) {
            EvaluationContext evaluationContext = evaluator.createEvaluationContext(method, args, target,
                    targetClass, CacheOperationExpressionEvaluator.NO_RESULT);

            AnnotatedElementKey methodCacheKey = new AnnotatedElementKey(method, targetClass);
            // 兼容传null值得情况
            Object keyValue = evaluator.key(expireTimeExp, methodCacheKey, evaluationContext);
            return Objects.isNull(keyValue) ? "null" : keyValue;
        }
        return null;
    }

    /**
     * 获取类信息
     *
     * @param target Object
     * @return targetClass
     */
    private Class<?> getTargetClass(Object target) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        if (targetClass == null) {
            targetClass = target.getClass();
        }
        return targetClass;
    }


    /**
     * 获取Method
     *
     * @param pjp ProceedingJoinPoint
     * @return {@link Method}
     */
    private Method getSpecificmethod(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        // The method may be on an interface, but we need attributes from the
        // target class. If the target class is null, the method will be
        // unchanged.
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(pjp.getTarget());
        if (targetClass == null && pjp.getTarget() != null) {
            targetClass = pjp.getTarget().getClass();
        }
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the
        // original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        return specificMethod;
    }
}