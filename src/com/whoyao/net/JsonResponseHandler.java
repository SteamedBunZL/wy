package com.whoyao.net;

import java.util.List;

import com.whoyao.utils.JSON;

/**
 * @author hyh 
 * creat_at：2013-11-7-上午11:11:34
 */
public class JsonResponseHandler <T> extends ResponseHandler {

	private Class<T> cls;

	public JsonResponseHandler(Class<T> clazz) {
		super();
		cls = clazz;
	}

	public JsonResponseHandler(boolean isShowProgress, Class<T> clazz) {
		super(isShowProgress);
		cls = clazz;
	}
	
	@Override
	public void onSuccess(String content) {
		T object = JSON.getObject(content, cls);
		if(null == object){
			onSuccess(JSON.getList(content, cls));
		}
		onSuccess(object);
	}
	
	public void onSuccess(T object) {

	}
	public void onSuccess(List<T> list) {
		
	}
}
