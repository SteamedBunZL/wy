package com.whoyao.model;

/**
 * @author hyh 
 * creat_at：2013-7-30-下午1:07:31
 */
public class TagItemModel extends TagModel{
	

	private boolean isSelected;
	
	
	public TagItemModel(int id, String name) {
		TagId = id;
		TagName = name;
	}
	public TagItemModel(TagModel tag,boolean isSelect) {
		TagId = tag.getTagId();
		TagName = tag.getTagName();
		isSelected = isSelect;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
		
}
