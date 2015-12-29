package com.whoyao.fragment;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author hyh creat_at：2013-9-3-下午4:42:56
 */
public class DevelopingFragment extends Fragment{

	private TextView tvTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_developing, container, false);
		 tvTitle = (TextView)v.findViewById(R.id.page_tv_title);
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Bundle bundle = getArguments();
		String title = null;
		if(bundle != null){
			title = bundle.getString(Extra.Title);
		}
		if(tvTitle != null){
			if(!TextUtils.isEmpty(title)){
				tvTitle.setText(title);
			}else{
				tvTitle.setText(R.string.app_name);
			}
		}
	}
}
