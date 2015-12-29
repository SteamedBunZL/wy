package com.whoyao.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.utils.FileUtils;

/**
 * @author hyh 
 * creat_at：2013-9-29-下午3:56:49
 */
public class CodeResponseHandler extends BinaryHttpResponseHandler {
	
	private CallBack<File> callBack;

	public CodeResponseHandler(CallBack<File> callBack) {
		super();
		this.callBack = callBack;
	}

	@Override
	public void onSuccess(byte[] binaryData) {
		if(null == binaryData){
			return;
		}
		File file = FileUtils.findImage(AppContext.serviceTimeMillis()+".jpg");
		File filePath = file.getParentFile();
		if(!filePath.exists()){
			filePath.mkdirs();
		}
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(binaryData);
			fos.flush();
			callBack.onCallBack(file);
		} catch (IOException e) {
			AppException.handle(e);
		}finally{
			try {
				fos.close();
			} catch (IOException e) {
				AppException.handle(e);
			}
		}
	}
}
