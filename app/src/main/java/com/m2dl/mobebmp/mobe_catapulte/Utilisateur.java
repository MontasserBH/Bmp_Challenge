package com.m2dl.mobebmp.mobe_catapulte;

/**
 * Created by MONTASSER on 16/03/2018.
 */

public class Utilisateur {

    private String nom;
    private int score;

    public Utilisateur(String nom, int score) {
        this.nom = nom;
        this.score = score;
    }

    public Utilisateur() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
