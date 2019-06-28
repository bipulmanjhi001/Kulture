package com.org.kulture.kulture.database;

/**
 * Created by tkru on 8/18/2017.
 */

public class CartGetData {
    //private variables
    private int _qty;
    private String _name;
    private Double _total_price;
    private String _image;

    public CartGetData(int _qty, String _name, Double total_price, String _image) {
        this._qty = _qty;
        this._total_price = total_price;
        this._name = _name;
        this._image=_image;
    }

    public CartGetData() {
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public int get_qty() {
        return _qty;
    }

    public void set_qty(int _qty) {
        this._qty = _qty;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public Double get_total_price() {
        return _total_price;
    }

    public void set_total_price(Double _total_price) {
        this._total_price = _total_price;
    }
}

