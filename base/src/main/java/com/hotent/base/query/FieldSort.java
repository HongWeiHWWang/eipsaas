package com.hotent.base.query;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 排序对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月4日
 */
@ApiModel(description="排序对象")
public class FieldSort implements Serializable{
	private static final long serialVersionUID = -1712830705595375365L;
	
	@ApiModelProperty(name="property",notes="排序字段")
	private String property;
	@ApiModelProperty(name="direction", notes="排序方向", example="ASC")
	private Direction direction = Direction.ASC;

	public FieldSort(){}

	public FieldSort(String property) {
		this(property, Direction.ASC);
	}

	public FieldSort(String property, Direction direction) {
		this.direction = direction;
		this.property = property;
	}

	/**
	 * 构造器
	 * @param property 排序属性
	 * @param direction 排序方向
	 * @param clazz 实体类
	 * @return
	 */
	public static FieldSort create(String property, String direction){
		return new FieldSort(property, Direction.fromString(direction));
	}

	public Direction getDirection() {
		return direction;
	}

	public String getProperty() {
		return property;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}