package com.monresto.acidlabs.monresto.Model;

import java.io.Serializable;

public class PaymentMode implements Serializable {
    private int id;
    private String title;
    private String image;

    private String paymentModeDesc;


    public PaymentMode(int id, String title, String paymentModeDesc, String image) {
        this.id = id;
        this.title = title;
        this.paymentModeDesc = paymentModeDesc;
        this.image = image;
    }

    public String getPaymentModeDesc() {
        return paymentModeDesc;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}