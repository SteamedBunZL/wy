package com.whoyao.venue.engine;

import java.net.URLEncoder;
import android.app.Activity;
import android.os.Handler;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppException;
import com.whoyao.Const;
import com.whoyao.net.Net;
import com.whoyao.utils.L;
import com.whoyao.venue.engine.alipay.Rsa;
import com.whoyao.venue.model.OrderInfoModel;

/**
 * @author hyh creat_at：2014-2-28-下午4:54:19
 */
public class PayEngine {

	private static String getOrderInfo(OrderInfoModel model) {

		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Const.ALIPAY_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(model.getOut_trade_no());
		sb.append("\"&subject=\"");
		sb.append(model.getSubject());
		sb.append("\"&body=\"");
		sb.append(model.getBody());
		sb.append("\"&total_fee=\"");
		sb.append(model.getTotal_fee());
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(model.getNotify_url());// TODO 缺少
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(model.getSeller_id());

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"");
		sb.append(model.getIt_b_pay());
		sb.append("\"");

		// start the pay.
		return new String(sb);
	}

	private static String getOrderInfoWithoutNull(OrderInfoModel model) {
		RequestParams params = Net.getRequestParamsWithoutNull(model);
		String info = params.toString();
		info = info.replace("=", "=\"").replace("&", "\"&") + "\"";
		return info;
	}

	private static String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private static String signInfo2(String orderInfo) {
		L.i(Const.ZL, "orederinfo---" + orderInfo);
		String sign = Rsa.sign(orderInfo, Const.ALIPAY_PUBLIC);
		L.i(Const.ZL, "info2-----" + sign);
		sign = URLEncoder.encode(sign);
		orderInfo += "&sign=\"" + sign + "\"&" + getSignType();
		return orderInfo;
	}

	private static String signInfo(String orderInfo, String sign) {
		// String sign = Rsa.sign(orderInfo, Const.ALIPAY_PRIVATE);
		// String newSign = URLEncoder.encode(sign);
		orderInfo += "&sign=\"" + sign + "\"&" + getSignType();
		return orderInfo;
	}

	public static void startAliPayClient(Activity context, String model,
			String sign, Handler handler) {

		try {
			// String info = getOrderInfo(model);
			String info = model;
			L.i(Const.ZL, "model-" + info);
//			String info2 = signInfo2(info);
			info = signInfo(info, sign);
			L.i(Const.ZL, "sign1---" + sign);
			L.i(Const.ZL, "info--" + info);
			new AlipayThread(context, info, handler).start();
		} catch (Exception ex) {
			AppException.handle(ex);
		}

	}

	public static void startAliPayClient(Activity context,
			OrderInfoModel model,Handler handler) {

		try {
			// String info = getOrderInfo(model);
			String info = getOrderInfoWithoutNull(model);
			L.i(Const.ZL, "model-" + info);
			String info2 = signInfo2(info);
//			info = signInfo(info, sign);
			L.i(Const.ZL, "info--" + info);
			new AlipayThread(context, info2, handler).start();
		} catch (Exception ex) {
			AppException.handle(ex);
		}

	}

	public static void startAliPayClient2(Activity context, String result,
			Handler handler) {

		try {
			// String info = getOrderInfo(model);
			// String info = getOrderInfoWithoutNull(model);
			// info = signInfo(info);
			L.i(Const.ZL, "info--" + result);
			new AlipayThread(context, result, handler).start();
		} catch (Exception ex) {
			AppException.handle(ex);
		}

	}

}
