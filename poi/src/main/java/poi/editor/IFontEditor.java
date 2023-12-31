package poi.editor;

import poi.style.font.Font;




/**
 * 字体编辑器
 *@author zxh
 */
public interface IFontEditor {
	/**
	 * 修改字体属性
	 * @param font 字体，可设置或获取字体属性
	 */
	public void updateFont(Font font);
}
