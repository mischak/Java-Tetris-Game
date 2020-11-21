package com.zetcode;

import java.awt.Color;
import java.util.Random;

public class Figur {

    static Random zufallszahl = new Random();

    enum TetrisForm {
        Keine    (new int[][]{ { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 }  }, new Color(0, 0, 0)       ),
        Z        (new int[][]{ { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } }, new Color(204, 102, 102) ),
        S        (new int[][]{ { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 }  }, new Color(102, 204, 102) ),
        Linie    (new int[][]{ { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 }  }, new Color(102, 102, 204) ),
        T        (new int[][]{ { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 }  }, new Color(204, 204, 102) ),
        Quadrat  (new int[][]{ { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 }  }, new Color(204, 102, 204) ),
        L        (new int[][]{ { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 }  }, new Color(102, 204, 204) ),
        LVerkehrt(new int[][]{ { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 }  }, new Color(218, 170, 0)   );

        final int[][] koords;
        final Color   farbe;

        TetrisForm(int[][] koords, Color farbe) {
            this.koords = koords;
            this.farbe = farbe;
        }
    }

    final TetrisForm form;
    final int[][]    koordinaten = new int[4][2];


    Figur() {
        this(TetrisForm.values()[Math.abs(zufallszahl.nextInt()) % 7 + 1]);
    }

    Figur(TetrisForm form) {
        this(form, form.koords);
    }

    Figur(TetrisForm form, int[][] koords) {
        this.form = form;
        for (int i = 0; i < 4 ; i++) {
            koordinaten[i][0] = koords[i][0];
            koordinaten[i][1] = koords[i][1];
        }
    }

    int x(int index) {
        return koordinaten[index][0];
    }

    int y(int index) {
        return koordinaten[index][1];
    }

    int minY() {
        int m = koordinaten[0][1];

        for (int i=0; i < 4; i++) {

            m = Math.min(m, koordinaten[i][1]);
        }

        return m;
    }

    public Figur copy() {
        return new Figur(this.form, this.koordinaten);
    }


    Figur dreheLinks() {
        if (form == TetrisForm.Quadrat) {
            return this;
        }

        for (int i = 0; i < 4; ++i) {
            int yAlt = y(i);
            int xAlt = x(i);
            koordinaten[i][0] = yAlt;
            koordinaten[i][1] = -xAlt;
        }

        return this;
    }

    Figur dreheRechts() {
        if (form == TetrisForm.Quadrat) {
            return this;
        }

        for (int i = 0; i < 4; ++i) {
            int yAlt = y(i);
            int xAlt = x(i);
            koordinaten[i][0] = -yAlt;
            koordinaten[i][1] = xAlt;
        }

        return this;
    }
}

