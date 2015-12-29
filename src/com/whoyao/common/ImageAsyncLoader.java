package com.whoyao.common;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.utils.FileUtils;
import com.whoyao.utils.L;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

/**
 * 图片异步加载抽象类
 * 
 * @author HYH create at：2012-11-23 下午04:02:52
 */
public abstract class ImageAsyncLoader {
	private static final String TAG = "ImageAsyncLoader";
	private static final int ImageTagKey = -1;
	protected HashMap<String, SoftReference<Bitmap>> imageCache;
	private HashMap<String, WeakReference<ImageView>> viewCache;
	private BlockingQueue<Runnable> queue;
	private ThreadPoolExecutor executor;
	private String id = null;

	private Handler handler = new Handler();
	protected String imageDemen = WebApi.ImageDemen_100;
	private int defaultImageID = 0;

	protected void setDefaultImage(int defImageID) {
		defaultImageID = defImageID;
	}

	public ImageAsyncLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
		viewCache = new HashMap<String, WeakReference<ImageView>>();
		// 线程池：最大50条，每次执行：1条，空闲线程结束的超时时间：120秒
		queue = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(1, 50, 180, TimeUnit.SECONDS, queue);
	}

	public void loadBitmap(File imageFile, ImageView iv) {
		if (!imageFile.exists()) {
			return;
		}
		Bitmap bitmap = readImage(imageFile);
		iv.setImageBitmap(bitmap);
	}

	/**
	 * 该方法只对loadBitmapByID有效
	 * 
	 * @return
	 */
	public ImageAsyncLoader setImageDemen(String imageDemen) {
		this.imageDemen = imageDemen;
		return this;
	}

	public void loadBitmapByID(String imageID, ImageView iv) {
		loadBitmap(WebApi.Image_Addr + imageID + imageDemen, iv);
	}

	public void loadBitmap(String imageUrl, ImageView iv) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			Bitmap bitmap = softReference.get();
			if (bitmap != null) {
				iv.setImageBitmap(bitmap);
				return;
			}
		} 
		if (0 != defaultImageID) {
			iv.setImageResource(defaultImageID);
		}

		iv.setTag(ImageTagKey, imageUrl);
		WeakReference<ImageView> reference = new WeakReference<ImageView>(iv);
		viewCache.put(imageUrl, reference);

		// 用线程池来做下载并压缩图片的任务
		executor.execute(new DoLoad(imageUrl));

	}

	public interface ImageCallback {
		public void imageLoaded(Bitmap bitmap, String imageTag);
	}

	/** 加载 */
	protected abstract Bitmap readImage(File imageFile);

	/**
	 * 下载图片并加载
	 * 
	 * @author hyh creat_at：2013-8-1-下午5:09:19
	 */
	protected class DoLoad implements Runnable {

		private String imageUrl;

		public DoLoad(String imageUrl) {
			super();
			this.imageUrl = imageUrl;
		}

		@Override
		public void run() {
			File image = FileUtils.findImage(imageUrl);
			File temp = new File(image.getPath() +".tmp");
			if (!image.exists() && !temp.exists()) {// 下载
				L.i(TAG, "load imageUrl = " + imageUrl);
				FileUtils.downloadFile(imageUrl, image);
				L.i(TAG, "load imagePath = " + image.getPath());
			}

			if (image != null && image.exists()) {
				L.i(TAG, imageUrl + "\n已存在  路径是:\n" + image.getPath());
				Bitmap bitmap = readImage(image);// 实现了大小图片的通用
				imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
//				handler.sendMessage(handler.obtainMessage(0, imageUrl));
				handler.post(new OnPostExecute(imageUrl, bitmap));
			} else {
				// TODO 下载失败
				L.i(TAG, imageUrl+"->->->"+image.getPath() +"下载失败");
			}
		}

	}

	protected class OnPostExecute implements Runnable {

		private String imageUrl;
		private Bitmap bitmap;

		public OnPostExecute(String imageUrl, Bitmap bitmap) {
			super();
			this.imageUrl = imageUrl;
			this.bitmap = bitmap;
		}

		@Override
		public void run() {
			ImageView iv = viewCache.get(imageUrl).get();
			if (null != iv && imageUrl.equals(iv.getTag(ImageTagKey))) {
				if (bitmap != null) {
					setImageBitmap(iv, bitmap);
				}
			}
		}
	}

	protected void setImageBitmap(ImageView iv, Bitmap bitmap) {
		iv.setImageBitmap(bitmap);
	}
}
