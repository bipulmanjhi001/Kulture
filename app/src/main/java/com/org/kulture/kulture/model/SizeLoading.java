package com.org.kulture.kulture.model;

/**
 * Created by tkru on 10/13/2017.
 */

public class SizeLoading {
    private String pro_size;

    public SizeLoading() {
    }
    public String getPro_size() {
        return pro_size;
    }

    public void setPro_size(String pro_size) {
        this.pro_size = pro_size;
    }
    /**
     * Pay attention here, you have to override the toString method as the
     * ArrayAdapter will reads the toString of the given object for the name
     *
     * @return contact_name
     */
    @Override
    public String toString() {
        return pro_size;
    }
}
