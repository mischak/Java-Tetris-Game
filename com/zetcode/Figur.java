package com.zetcode;

import java.util.Random;

public class Figur {

    Random zufallszahl = new Random();

    enum TetrisForm {
        Keine,
        Z,
        S,
        Linie,
        T,
        Quadrat,
        L,
        LVerkehrt
    }

    TetrisForm teil;
    int        koordinaten[][];
    int[][][]  koordTabelle;


    Figur() {
        koordinaten = new int[4][2];

        koordTabelle = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };

        setzeForm(TetrisForm.Keine);
    }

    void setzeForm(TetrisForm form) {
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                koordinaten[i][j] = koordTabelle[form.ordinal()][i][j];
            }
        }

        teil = form;
    }

    void setX(int index, int x) {
        koordinaten[index][0] = x;
    }

    void setY(int index, int y) {
        koordinaten[index][1] = y;
    }

    int x(int index) {
        return koordinaten[index][0];
    }

    int y(int index) {
        return koordinaten[index][1];
    }

    void setzeZufaelligeForm() {
        int x = Math.abs(zufallszahl.nextInt()) % 7 + 1;

        setzeForm(TetrisForm.values()[x]);
    }

    int minX() {
        int m = koordinaten[0][0];

        for (int i=0; i < 4; i++) {

            m = Math.min(m, koordinaten[i][0]);
        }

        return m;
    }


    int minY() {
        int m = koordinaten[0][1];

        for (int i=0; i < 4; i++) {

            m = Math.min(m, koordinaten[i][1]);
        }

        return m;
    }

    Figur dreheLinks() {
        if (teil == TetrisForm.Quadrat) {

            return this;
        }

        var result = new Figur();
        result.teil = teil;

        for (int i = 0; i < 4; ++i) {

            result.setX(i, y(i));
            result.setY(i, -x(i));
        }

        return result;
    }

    Figur dreheRechts() {
        if (teil == TetrisForm.Quadrat) {
            return this;
        }

        var resultat = new Figur();
        resultat.teil = teil;

        for (int i = 0; i < 4; ++i) {
            resultat.setX(i, -y(i));
            resultat.setY(i, x(i));
        }

        return resultat;
    }
}

