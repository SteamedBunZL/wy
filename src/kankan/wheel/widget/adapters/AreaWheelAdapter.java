package kankan.wheel.widget.adapters;

import java.util.List;

import com.whoyao.engine.client.ClientEngine;
import com.whoyao.model.AreaData;
import com.whoyao.utils.L;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.util.FloatMath;
/**
 * 
 * 类说明
 * @author hyh
 * create at：2013-3-28 下午03:46:52
 * @param <T>
 */
public class AreaWheelAdapter extends kankan.wheel.widget.adapters.ListWheelAdapter<AreaData> {


	int maxWidth = ClientEngine.getMobileInfo().vgawidth;//Constant.SCREEN_W / 2;//屏幕宽度的一半
	
	int maxLength;
	
	/**
	 * Constructor
	 * @param list
	 */
	@SuppressLint("FloatMath")
	public AreaWheelAdapter(Context context, List<AreaData> items) {
		super(context, items);
		
		TextPaint itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG| Paint.FAKE_BOLD_TEXT_FLAG);
		itemsPaint.setTextSize(AbstractWheelTextAdapter.DEFAULT_TEXT_SIZE);
		float textWidth = FloatMath.ceil(Layout.getDesiredWidth("0", itemsPaint));
		maxLength = (int) (maxWidth / textWidth / 3);
		L.i("AreaAdapter", "maxLength = "+ maxLength);
		L.i("AreaAdapter", "maxWidth = "+ maxWidth);
	}

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.size()) {
        	String item = items.get(index).getAreaName();
        	if(maxLength < item.length()){
        		item = item.substring(0, maxLength);
    		}
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item.toString();
        }
        return null;
    }


}
