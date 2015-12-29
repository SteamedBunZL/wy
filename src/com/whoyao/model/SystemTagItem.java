package com.whoyao.model;

import java.io.Serializable;

public class SystemTagItem implements Serializable{
	private int tagid;
	private String tag;
	public int getTagid() {
		return tagid;
	}
	public void setTagid(int tagid) {
		this.tagid = tagid;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	

}
