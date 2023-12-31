package com.hotent.bpm.api.model.process.nodedef.ext;

/**
 * 并签 顺签 并审
 
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：jason
 * 邮箱:liygui@jee-soft.cn
 * 日期:2020-3-1-上午9:56:25
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class CustomSignNodeDef  extends UserTaskNodeDef {
	
	/**
	 * 并行签署
	 */
	public static final String SIGNTYPE_PARALLEL = "Parallel";
	/**
	 * 顺序签署
	 */
	public static final String SIGNTYPE_SEQUENTIAL = "Sequential";
	/**
	 * 并行审批
	 */
	public static final String SIGNTYPE_PARALLELAPPROVE = "ParallelApprove";
	/**
	 * 开始串并签之前
	 */
	public static final String BEFORE_SIGN = "BeforeSign";
	/**
	 * 串并签之后
	 */
	public static final String AFTER_SIGN = "AfterSign";
	
	private static final long serialVersionUID = -12965499812901552L;

	/**
	 * 是否串行会签。
	 */
	private boolean isParallel=false;
	
	/**
	 *  并签  顺签  并审
	 */
	private String signType;
	

	
	@Override
	public void setParallel(boolean isParallel) {
		this.isParallel = isParallel;
	}
	
	@Override
	public boolean isParallel() {
	
		return this.isParallel;
	}

	
	@Override
	public boolean supportMuliInstance() {
		return true;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

}
