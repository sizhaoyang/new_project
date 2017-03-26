package com.jaovo.cms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_group")
public class Group {
	/**
	 * 组ID
	 */
	private int id;
	/**
	 * 组名称
	 */
	private String name;

	/**
	 * 组描述信息
	 */
	private String descr;

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Group(int id, String name, String descr) {
		super();
		this.id = id;
		this.name = name;
		this.descr = descr;
	}

	public Group(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Group() {
		super();
	}

}
