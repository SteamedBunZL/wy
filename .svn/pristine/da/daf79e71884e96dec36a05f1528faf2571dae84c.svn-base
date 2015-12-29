package com.whoyao.adapter;

import android.content.Context;
import android.text.TextUtils;


/**
 * 简单的数组滚轴适配器
 * 
 */
public class IntWheelAdapter extends kankan.wheel.widget.adapters.AbstractWheelTextAdapter {


	//日期后的标签
	private String label = "";
	
	// items
    private int items[];

    /**
     * Constructor
     * @param context the current context
     * @param items the items
     */
    public IntWheelAdapter(Context context, int items[]) {
        super(context);
        
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = items;
    }
    
    /**
     * Constructor
     * @param context the current context
     * @param items the items
     * @param label 
     */
    public IntWheelAdapter(Context context, int items[], String label) {
        this(context,items);
        if(!TextUtils.isEmpty(label)){
        	this.label = label;
        }
    }
    
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.length) {
            String item = items[index] +label;
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item.toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items.length;
    }
	
}
