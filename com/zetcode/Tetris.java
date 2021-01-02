package com.zetcode;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

/*
Java Tetris game clone

Author: Jan Bodnar
Website: http://zetcode.com
 */
public class Tetris extends JFrame {

    JLabel statuszeile;
    Feld   naechstes;
    Feld   halten;

    Tetris() {
        initUI();
    }

    void initUI() {
        statuszeile = new JLabel(" 0");
        statuszeile.setOpaque(true);
        statuszeile.setBackground(new Color(220, 240, 220));
        add(statuszeile, BorderLayout.SOUTH);

        naechstes = new Feld();
        naechstes.setPreferredSize(new Dimension(100, 1));
        naechstes.setBackground(new Color(240, 220, 220));
        add(naechstes, BorderLayout.EAST);

        halten = new Feld();
        halten.setPreferredSize(new Dimension(100, 1));
        halten.setBackground(new Color(220, 220, 240));
        add(halten, BorderLayout.WEST);

        var board = new Brett(statuszeile, halten, naechstes);
        add(board);
        board.start();

        setTitle("Tetris");
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new Tetris().setVisible(true);
        });
    }
}
