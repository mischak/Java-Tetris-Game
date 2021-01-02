package com.zetcode;

import com.zetcode.Figur.TetrisForm;

import javax.swing.*;
import java.awt.Graphics;

/**
 * @author koe
 */
public class Feld extends JPanel {

    int   quadratBreite = 20;
    int   quadratHoehe  = 20;
    int   paddingOben   = 30;
    Figur aktuellesTeil;


    protected void zeichneTeil(Graphics g, int offsetX, int offsetY) {
        if (aktuellesTeil == null || aktuellesTeil.form == TetrisForm.Keine) {
            return;
        }

        for (int i = 0; i < 4; i++) {
            zeichneQuadrat(g, (offsetX + aktuellesTeil.x(i)) * quadratBreite,
                    paddingOben + (offsetY + aktuellesTeil.y(i)) * quadratHoehe,
                    aktuellesTeil.form);
        }
    }

    void zeichneQuadrat(Graphics g, int x, int y, TetrisForm form) {
        var color = form.farbe;

        g.setColor(color);
        g.fillRect(x + 1, y + 1, quadratBreite - 2, quadratHoehe - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + quadratHoehe - 1, x, y);
        g.drawLine(x, y, x + quadratBreite - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + quadratHoehe - 1,
                x + quadratBreite - 1, y + quadratHoehe - 1);
        g.drawLine(x + quadratBreite - 1, y + quadratHoehe - 1,
                x + quadratBreite - 1, y + 1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        zeichne(g);
    }

    void zeichne(Graphics g) {
        zeichneTeil(g, 2, 0);
    }
}
