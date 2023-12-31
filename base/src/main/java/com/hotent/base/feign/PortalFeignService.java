package com.hotent.base.feign;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.conf.FeignConfig;
import com.hotent.base.feign.impl.PortalFeignServiceImpl;
import com.hotent.base.jms.Notice;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;

/**
 *
 * @author liyg
 *
 */
@FeignClient(name="portal-eureka",fallback=PortalFeignServiceImpl.class, configuration=FeignConfig.class)
public interface PortalFeignService {

	@RequestMapping(value="/sys/sysRoleAuth/v1/getMethodRoleAuth",method=RequestMethod.GET)
	public List<HashMap<String,String>> getMethodRoleAuth();

	@RequestMapping(value="/sys/sysProperties/v1/getByAlias",method=RequestMethod.GET)
	public String getPropertyByAlias(@RequestParam(value="alias", required = true)String alias) ;

    @RequestMapping(value="/sys/sysProperties/v1/getByAlias",method=RequestMethod.GET)
    public String getByAlias(@RequestParam(value="alias", required = true)String alias,
                             @RequestParam(value="defaultValue", required = true)String defaultValue) ;

	/**
	 * 根据别名获取系统分类
	 *
	 */
	 @RequestMapping(value="/sys/sysType/v1/getJson",method=RequestMethod.GET)
	 public ObjectNode getSysTypeById(@RequestParam(value="id", required = true)String id);

	 /**
	 * 根据queryFilter获取系统分类。不带分页
	 */
	 @RequestMapping(value="/sys/sysType/v1/list",method=RequestMethod.POST)
	 public ObjectNode getAllSysType(@RequestBody(required = true)QueryFilter queryFilter);

	 /**
	 * 发送消息
	 * @param notice
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
     @RequestMapping(value="/portal/jms/v1/sendNoticeToQueue",method=RequestMethod.POST)
	 public CommonResult<String> sendNoticeToQueue(@RequestBody(required = true)Notice notice);

     /**
 	 * 发送消息到队列中
 	 * @param model
 	 * @return
 	 * @throws ClientProtocolException
 	 * @throws IOException
 	 */
     @RequestMapping(value="/portal/jms/v1/sendToQueue",method=RequestMethod.POST)
 	 public CommonResult<String> sendToQueue(@RequestBody(required = true)ObjectNode model);


     /**
 	 * 根据用户，指定工时，指定开始时间,计算任务实际完成时间
 	 * @param userId		用户ID
 	 * @param startTime		开始时间
 	 * @param time			工时(分钟)
 	 * @return				完成时间
 	 * @throws Exception
 	 */
     @RequestMapping(value="/portal/calendar/v1/getEndTimeByUser",method=RequestMethod.POST)
 	 public String getEndTimeByUser(@RequestBody(required = true)ObjectNode param);


     /**
 	 * 根据用户开始时间和结束时间，获取这段时间的有效工时
 	 * @param model
 	 * @return
 	 * @throws ClientProtocolException
 	 * @throws IOException
 	 */
     @RequestMapping(value="/portal/calendar/v1/getWorkTimeByUser",method=RequestMethod.POST)
 	 public Long getWorkTimeByUser(@RequestBody(required = true)ObjectNode param);


     /**
 	 * 获取用户已读未读消息
 	 * @param account
 	 * @return
 	 * @throws Exception
 	 */
     @RequestMapping(value="/innermsg/messageReceiver/v1/getMessBoxInfo",method=RequestMethod.GET)
 	 public ObjectNode getMessBoxInfo(@RequestParam(value="account", required = true)String account);


     /**
      * 根据数据源别名，获取数据源的设置信息
      * @param alias
      * @return
      */
     @RequestMapping(value="/sys/sysDataSource/v1/getBeanByAlias", method=RequestMethod.GET)
 	 public JsonNode getBeanByAlias(@RequestParam(value="alias", required = true) String alias);

     /**
     * 根据流水号别名获取下一个流水号
     * @param alias
     * @return
     */
    @RequestMapping(value="/sys/identity/v1/getNextIdByAlias", method=RequestMethod.GET)
	public String getNextIdByAlias(@RequestParam(value="alias", required = true) String alias);

    /**
   	 * 根据queryFilter获取新闻公告
   	 */
   	 @RequestMapping(value="/portal/messageNews/v1/list",method=RequestMethod.POST)
   	 public ObjectNode getMessageNews(@RequestBody(required = true)JsonNode queryFilter);

     /**
	 * 根据新闻公告id发布新闻公告
	 */
	 @RequestMapping(value="/portal/messageNews/v1/publicMsgNews",method=RequestMethod.POST)
	 public ObjectNode publicMsgNews(@RequestBody(required = true)String array);

	 @RequestMapping(value="/sys/sysLogsSettings/v1/getSysLogsSettingStatusMap",method=RequestMethod.GET)
	 public Map<String, String> getSysLogsSettingStatusMap();

	 /**
 	 * 根据权限数据判断当前人是否有权限
 	 * @param permssionJson
 	 * @return
 	 * @throws Exception
 	 */
     @RequestMapping(value="/sys/authUser/v1/calcPermssion",method=RequestMethod.GET)
 	 public boolean calcPermssion(@RequestParam(value="permssionJson", required = true)String permssionJson);

     /**
  	 * 根据权限数据判断当前人是否有权限
  	 * @param permssionJson
  	 * @return
  	 * @throws Exception
  	 */
      @RequestMapping(value="/sys/authUser/v1/calcAllPermssion",method=RequestMethod.GET)
  	 public ObjectNode calcAllPermssion(@RequestParam(value="permssionJson", required = true)String permssionJson);

	@RequestMapping(value="sys/authUser/v1/getAuthorizeIdsByUserMap", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	public List<String> getAuthorizeIdsByUserMap(@RequestParam(value="objType" , required = true)String objType) ;

	@RequestMapping(value="/portal/sysExternalUnite/v1/getToken", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	public String getToken(@RequestParam(value="type" , required = true) String type);

	/**
	 * 创建租户时 初始化租户数据
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value="/portal/tenantInitData/v1/initData", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	public CommonResult<String> initData(@RequestParam(value="tenantId" , required = true) String tenantId);

	@RequestMapping(value="/portal/sysExternalUnite/v1/getUserInfoUrl", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	public String getUserInfoUrl(@RequestParam(value="type" , required = true) String type,@RequestParam(value="code" , required = true) String code);

	@RequestMapping(value="/system/file/v1/wordPrint",method=RequestMethod.POST)
	public String wordPrint(@RequestBody ObjectNode objectNode) ;
}
