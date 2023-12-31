package com.hotent.base.example.model;

import java.io.IOException;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.JsonUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * student 实体类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月20日
 */
@ApiModel(description="学生实体类")
@TableName("ex_student")
public class Student extends BaseModel<Student>  {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(name="id",notes="主键")
	@TableId("id_")
    private String id;
    //姓名
	@ApiModelProperty(name="name",notes="名称")
	@TableField("name_")
    private String name;
    //生日
	@ApiModelProperty(name="birthday",notes="生日")
	@TableField("birthday_")
    private LocalDate birthday;
    //姓别
	@ApiModelProperty(name="sex",notes="性别")
	@TableField("sex_")
    private Short sex;
    //简短介绍
	@ApiModelProperty(name="desc",notes="描述")
	@TableField("desc_")
    private String desc;
	
	@TableField(exist=false)
	private String tmp;

    public Student() {
    }
    
    public Student(String id, String name, LocalDate birthday, Short sex, String desc){
    	this.id = id;
    	this.name = name;
    	this.birthday = birthday;
    	this.sex = sex;
    	this.desc = desc;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	@Override
    public String toString() {
    	String json = null;
		try {
			json = JsonUtil.toJson(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return json;
    }
}
