package com.company.accesolab;

import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Tabla de verdad de Acceso = P ∧ Q ∧ R y comprobacion de De Morgan.
 */
public class PanelTabla extends JPanel {

    public PanelTabla() {
        setLayout(new BorderLayout());
        Expr negado = new Expr.No(Sistema.ACCESO);
        Expr deMorgan = negado.deMorgan();
        String[] cols = {"P", "Q", "R", "Acceso", negado.toString(), deMorgan.toString()};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        List<String> vars = Sistema.VARIABLES;
        for (boolean[] f : TablaVerdad.combinaciones(vars.size())) {
            Map<String, Boolean> v = TablaVerdad.asignar(vars, f);
            m.addRow(new Object[]{vf(f[0]), vf(f[1]), vf(f[2]), vf(Sistema.ACCESO.eval(v)),
                vf(negado.eval(v)), vf(deMorgan.eval(v))});
        }
        JTable t = new JTable(m);
        t.setRowHeight(22);
        t.setEnabled(false);
        centrar(t);
        JLabel pie = new JLabel("Las dos últimas columnas coinciden: De Morgan "
                + (TablaVerdad.equivalentes(negado, deMorgan, vars) ? "verificado." : "ERROR."));
        pie.setBorder(BorderFactory.createEmptyBorder(8, 2, 0, 2));
        add(t.getTableHeader(), BorderLayout.NORTH);
        add(t, BorderLayout.CENTER);
        add(pie, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(640, 230));
    }

    private static String vf(boolean b) {
        return b ? "V" : "F";
    }

    private static void centrar(JTable t) {
        var r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(r);
        }
    }
}
