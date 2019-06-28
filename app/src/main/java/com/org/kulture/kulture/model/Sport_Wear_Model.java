package com.org.kulture.kulture.model;

/**
 * Created by tkru on 10/12/2017.
 */

public class Sport_Wear_Model {
    private Integer sport_id;
    private String sport_name;
    private String sport_image;
    private String sport_image2;
    private String sport_title;

    public String getSport_image2() {
        return sport_image2;
    }

    public void setSport_image2(String sport_image2) {
        this.sport_image2 = sport_image2;
    }

    public Integer getSport_id() {
        return sport_id;
    }

    public void setSport_id(Integer sport_id) {
        this.sport_id = sport_id;
    }

    public String getSport_name() {
        return sport_name;
    }

    public void setSport_name(String sport_name) {
        this.sport_name = sport_name;
    }

    public String getSport_image() {
        return sport_image;
    }

    public void setSport_image(String sport_image) {
        this.sport_image = sport_image;
    }

    public String getSport_title() {
        return sport_title;
    }

    public void setSport_title(String sport_title) {
        this.sport_title = sport_title;
    }
}
