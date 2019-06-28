package com.org.kulture.kulture.model;

import java.util.List;

/**
 * Created by tkru on 11/22/2017.
 */

public class Payment_Json {

    private List<PaymentDetails> payment_details;
    private List<BillingAddress> billingAddress;
    private List<ShippingAddress> shippingAddress;
    private List<line_items> itemsList;
    private List<shipping_lines> shipping_lines;

    public class PaymentDetails{
        private String payment_method;
        private String payment_method_title;
        private Boolean set_paid;
        private Integer customer_id;

        public Integer getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(Integer customer_id) {
            this.customer_id = customer_id;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getPayment_method_title() {
            return payment_method_title;
        }

        public void setPayment_method_title(String payment_method_title) {
            this.payment_method_title = payment_method_title;
        }

        public Boolean getSet_paid() {
            return set_paid;
        }

        public void setSet_paid(Boolean set_paid) {
            this.set_paid = set_paid;
        }
    }

    public class BillingAddress {
        private String first_name;
        private String last_name;
        private String address1;
        private String postcode;
        private String country;
        private String email;
        private String phone;
        private String city;
        private String state;

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
    public class ShippingAddress {
        private String shipping_first_name;
        private String shipping_last_name;
        private String shipping_address1;
        private String shipping_postcode;
        private String shipping_country;
        private String shipping_city;
        private String shipping_state;

        public String getShipping_first_name() {
            return shipping_first_name;
        }

        public void setShipping_first_name(String shipping_first_name) {
            this.shipping_first_name = shipping_first_name;
        }

        public String getShipping_last_name() {
            return shipping_last_name;
        }

        public void setShipping_last_name(String shipping_last_name) {
            this.shipping_last_name = shipping_last_name;
        }

        public String getShipping_address1() {
            return shipping_address1;
        }

        public void setShipping_address1(String shipping_address1) {
            this.shipping_address1 = shipping_address1;
        }

        public String getShipping_postcode() {
            return shipping_postcode;
        }

        public void setShipping_postcode(String shipping_postcode) {
            this.shipping_postcode = shipping_postcode;
        }

        public String getShipping_country() {
            return shipping_country;
        }

        public void setShipping_country(String shipping_country) {
            this.shipping_country = shipping_country;
        }

        public String getShipping_city() {
            return shipping_city;
        }

        public void setShipping_city(String shipping_city) {
            this.shipping_city = shipping_city;
        }

        public String getShipping_state() {
            return shipping_state;
        }

        public void setShipping_state(String shipping_state) {
            this.shipping_state = shipping_state;
        }
    }
    public class line_items {
        private Integer product_id;
        private Integer quantity;
        private String variation_id;

        public Integer getProduct_id() {
            return product_id;
        }

        public void setProduct_id(Integer product_id) {
            this.product_id = product_id;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getVariation_id() {
            return variation_id;
        }

        public void setVariation_id(String variation_id) {
            this.variation_id = variation_id;
        }
    }

    public class shipping_lines{
        private String method_id;
        private String method_title;
        private Double total;

        public String getMethod_id() {
            return method_id;
        }

        public void setMethod_id(String method_id) {
            this.method_id = method_id;
        }

        public String getMethod_title() {
            return method_title;
        }

        public void setMethod_title(String method_title) {
            this.method_title = method_title;
        }

        public Double getTotal() {
            return total;
        }

        public void setTotal(Double total) {
            this.total = total;
        }
    }

    public List<PaymentDetails> getPayment_details() {
        return payment_details;
    }

    public void setData(List<PaymentDetails> PaymentDetails) {
        this.payment_details = PaymentDetails;
    }

    public List<BillingAddress> getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(List<BillingAddress> billingAddress) {
        this.billingAddress = billingAddress;
    }

    public List<ShippingAddress> getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(List<ShippingAddress> shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<line_items> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<line_items> itemsList) {
        this.itemsList = itemsList;
    }

    public List<Payment_Json.shipping_lines> getShipping_lines() {
        return shipping_lines;
    }

    public void setShipping_lines(List<Payment_Json.shipping_lines> shipping_lines) {
        this.shipping_lines = shipping_lines;
    }
}
