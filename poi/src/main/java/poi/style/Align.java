package poi.style;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * 水平对齐方式
 * @author zxh
 *
 */
public enum Align {	
	/**
	 * 
	 */
	GENERAL(HorizontalAlignment.GENERAL),
	
	/**
	 * 
	 */
	LEFT(HorizontalAlignment.LEFT),
	
	/**
	 * 
	 */
	CENTER(HorizontalAlignment.CENTER),
	
	/**
	 * 
	 */
	RIGHT(HorizontalAlignment.RIGHT),

	/**
	 * 
	 */
	FILL(HorizontalAlignment.FILL),
	
	/**
	 * 
	 */
	JUSTIFY(HorizontalAlignment.JUSTIFY),
	
	/**
	 * 
	 */
	CENTER_SELECTION(HorizontalAlignment.CENTER_SELECTION);
	
	private HorizontalAlignment alignment;

	private Align(HorizontalAlignment alignment) {
		this.alignment = alignment;
	}

	public HorizontalAlignment getAlignment() {
		return alignment;
	}
}
