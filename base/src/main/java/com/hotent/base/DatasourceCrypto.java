package com.hotent.base;

import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;

/**
 * druid数据源密码加密生成器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月6日
 */
public class DatasourceCrypto {
	public static void main(String[] args) throws Exception {
		// 数据库密码
        String password = "eip7";

        // 自定义publicKey
        String[] arr = CryptoUtils.genKeyPair(512);
        // 非对称加密的私钥
        System.out.println("privateKey:" + arr[0]);
        // 非对称加密的公钥(这个公钥配置到spring.datasource.dynamic.datasource.数据源组名.druid.public-key)
        // 注意：每一次生成生成的public-key与密码加密字符串都是成对的，需要一起替换
        System.out.println("publicKey:" + arr[1]);
        // 数据库密码被加密后的字符串(这个配置到spring.datasource.dynamic.datasource.数据源组名.password)
        // 注意：每一次生成的密码加密字符串与public-key都是成对的，需要一起替换
        System.out.println("password:" + CryptoUtils.encrypt(arr[0], password));
    }
}
