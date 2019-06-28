package com.org.kulture.kulture.model;

/**
 * Created by tkru on 10/7/2017.
 */

public class Product_Grid_Model {
    private String cat_id;
    private String cat_name;
    private String cat_slug;
    private String cat_image;
    private String cat_count;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_slug() {
        return cat_slug;
    }

    public void setCat_slug(String cat_slug) {
        this.cat_slug = cat_slug;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }

    public String getCat_count() {
        return cat_count;
    }

    public void setCat_count(String cat_count) {
        this.cat_count = cat_count;
    }
}
