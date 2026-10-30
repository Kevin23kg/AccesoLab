package com.company.accesolab;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Panel principal: el usuario ingresa la solicitud y el sistema decide al
 * instante.
 */
public class PanelAcceso extends JPanel {

    private static final Color VERDE = new Color(46, 125, 50), ROJO = new Color(198, 40, 40);

    private final Sistema sistema = new Sistema();
    private final JTextField usuario = new JTextField("U001", 8);
    private final JComboBox<String> lab = new JComboBox<>(Sistema.LABORATORIOS.toArray(new String[0]));
    private final JSpinner hora = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
    private final JLabel tarjeta = new JLabel("", SwingConstants.CENTER);
    private final JTextArea motivo = new JTextArea(2, 40);
    private final JTextArea detalle = new JTextArea(8, 40);

    public PanelAcceso() {
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(12, 14, 12, 14));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        form.setBorder(BorderFactory.createTitledBorder("Solicitud de acceso"));
        form.add(new JLabel("Usuario:"));
        form.add(usuario);
        form.add(new JLabel("Laboratorio:"));
        form.add(lab);
        form.add(new JLabel("Hora (0-23):"));
        form.add(hora);

        tarjeta.setOpaque(true);
        tarjeta.setForeground(Color.WHITE);
        tarjeta.setFont(new Font("SansSerif", Font.BOLD, 30));
        tarjeta.setPreferredSize(new Dimension(100, 70));
        motivo.setFont(new Font("SansSerif", Font.BOLD, 13));
        motivo.setEditable(false);
        motivo.setOpaque(false);
        motivo.setLineWrap(true);
        motivo.setWrapStyleWord(true);

        detalle.setEditable(false);
        detalle.setFont(new Font("Monospaced", Font.PLAIN, 13));
        detalle.setBorder(new EmptyBorder(4, 6, 4, 6));

        JButton verTabla = new JButton("Ver tabla de verdad");
        verTabla.addActionListener(e -> JOptionPane.showMessageDialog(this, new PanelTabla(),
                "Tabla de verdad del acceso", JOptionPane.PLAIN_MESSAGE));

        JPanel centro = new JPanel(new BorderLayout(0, 6));
        centro.add(tarjeta, BorderLayout.NORTH);
        centro.add(motivo, BorderLayout.CENTER);
        JPanel sur = new JPanel(new BorderLayout(0, 8));
        sur.add(new JScrollPane(detalle), BorderLayout.CENTER);
        sur.add(verTabla, BorderLayout.SOUTH);

        JPanel medio = new JPanel(new BorderLayout(0, 8));
        medio.add(centro, BorderLayout.NORTH);
        medio.add(sur, BorderLayout.CENTER);
        add(form, BorderLayout.NORTH);
        add(medio, BorderLayout.CENTER);

        usuario.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                actualizar();
            }

            public void removeUpdate(DocumentEvent e) {
                actualizar();
            }

            public void changedUpdate(DocumentEvent e) {
                actualizar();
            }
        });
        lab.addActionListener(e -> actualizar());
        hora.addChangeListener(e -> actualizar());
        actualizar();
        setPreferredSize(new Dimension(540, 420));
    }

    /**
     * Usado para fijar una solicitud desde el codigo (pruebas y capturas).
     */
    public void fijar(String u, String l, int h) {
        usuario.setText(u);
        lab.setSelectedItem(l);
        hora.setValue(h);
        actualizar();
    }

    private void actualizar() {
        Sistema.Resultado r = sistema.evaluar(usuario.getText(), (String) lab.getSelectedItem(), (Integer) hora.getValue());
        tarjeta.setText(r.concedido() ? "ACCESO CONCEDIDO" : "ACCESO DENEGADO");
        tarjeta.setBackground(r.concedido() ? VERDE : ROJO);
        motivo.setText(r.concedido() ? "Se cumplen P, Q y R."
                : "Motivo: " + String.join(" o ", r.motivos()) + ".");

        StringBuilder sb = new StringBuilder("Proposiciones:\n");
        String[] nombres = {"usuario registrado", "tiene reserva", "horario válido"};
        int i = 0;
        for (var e : r.proposiciones().entrySet()) {
            sb.append("  ").append(e.getKey()).append(" (").append(nombres[i++]).append(") = ")
                    .append(e.getValue() ? "V" : "F").append("\n");
        }
        sb.append("Razonamiento:\n");
        for (String l : r.razonamiento()) {
            sb.append("  ").append(l).append("\n");
        }
        detalle.setText(sb.toString());
        detalle.setCaretPosition(0);
    }
}
