package com.kylev1999.qstorage;

import java.io.Serializable;

public class Items implements Serializable {

    public String name;
    public String quantity;
    public int quantityint;

    public Items() {
    }

    public Items(String name) {
        this.name = name;
    }

    public Items(int quantityint) {
        this.quantityint = quantityint;
    }

    public Items(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getQuantityint() {
        return quantityint;
    }

    public void setQuantityint(int quantityint) {
        this.quantityint = quantityint;
    }
}
