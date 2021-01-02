package com.zetcode;

import com.zetcode.Figur.TetrisForm;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class Brett extends Feld {

    static final int BRETT_BREITE = 10;
    static final int BRETT_HOEHE  = 22;
    static final int INTERVALL    = 300;

    Timer          timer;
    boolean        untenAngekommen    = false;
    boolean        istPausiert        = false;
    int            anzEntfernteZeilen = 0;
    int            aktuellesX         = 0;
    int            aktuellesY         = 0;

    final Feld           halten;
    final Feld           naechstes;
    final JLabel         statuszeile;
    final TetrisForm[][] brett = new TetrisForm[BRETT_HOEHE][BRETT_BREITE];

    public Brett(JLabel statuszeile, Feld halten, Feld naechstes) {
        this.halten = halten;
        this.naechstes = naechstes;
        this.statuszeile = statuszeile;

        setFocusable(true);

        addKeyListener(new TAdapter());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                quadratBreite = (int) getSize().getWidth() / BRETT_BREITE;
                quadratHoehe  = (int) getSize().getHeight() / BRETT_HOEHE;
                paddingOben = (int) getSize().getHeight() - BRETT_HOEHE * quadratHoehe;

            }
        });
    }

    TetrisForm figurBei(int x, int y) {
        return brett[y][x];
    }

    void start() {
        aktuellesTeil = new Figur();
        quadratBreite = (int) getSize().getWidth() / BRETT_BREITE;
        quadratHoehe  = (int) getSize().getHeight() / BRETT_HOEHE;
        paddingOben   = (int) getSize().getHeight() - BRETT_HOEHE * quadratHoehe;

        feldLoeschen(brett);
        neuesTeil();

        timer = new Timer(INTERVALL, e -> spielZyklus());
        timer.start();
    }

    private void pause() {
        istPausiert = !istPausiert;

        if (istPausiert) {
            statuszeile.setText("Pause!");
        } else {
            statuszeile.setText(String.valueOf(anzEntfernteZeilen));
        }

        repaint();
    }

    void zeichne(Graphics g) {
        for (int i = 0; i < BRETT_HOEHE; i++) {
            for (int j = 0; j < BRETT_BREITE; j++) {
                TetrisForm form = figurBei(j, BRETT_HOEHE - i - 1);

                if (form != TetrisForm.Keine) {
                    zeichneQuadrat(g, j * quadratBreite,
                            paddingOben + i * quadratHoehe, form);
                }
            }
        }

        zeichneTeil(g, this.aktuellesX, BRETT_HOEHE - this.aktuellesY - 1);
    }

    void fallenlassen() {
        int newY = aktuellesY;

        while (newY > 0) {
            if (!versucheBewegung(aktuellesTeil, aktuellesX, newY - 1)) {
                break;
            }

            newY--;
        }

        teilIstUnten();
    }

    void eineZeileRunter() {
        if (!versucheBewegung(aktuellesTeil, aktuellesX, aktuellesY - 1)) {
            teilIstUnten();
        }
    }

    void feldLoeschen(TetrisForm[][] feld) {
        for (TetrisForm[] tetrisForms : feld) {
            Arrays.fill(tetrisForms, TetrisForm.Keine);
        }
    }

    void teilIstUnten() {
        for (int i = 0; i < 4; i++) {
            int x = aktuellesX + aktuellesTeil.x(i);
            int y = aktuellesY - aktuellesTeil.y(i);
            brett[y][x] = aktuellesTeil.form;
        }

        volleZeilenLoeschen();

        if (!untenAngekommen) {
            neuesTeil();
        }
    }

    void neuesTeil() {
        aktuellesTeil = new Figur();
        aktuellesX = BRETT_BREITE / 2;
        aktuellesY = BRETT_HOEHE - 1 + aktuellesTeil.minY();

        if (!versucheBewegung(aktuellesTeil, aktuellesX, aktuellesY)) {
            aktuellesTeil = new Figur(TetrisForm.Keine);
            timer.stop();

            statuszeile.setText(String.format("Game over. Score: %d", anzEntfernteZeilen));
        }
    }

    boolean versucheBewegung(Figur neueFigur, int neuesX, int neuesY) {
        for (int i = 0; i < 4; i++) {
            int x = neuesX + neueFigur.x(i);
            int y = neuesY - neueFigur.y(i);

            if (x < 0 || x >= BRETT_BREITE || y < 0 || y >= BRETT_HOEHE) {
                return false;
            }

            if (figurBei(x, y) != TetrisForm.Keine) {
                return false;
            }
        }

        aktuellesTeil = neueFigur;
        aktuellesX = neuesX;
        aktuellesY = neuesY;

        repaint();

        return true;
    }

    void volleZeilenLoeschen() {
        int anzVolleZeilen = 0;

        for (int i = BRETT_HOEHE - 1; i >= 0; i--) {
            boolean zeileIstVoll = true;

            for (int j = 0; j < BRETT_BREITE; j++) {
                if (figurBei(j, i) == TetrisForm.Keine) {
                    zeileIstVoll = false;
                    break;
                }
            }

            if (zeileIstVoll) {
                anzVolleZeilen++;

                for (int y = i; y < BRETT_HOEHE - 1; y++) {
                    for (int x = 0; x < BRETT_BREITE; x++) {
                        brett[y][x] = figurBei(x, y + 1);
                    }
                }
            }
        }

        if (anzVolleZeilen > 0) {
            anzEntfernteZeilen += anzVolleZeilen;

            statuszeile.setText(String.valueOf(anzEntfernteZeilen));
            untenAngekommen = true;
        }
    }


    void spielZyklus() {
        aktualisieren();
        repaint();
    }

    void aktualisieren() {
        if (istPausiert) {
            return;
        }

        if (untenAngekommen) {
            untenAngekommen = false;
            neuesTeil();
        } else {
            eineZeileRunter();
        }
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (aktuellesTeil.form == TetrisForm.Keine) {
                return;
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_P     -> pause();
                case KeyEvent.VK_LEFT  -> versucheBewegung(aktuellesTeil, aktuellesX - 1, aktuellesY);
                case KeyEvent.VK_RIGHT -> versucheBewegung(aktuellesTeil, aktuellesX + 1, aktuellesY);
                case KeyEvent.VK_SPACE -> versucheBewegung(aktuellesTeil.copy().dreheRechts(), aktuellesX, aktuellesY);
                case KeyEvent.VK_UP    -> versucheBewegung(aktuellesTeil.copy().dreheLinks(), aktuellesX, aktuellesY);
                case KeyEvent.VK_DOWN  -> fallenlassen();
                case KeyEvent.VK_D     -> eineZeileRunter();
            }
        }
    }
}
