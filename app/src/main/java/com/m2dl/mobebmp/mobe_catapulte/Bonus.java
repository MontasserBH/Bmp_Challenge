package com.m2dl.mobebmp.mobe_catapulte;

import android.graphics.drawable.Drawable;

/**
 * Created by Franck on 16/03/2018.
 */

public class Bonus {
    private int X;
    private int Y;
    private int id;
    int image;
    String type;


    public Bonus(int x, int y, int image, String type) {
        X = x;
        Y = y;
        this.image = image;
        this.type = type;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getId() {
        return getX() + getY();
    }
}
