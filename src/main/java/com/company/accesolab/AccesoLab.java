package com.company.accesolab;

import javax.swing.*;

/**
 * AccesoLab: control de acceso a laboratorios con logica proposicional.
 */
public class AccesoLab {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorada) {
            }
            JFrame f = new JFrame("AccesoLab - Control de acceso a laboratorios");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setContentPane(new PanelAcceso());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
