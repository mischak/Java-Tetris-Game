package com.zetcode;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
Java Tetris game clone

Author: Jan Bodnar
Website: http://zetcode.com
 */
public class Tetris extends JFrame {

    JLabel statuszeile;

    Tetris() {
        initUI();
    }

    void initUI() {
        statuszeile = new JLabel(" 0");
        add(statuszeile, BorderLayout.SOUTH);

        var board = new Brett(this);
        add(board);
        board.start();

        setTitle("Tetris");
        setSize(400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new Tetris().setVisible(true);
        });
    }
}
