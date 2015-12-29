package com.whoyao.engine;

/**
 * 业务逻辑
 * @author HYH create at：2013-5-6 下午01:00:39
 */
public abstract class BasicEngine {
	

	/**
	 * 业务层回调
	 * @author hyh
	 * @param <T>
	 *
	 */
	public static class CallBack<T>{
		protected Object[] params;
		// TODO 这个改成property
		public CallBack() {
			super();
		}
		public CallBack(Object... params ) {
			super();
			this.params = params;
		}
		
		/** 仅成功时回调*/
		public void onCallBack(){
		}
		/** 仅成功时回调*/
		public  void  onCallBack(T data){
		}
		/** 失败时也回调*/
		public  void  onCallBack(boolean isSuccess){
		}
		/** 失败时也回调*/
		public void  onCallBack(boolean isSuccess,T data){
			onCallBack(isSuccess);
			if(isSuccess){
				onCallBack(data);
				onCallBack();
			}
		}
	}
	
}
