package handmark.pulltorefresh.library;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public abstract class MyClass<T extends View> extends LinearLayout implements IPullToRefresh<T> {

	public MyClass(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

}
