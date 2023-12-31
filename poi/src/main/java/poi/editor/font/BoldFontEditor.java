package poi.editor.font;


import poi.editor.IFontEditor;
import poi.style.font.BoldWeight;
import poi.style.font.Font;
/**
 * 实现一些常用的字体<br/>
 * 该类用于把字体加粗
 * @author zxh
 *
 */
public class BoldFontEditor implements IFontEditor {

	public void updateFont(Font font) {
		font.boldweight(BoldWeight.BOLD);
	}

}
