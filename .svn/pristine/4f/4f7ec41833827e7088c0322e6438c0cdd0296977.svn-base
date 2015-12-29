package com.whoyao.venue.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.text.TextUtils;

import com.loopj.android.http.RequestParams;
import com.whoyao.Const;
import com.whoyao.WebApi;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.L;
import com.whoyao.venue.model.CartRModel;
import com.whoyao.venue.model.CartSubItemStateModel;
import com.whoyao.venue.model.CartSubItemModel;
import com.whoyao.venue.model.VenueBaseInfoModel;
import com.whoyao.venue.model.VenueCartModel;

/**
 * @author hyh 
 * creat_at：2014-2-26-下午1:32:32
 */
public class CartEngine {
	private static TreeMap<Integer, VenueBaseInfoModel> mVenueMap;
	private static TreeMap<Integer, VenueCartModel> mVenueCartMap;
	private static TreeMap<Integer, CartSubItemStateModel> mCartErrMap;
	
	public static void initVenueCart(CartRModel cartData){
		if(mVenueMap == null){
			mVenueMap = new TreeMap<Integer, VenueBaseInfoModel>();
		}else{
			mVenueMap.clear();
		}
		if(mVenueCartMap == null){
			mVenueCartMap = new TreeMap<Integer, VenueCartModel>();
		}else{
			mVenueCartMap.clear();
		}
		if(mCartErrMap == null){
			mCartErrMap = new TreeMap<Integer, CartSubItemStateModel>();
		}else{
			mCartErrMap.clear();
		}

		VenueBaseInfoModel[] venueList = cartData.getVenueList();
		int length = venueList.length;
		for(int i = 0; i<length; i++){
			VenueBaseInfoModel venue = venueList[i];
			System.out.println("id: "+venue.getVenueid()+"name: "+venue);
			mVenueMap.put(venue.getVenueid(), venue);
		}
		ArrayList<VenueCartModel> venueCarts = cartData.getVenueCartList();
		length = venueCarts.size();
		for(int i = 0; i<length; i++){
			VenueCartModel venuecart = venueCarts.get(i);
			mVenueCartMap.put(venuecart.getCartid(), venuecart);
		}
		CartSubItemStateModel[] cartErrors = cartData.getCheckError();
		if(null != cartErrors){
			length = cartErrors.length;
			for(int i = 0; i<length; i++){
				CartSubItemStateModel error = cartErrors[i];
				mCartErrMap.put(error.getId(), error);
			}
		}
		CartSubItemModel[] subItemList = cartData.getCartDetailList();
		length = subItemList.length;
		for(int i = 0; i<length; i++){
			CartSubItemModel subItem = subItemList[i];
			VenueCartModel cartModel = mVenueCartMap.get(subItem.getCartid());
			System.out.println(cartModel.getVenueid());
			if (mVenueMap.containsKey(cartModel.getVenueid())) {
				cartModel.setVenueName(mVenueMap.get(cartModel.getVenueid()).getFullname());
			}else {
				cartModel.setVenueName("");
			}
			CartSubItemStateModel state = mCartErrMap.get(subItem.getCartdetailid());
			if(null != state){
				subItem.setState(state.getState());
			}
			cartModel.getSubList().add(subItem);
		}
		mVenueCartMap.clear();
		mCartErrMap.clear();
		mVenueMap.clear();
		return;
	}
	
	public static int  countTotalAmount(CartRModel cartData){
		int totalAmount = 0;
		ArrayList<VenueCartModel> cartList = cartData.getVenueCartList();
		int length = cartList.size();
		for(int i = 0; i < length; i++){
			List<CartSubItemModel> subList = cartList.get(i).getSubList();
			int size = subList.size();
			for(int j = 0; j < size; j++){
				CartSubItemModel subItem = subList.get(j);
				if(subItem.getState() == 0){
					totalAmount += subItem.getAmount();
				}
			}
		}
		cartData.setTotalAmount(totalAmount);
		return totalAmount;
	}
	public static int  countTotalItem(CartRModel cartData){
		int totalItem = 0;
		ArrayList<VenueCartModel> cartList = cartData.getVenueCartList();
		int length = cartList.size();
		for(int i = 0; i < length; i++){
			List<CartSubItemModel> subList = cartList.get(i).getSubList();
			int size = subList.size();
			for(int j = 0; j < size; j++){
				CartSubItemModel subItem = subList.get(j);
				totalItem += subItem.getReservecount();
			}
		}
		cartData.setTotalItem(totalItem);
		return totalItem;
	}
	
	/**
	 * @param cartData
	 * @return
	 */
	public static int  countValidItem(CartRModel cartData){
		int validItem = 0;
		ArrayList<VenueCartModel> cartList = cartData.getVenueCartList();
		int length = cartList.size();
		for(int i = 0; i < length; i++){
			List<CartSubItemModel> subList = cartList.get(i).getSubList();
			int size = subList.size();
			for(int j = 0; j < size; j++){
				CartSubItemModel subItem = subList.get(j);
				if(subItem.getState() == 0){
					validItem += subItem.getReservecount();
				}
			}
		}
		cartData.setValidItem(validItem);
		return validItem;
	}
	
	
	public static String getErrorState(int state){
		switch (state) {
		case 1:
			return "已过期";
		case 2:
			return "已定完";
		case 3:
			return "剩余不足";
		case 4:
			return "仅剩1块";
		case 5:
			return "已失效";
		default:
			return "";
		}
	}
	public static String getCartSubItemIDs(CartRModel model){
		String ids = "";
		ArrayList<VenueCartModel> list = model.getVenueCartList();
		int size = list.size();
		for(int i = 0;i < size ; i++){
			VenueCartModel cartModel = list.get(i);
			List<CartSubItemModel> subList = cartModel.getSubList();
			if(subList != null && !subList.isEmpty()){
				int size2 = subList.size();
				for(int j = 0; j< size2;j++){
					int cartdetailid = subList.get(j).getCartdetailid();
					ids += cartdetailid +",";
				}
			}
		}
		return ids.substring(0, ids.length()-1);
	}
}
