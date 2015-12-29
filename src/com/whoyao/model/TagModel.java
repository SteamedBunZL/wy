package com.whoyao.model;

/**
 * @author hyh 
 * creat_at：2013-7-30-下午1:07:31
 */
public class TagModel {
	
	protected  int TagId;
	protected String TagName;
	
	
	public TagModel() { }
	public TagModel(int id, String name) {
		TagId = id;
		TagName = name;
	}
	public int getTagId() {
		return TagId;
	}
	public void setTagId(int tagId) {
		TagId = tagId;
	}
	public String getTagName() {
		return TagName;
	}
	public void setTagName(String tagName) {
		TagName = tagName;
	}
		
}
