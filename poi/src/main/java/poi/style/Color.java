package poi.style;

import org.apache.poi.hssf.util.HSSFColor;

/**
 * 颜色
 * @author zxh
 */
public enum Color {
	/**
	 * 默认颜色
	 */
	AUTOMATIC(HSSFColor.HSSFColorPredefined.AUTOMATIC.getIndex()),
	
	/**
	 * 浅绿色
	 */
	AQUA(HSSFColor.HSSFColorPredefined.AQUA.getIndex()),
	
	/**
	 * 黑色
	 */
	BLACK(HSSFColor.HSSFColorPredefined.BLACK.getIndex()),
	
	/**
	 * 蓝色
	 */
	BLUE(HSSFColor.HSSFColorPredefined.BLUE.getIndex()),
	
	/**
	 * 蓝灰色
	 */
	BLUE_GREY(HSSFColor.HSSFColorPredefined.BLUE_GREY.getIndex()),
	
	/**
	 * 鲜绿色
	 */
	BRIGHT_GREEN(HSSFColor.HSSFColorPredefined.BRIGHT_GREEN.getIndex()),
	
	/**
	 * 棕色
	 */
	BROWN(HSSFColor.HSSFColorPredefined.BROWN.getIndex()),
	
	/**
	 * 珊瑚红
	 */
	CORAL(HSSFColor.HSSFColorPredefined.CORAL.getIndex()),
	
	/**
	 * 浅蓝色,矢车菊蓝
	 */
	CORNFLOWER_BLUE(HSSFColor.HSSFColorPredefined.CORNFLOWER_BLUE.getIndex()),
	
	/**
	 * 深蓝色
	 */
	DARK_BLUE(HSSFColor.HSSFColorPredefined.DARK_BLUE.getIndex()),
	
	/**
	 * 深绿色
	 */
	DARK_GREEN(HSSFColor.HSSFColorPredefined.DARK_GREEN.getIndex()),
	
	/**
	 * 深红色
	 */
	DARK_RED(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex()),
	
	/**
	 * 深青色
	 */
	DARK_TEAL(HSSFColor.HSSFColorPredefined.DARK_TEAL.getIndex()),
	
	/**
	 * 深黄色
	 */
	DARK_YELLOW(HSSFColor.HSSFColorPredefined.DARK_YELLOW.getIndex()),
	
	/**
	 * 金色
	 */
	GOLD(HSSFColor.HSSFColorPredefined.GOLD.getIndex()),
	
	/**
	 * 绿色
	 */
	GREEN(HSSFColor.HSSFColorPredefined.GREEN.getIndex()),
	
	/**
	 * 25%浓度灰色
	 */
	GREY_25_PERCENT(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex()),
	
	/**
	 * 40%浓度灰色
	 */
	GREY_40_PERCENT(HSSFColor.HSSFColorPredefined.GREY_40_PERCENT.getIndex()),
	
	/**
	 * 50%浓度灰色
	 */
	GREY_50_PERCENT(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex()),
	
	/**
	 * 80%浓度灰色
	 */
	GREY_80_PERCENT(HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex()),
	
	/**
	 * 靛蓝色
	 */
	INDIGO(HSSFColor.HSSFColorPredefined.INDIGO.getIndex()),
	
	/**
	 * 浅紫色
	 */
	LAVENDER(HSSFColor.HSSFColorPredefined.LAVENDER.getIndex()),
	
	/**
	 * 浅柠檬色
	 */
	LEMON_CHIFFON(HSSFColor.HSSFColorPredefined.LEMON_CHIFFON.getIndex()),
	
	/**
	 * 浅蓝色
	 */
	LIGHT_BLUE(HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getIndex()),
	
	/**
	 * 浅蓝色，浅矢车菊蓝
	 */
	LIGHT_CORNFLOWER_BLUE(HSSFColor.HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex()),
	
	/**
	 * 浅绿色
	 */
	LIGHT_GREEN(HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getIndex()),
	
	/**
	 * 浅橙色
	 */
	LIGHT_ORANGE(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex()),
	
	/**
	 * 浅绿蓝
	 */
	LIGHT_TURQUOISE(HSSFColor.HSSFColorPredefined.LIGHT_TURQUOISE.getIndex()),
	
	/**
	 * 浅黄色
	 */
	LIGHT_YELLOW(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex()),
	
	/**
	 * 绿黄，亮绿色
	 */
	LIME(HSSFColor.HSSFColorPredefined.LIME.getIndex()),
	
	/**
	 *栗色
	 */
	MAROON(HSSFColor.HSSFColorPredefined.MAROON.getIndex()),
	
	/**
	 * 橄榄绿
	 */
	OLIVE_GREEN(HSSFColor.HSSFColorPredefined.OLIVE_GREEN.getIndex()),
	
	/**
	 * 橙色
	 */
	ORANGE(HSSFColor.HSSFColorPredefined.ORANGE.getIndex()),
	
	/**
	 * 淡紫色
	 */
	ORCHID(HSSFColor.HSSFColorPredefined.ORCHID.getIndex()),
	
	/**
	 * 淡蓝色，粉青
	 */
	PALE_BLUE(HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex()),
	
	/**
	 * 粉红色
	 */
	PINK(HSSFColor.HSSFColorPredefined.PINK.getIndex()),
	
	/**
	 * 紫红色
	 */
	PLUM(HSSFColor.HSSFColorPredefined.PLUM.getIndex()),
	
	/**
	 * 红色
	 */
	RED(HSSFColor.HSSFColorPredefined.RED.getIndex()),
	
	/**
	 * 玫瑰红
	 */
	ROSE(HSSFColor.HSSFColorPredefined.ROSE.getIndex()),
	
	/**
	 * 皇家蓝，宝蓝色
	 */
	ROYAL_BLUE(HSSFColor.HSSFColorPredefined.ROYAL_BLUE.getIndex()),
	
	/**
	 * 海藻绿
	 */
	SEA_GREEN(HSSFColor.HSSFColorPredefined.SEA_GREEN.getIndex()),
	
	/**
	 * 天蓝色
	 */
	SKY_BLUE(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex()),
	
	/**
	 * 棕褐色
	 */
	TAN(HSSFColor.HSSFColorPredefined.TAN.getIndex()),
	
	/**
	 * 青色
	 */
	TEAL(HSSFColor.HSSFColorPredefined.TEAL.getIndex()),
	
	/**
	 * 宝石绿，蓝绿色
	 */
	TURQUOISE(HSSFColor.HSSFColorPredefined.TURQUOISE.getIndex()),
	
	/**
	 * 紫色，紫罗兰色
	 */
	VIOLET(HSSFColor.HSSFColorPredefined.VIOLET.getIndex()),
	
	/**
	 * 白色
	 */
	WHITE(HSSFColor.HSSFColorPredefined.WHITE.getIndex()),
	
	/**
	 * 黄色
	 */
	YELLOW(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
	
	private short index;

	private Color(short index){
		this.index = index;
	}

	public short getIndex() {
		return index;
	}
	
	/**
	 * 根据值返回对应的枚举值
	 * @param weight
	 * @return
	 */
	public static Color instance(short index){
		for(Color e : Color.values()){
			if(e.getIndex() == index){
				return e;
			}
		}
		return Color.AUTOMATIC;
	}
}
