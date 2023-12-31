package com.hotent.uc.manager;

import org.springframework.web.multipart.MultipartFile;

import com.hotent.base.model.CommonResult;
import com.hotent.uc.params.org.OaAsyncObject;
import com.hotent.uc.params.user.UserImportVo;


/**
 * 
 * <pre>
 * 描述：用户表 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:26:50
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserImportManager{
	
	
	/**
	 * 导入Excel用户组织
	 * @param file
	 * @param demCode
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> importExcelUser(MultipartFile file,String demCode);
	
	/**
	 * 导入用户组织关系
	 * @param file
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> importZipUser(MultipartFile file,UserImportVo importVo)  throws Exception ;
	
	/**
	 * 
	 * @param oaAsyncObject
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> syncSoap(OaAsyncObject oaAsyncObject,String ip) throws Exception ;
	
	/**
 	 * AD域同步
 	 * @param action 全量：“all”,其他字符为增量
 	 * @param ip 全量：操作ip
 	 * @return
 	 * @throws Exception
 	 */
 	CommonResult<String> syncADUsers(String action,String ip)throws Exception;
}
