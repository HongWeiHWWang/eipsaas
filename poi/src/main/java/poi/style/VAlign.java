package poi.style;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;


/**
 * 垂直对齐方式
 * @author zxh
 *
 */
public enum VAlign {
	/**
	 * 
	 */
	TOP(VerticalAlignment.TOP),
	
	/**
	 * 
	 */
	CENTER(VerticalAlignment.CENTER),
	
	/**
	 * 
	 */
	BOTTOM(VerticalAlignment.BOTTOM),
	
	/**
	 * 
	 */
	JUSTIFY(VerticalAlignment.JUSTIFY);
	
	private VerticalAlignment alignment;

	private VAlign(VerticalAlignment alignment){
		this.alignment = alignment;
	}

	public VerticalAlignment getAlignment() {
		return alignment;
	}
}
