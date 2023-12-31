package poi.style;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

/**
 * 边框样式
 * @author zxh
 *
 */
public enum BorderStyle {
	/**
	 * 无边框
	 */
	NONE(org.apache.poi.ss.usermodel.BorderStyle.NONE),

	/**
	 * 细实线
	 */
	THIN(org.apache.poi.ss.usermodel.BorderStyle.THIN),

	/**
	 * 粗实线
	 */
	MEDIUM(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM),

	/**
	  * 最粗的实线
	  */
	THICK(org.apache.poi.ss.usermodel.BorderStyle.THICK),
	
	/**
	 * 细虚线
	 */
	DASHED(org.apache.poi.ss.usermodel.BorderStyle.DASHED),

	/**
	  * 细点线
	  */
	HAIR(org.apache.poi.ss.usermodel.BorderStyle.HAIR),

	/**
	  * 双实线
	  */
	DOUBLE(org.apache.poi.ss.usermodel.BorderStyle.DOUBLE),

	/**
	  * 细密点线
	  */
	DOTTED(org.apache.poi.ss.usermodel.BorderStyle.DOTTED),

	/**
	  * 粗虚线
	  */
	MEDIUM_DASHED(org.apache.poi.ss.usermodel.BorderStyle.DASHED),

	/**
	  * 虚线
	  */
	DASH_DOT(org.apache.poi.ss.usermodel.BorderStyle.DASH_DOT),

	/**
	  * 虚线-点，粗线
	  */
	MEDIUM_DASH_DOT(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM_DASH_DOT),

	/**
	  * 虚线-点-点，细线
	  */
	DASH_DOT_DOT(org.apache.poi.ss.usermodel.BorderStyle.DASH_DOT_DOT),

	/**
	  * 虚线-点-点，粗线
	  */
	MEDIUM_DASH_DOT_DOT(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM_DASH_DOT_DOT),

	/**
	  * 虚线-点，倾斜的粗线
	  */
	SLANTED_DASH_DOT(org.apache.poi.ss.usermodel.BorderStyle.SLANTED_DASH_DOT);

	private org.apache.poi.ss.usermodel.BorderStyle borderType;

	private BorderStyle(org.apache.poi.ss.usermodel.BorderStyle borderType) {
		this.borderType =  borderType;
	}

	public org.apache.poi.ss.usermodel.BorderStyle getBorderType() {
		return borderType;
	}
}
