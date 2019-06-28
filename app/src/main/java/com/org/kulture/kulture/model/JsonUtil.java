package com.org.kulture.kulture.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
	
	public static String toJSon(Payment_Json payment) {

		try {
			JSONObject jsonObj = new JSONObject();
			JSONArray jsonArr = new JSONArray();

			for (Payment_Json.PaymentDetails dn : payment.getPayment_details()) {
				JSONObject dnObj = new JSONObject();
				dnObj.put("payment_method", dn.getPayment_method());
				dnObj.put("payment_method_title", dn.getPayment_method_title());
				dnObj.put("set_paid", dn.getSet_paid());
				dnObj.put("customer_id", dn.getCustomer_id());

				jsonArr.put(dnObj);
			}
			jsonObj.put("payment_details", jsonArr);

			JSONArray jsonArr2 = new JSONArray();

			for (Payment_Json.BillingAddress billn : payment.getBillingAddress()) {
				JSONObject dnObj = new JSONObject();
				dnObj.put("first_name", billn .getFirst_name());
				dnObj.put("last_name", billn .getLast_name());
				dnObj.put("address_1", billn .getAddress1());

				dnObj.put("address_2", "");
				dnObj.put("city", billn .getCity());
				dnObj.put("state", billn .getState());

				dnObj.put("postcode", billn .getPostcode());
				dnObj.put("country", billn .getCountry());
				dnObj.put("email", billn .getEmail() );

				dnObj.put("phone", billn .getPhone());

				jsonArr2.put(dnObj);
			}
			jsonObj.put("billing", jsonArr2);

			JSONArray jsonArr3 = new JSONArray();

			for (Payment_Json.ShippingAddress shipn : payment.getShippingAddress()) {
				JSONObject dnObj = new JSONObject();
				dnObj.put("first_name", shipn .getShipping_first_name());
				dnObj.put("last_name", shipn .getShipping_last_name());
				dnObj.put("address_1", shipn .getShipping_address1());

				dnObj.put("address_2", "");
				dnObj.put("city", shipn .getShipping_city());
				dnObj.put("state", shipn .getShipping_state());

				dnObj.put("postcode", shipn .getShipping_postcode());
				dnObj.put("country", shipn .getShipping_country());

				jsonArr3.put(dnObj);
			}
			jsonObj.put("shipping", jsonArr3);
			JSONArray jsonArr4 = new JSONArray();

			for (Payment_Json.line_items itemsn : payment.getItemsList()) {
				JSONObject dnObj = new JSONObject();
				dnObj.put("product_id", itemsn .getProduct_id());
				dnObj.put("quantity", itemsn .getQuantity());
				dnObj.put("variation_id", itemsn .getVariation_id());

				jsonArr4.put(dnObj);
			}
			jsonObj.put("line_items", jsonArr4);

			JSONArray jsonArr5 = new JSONArray();

			for (Payment_Json.shipping_lines shiplines : payment.getShipping_lines()) {
				JSONObject dnObj = new JSONObject();
				dnObj.put("method_id", shiplines .getMethod_id());
				dnObj.put("method_title", shiplines .getMethod_title());
				dnObj.put("total", shiplines .getTotal());

				jsonArr5.put(dnObj);
			}
			jsonObj.put("shipping_lines", jsonArr5);

			return jsonObj.toString();

		}
		catch(JSONException ex) {
			ex.printStackTrace();
		}

		return null;
		
	}

}
