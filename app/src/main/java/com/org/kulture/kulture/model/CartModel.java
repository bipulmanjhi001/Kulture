package com.org.kulture.kulture.model;

import java.io.Serializable;

/**
 * Created by tkru on 10/26/2017.
 */

public class CartModel implements Serializable {
    private String cart_qty;
    private String cart_name;
    private String cart_total_price;
    private String cart_image;
    private String cart_size;
    private String product_id;

    public CartModel() {

    }
    public String getCart_qty() {
        return cart_qty;
    }

    public String getCart_size() {
        return cart_size;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setCart_size(String cart_size) {
        this.cart_size = cart_size;
    }

    public void setCart_qty(String cart_qty) {
        this.cart_qty = cart_qty;
    }

    public String getCart_name() {
        return cart_name;
    }

    public void setCart_name(String cart_name) {
        this.cart_name = cart_name;
    }

    public String getCart_total_price() {
        return cart_total_price;
    }

    public void setCart_total_price(String cart_total_price) {
        this.cart_total_price = cart_total_price;
    }

    public String getCart_image() {
        return cart_image;
    }

    public void setCart_image(String cart_image) {
        this.cart_image = cart_image;
    }
}