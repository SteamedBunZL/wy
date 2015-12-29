package com.whoyao.activity;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.protocol.UriPatternMatcher;

import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.ui.Toast;
import com.whoyao.utils.FileUtils;
import com.whoyao.R;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 选择照片
 * @author hyh creat_at：2013-7-31-下午8:27:41
 * 根据intent,判断是否需要裁剪
 */
public class SelectPhotoActivity extends AbsSelectActivity implements OnClickListener {
	private final int crop = 180;
	private boolean hasToCut = false;
	private File tempFile;
	private View chooseLl;
	private Button firstBtn;
	private Button secondBtn;
	private Button cancelBtn;
	private int aspectX;
	private int aspectY;
	private boolean isSDCardExist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isSDCardExist = FileUtils.isSDCardExist();
		setContentView(R.layout.activity_select_button3);
		hasToCut = getIntent().getBooleanExtra(Extra.HAS_TO_CUT, hasToCut);
		firstBtn = (Button) findViewById(R.id.select_btn_0);
		secondBtn = (Button) findViewById(R.id.select_btn_1);
		cancelBtn = (Button) findViewById(R.id.select_btn_cancle);
		firstBtn.setOnClickListener(this);
		secondBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		firstBtn.setText("拍照");
		secondBtn.setText("从相册选择");
		chooseLl = findViewById(R.id.select_ll_button);
		setSelectView(chooseLl);
		show();
		Intent intent = getIntent();
		aspectX = intent.getIntExtra(Extra.ASPECT_X, 1);
		aspectY = intent.getIntExtra(Extra.ASPECT_Y, 1);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.select_btn_0:// 从相机选择
			tempFile = getCameraFile();
			Intent intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");
			intentCamera.putExtra("output", Uri.fromFile(tempFile));
			startActivityForResult(intentCamera, R.id.select_btn_0);
			
			chooseLl.setVisibility(View.INVISIBLE);
			break;
		case R.id.select_btn_1:// 从相册选择
			Intent intent = new Intent("android.intent.action.PICK");
			intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
			tempFile = getShotFile();
			intent.putExtra("output", Uri.fromFile(tempFile));
			if (hasToCut) {
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", aspectX);// 裁剪框比例
				intent.putExtra("aspectY", aspectY);
//				intent.putExtra("outputX", crop);// 输出图片大小
//				intent.putExtra("outputY", crop);
			}
			startActivityForResult(intent, R.id.select_btn_1);
			break;
		case R.id.select_btn_cancle:
			dismiss();
			break;
		default:
			break;
		}
	}
	/**获取拍摄照片的存储路径*/
	private File getCameraFile() {
		File filePath = null;
		if(isSDCardExist){
			filePath = Const.SD_CAMERA_DIR;
		}else{
			filePath = new File(Const.CACHE_DIR, Const.PATH_IMAGE_CAMERA);
		}
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String tempFileName = AppContext.serviceTimeMillis() + ".jpg";
		return new File(filePath, tempFileName);
	}
	/**获取裁剪后照片的存储路径*/
	private File getShotFile() {
		File filePath = null;
		if(isSDCardExist){
			filePath = new File(Const.SD_CACHE_DIR, Const.PATH_IMAGE_SHOT);
		}else{
			filePath = new File(Const.CACHE_DIR, Const.PATH_IMAGE_SHOT);
		}
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String tempFileName = AppContext.serviceTimeMillis() + ".jpg";
		return new File(filePath, tempFileName);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismiss();
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.select_btn_0:// 相机
			if (RESULT_OK == resultCode && tempFile.exists()) {
				if (hasToCut) {
					Intent intent = new Intent();
					intent.setAction("com.android.camera.action.CROP");
					intent.setDataAndType(Uri.fromFile(tempFile), "image/*");
					tempFile = getShotFile();
					intent.putExtra("output", Uri.fromFile(tempFile));
					intent.putExtra("crop", "true");
					intent.putExtra("aspectX", aspectX);// 裁剪框比例
					intent.putExtra("aspectY", aspectY);
//					intent.putExtra("outputX", crop);// 输出图片大小
//					intent.putExtra("outputY", crop);
					startActivityForResult(intent, R.id.select_btn_1);
				} else {
					Intent intent = new Intent();
					intent.putExtra(Extra.IMAGE_PATH, tempFile.getPath());
					setResult(RESULT_OK, intent);
					//  删除toast,添加finish
					//Toast.show(tempFile.getPath());
					finish();
				}
			} else {
//				chooseLl.setVisibility(View.VISIBLE);
				finish();
			}
			break;
		case R.id.select_btn_1:// 相册
			if (RESULT_OK == resultCode && null != data) {
				if(!hasToCut){
					String filePath = null;
					Uri selectedImage = data.getData();
					if(null != selectedImage){
						String[] filePathColumn = {MediaStore.Images.Media.DATA};
						
						Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
						if(cursor.moveToFirst()){
							int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
							filePath = cursor.getString(columnIndex);
							cursor.close();
							if(filePath ==null){
								Toast.show("请选择本地照片");
								onClick(secondBtn);
							}
						}
					}else{
						filePath = data.getAction().replace("file://", "");
					}
					if(null != filePath){
						tempFile = new File(filePath);
					}
				}
				
				if(tempFile.exists()){
					Intent intent = new Intent();
					intent.putExtra(Extra.IMAGE_PATH, tempFile.getPath());
					setResult(RESULT_OK, intent);
					finish();
				}
				//  删除toast,添加finish
				//Toast.show(tempFile.getPath());
			} else {
//				chooseLl.setVisibility(View.VISIBLE);
				finish();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "选择照片";
	}
}
