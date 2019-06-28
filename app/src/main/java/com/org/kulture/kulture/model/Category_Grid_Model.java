package com.org.kulture.kulture.model;

/**
 * Created by tkru on 10/9/2017.
 */

public class Category_Grid_Model {
    private Integer cat_sub_id;
    private String cat_sub_name;
    private String cat_sub_image;
    private String cat_sub_price;
    private String cat_sub_desc;

    public String getCat_sub_desc() {
        return cat_sub_desc;
    }

    public void setCat_sub_desc(String cat_sub_desc) {
        this.cat_sub_desc = cat_sub_desc;
    }

    public Integer getCat_sub_id() {
        return cat_sub_id;
    }

    public void setCat_sub_id(Integer cat_sub_id) {
        this.cat_sub_id = cat_sub_id;
    }

    public String getCat_sub_name() {
        return cat_sub_name;
    }

    public void setCat_sub_name(String cat_sub_name) {
        this.cat_sub_name = cat_sub_name;
    }

    public String getCat_sub_image() {
        return cat_sub_image;
    }

    public String getCat_sub_price() {
        return cat_sub_price;
    }

    public void setCat_sub_price(String cat_sub_price) {
        this.cat_sub_price = cat_sub_price;
    }

    public void setCat_sub_image(String cat_sub_image) {
        this.cat_sub_image = cat_sub_image;
    }
}
