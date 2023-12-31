package com.hotent.system.persistence.manager;
import com.hotent.base.manager.BaseManager;
import com.hotent.system.model.SysExternalUnite;

import java.io.IOException;

/**
 *
 * <pre>
 * 描述：系统第三方集成 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:PangQuan
 * 邮箱:PangQuan@jee-soft.cn
 * 日期:2019-11-26 16:07:01
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface SysExternalUniteManager extends BaseManager<SysExternalUnite> {

	boolean isTypeExists(String type, String id);

	void syncUser(String uniteId) throws IOException;

	SysExternalUnite getWechatWork();

	SysExternalUnite getDingtalk();

	SysExternalUnite getWeChatOfficialAccounts();

	void saveAgent(SysExternalUnite sysExternalUnite) throws IOException;

	void pullUser(String uniteId) throws IOException;

}
