package com.m2dl.mobebmp.mobe_catapulte;

/**
 * Created by Franck on 16/03/2018.
 */

public class Score {
    String name;
    long score;

    public Score()
    {
        name = "";
        score = 0;
    }

    public Score(String name, long score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
