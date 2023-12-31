package poi.editor.font;


import poi.editor.IFontEditor;
import poi.style.font.Font;

/**
 * 实现一些常用的字体<br/>
 * 该类用于设置斜体
 * 
 * @author zxh
 * 
 */
public class ItalicFontEditor implements IFontEditor {

	public void updateFont(Font font) {
		font.italic(true);
	}

}
