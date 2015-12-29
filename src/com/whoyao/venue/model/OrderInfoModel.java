package com.whoyao.venue.model;

import java.net.URLDecoder;
import java.net.URLEncoder;

import com.whoyao.Const;

/**
 * @author hyh 
 * creat_at：2014-2-28-下午5:10:12
 */
public class OrderInfoModel {
	/**支付宝身份ID*/
	private String partner = Const.ALIPAY_PARTNER;
	private String service = "mobile.securitypay.pay";
	private String _input_charset = "UTF-8";
	private String return_url = URLEncoder.encode("http://m.alipay.com");
	/**商户网站唯一订单号*/
	private String out_trade_no;
	/**商品名称*/
	private String subject = Const.ALIPAY_SUBJECT;
	/**支付类型*/
	private String payment_type = "1";
	/**卖家支付宝账号*/
	private String seller_id = Const.ALIPAY_SELLER;
	/**总金额*/
	private String total_fee;
	/**商品详情*/
	private String body;
	/**未付款交易的超时时间*/
	private String it_b_pay;
	/**商品展示地址*/
	private String show_url;
	/**异步回调地址*/
	private String notify_url;
	/**授权令牌*/
	private String extern_token;
	
	/**商户网站唯一订单号<br/><font color=red size=5>不可空</font>*/
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	/**商品名称<br/><font color=red size=5>不可空,已有默认值</font>*/
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**支付类型<br/><font color=red size=5>不可空,已有默认值</font>*/
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	/**卖家支付宝账号<br/><font color=red size=5>不可空,已有默认值</font>*/
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	/**总金额<br/><font color=red size=5>不可空</font>*/
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	/**商品详情<br/><font color=red size=5>不可空</font>*/
	public void setBody(String body) {
		this.body = body;
	}
	/**未付款交易的超时时间<br/><font color=green size=5>可空</font>*/
	public void setIt_b_pay(String it_b_pay) {
		this.it_b_pay = it_b_pay;
	}
	/**商品展示地址<br/><font color=green size=5>可空</font>*/
	public void setShow_url(String show_url) {
		this.show_url = show_url;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public String getSubject() {
		return subject;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public String getBody() {
		return body;
	}
	public String getIt_b_pay() {
		return it_b_pay;
	}
	public String getShow_url() {
		return show_url;
	}
	public String getExtern_token() {
		return extern_token;
	}
	/**授权令牌<br/><font color=green size=5>可空</font>*/
	public void setExtern_token(String extern_token) {
		this.extern_token = extern_token;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getDecodeNotify_url() {
		return URLDecoder.decode(notify_url);
	}
	public void setEncodeNotify_url(String notify_url) {
		this.notify_url = URLEncoder.encode(notify_url);
	}
}
