package com.company.accesolab;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Motor de decision de AccesoLab. Acceso = P y Q y R.
 */
public class Sistema {

    public static final Map<String, String> USUARIOS = Map.of("U001", "Ana", "U002", "Luis", "U003", "Marta");
    public static final List<String> LABORATORIOS = List.of("LAB1", "LAB2");
    public static final int HORA_MIN = 8, HORA_MAX = 17;
    /**
     * Reservas con el formato usuario|laboratorio|hora.
     */
    private static final Set<String> RESERVAS = Set.of(
            "U001|LAB1|9", "U001|LAB1|10", "U002|LAB1|11", "U003|LAB2|14");

    public static final Expr P = new Expr.Atomo("P");  // usuario registrado
    public static final Expr Q = new Expr.Atomo("Q");  // tiene reserva
    public static final Expr R = new Expr.Atomo("R");  // horario valido
    public static final Expr ACCESO = new Expr.Y(new Expr.Y(P, Q), R);
    public static final List<String> VARIABLES = List.of("P", "Q", "R");

    private static final Map<String, String> MOTIVO = Map.of(
            "P", "usuario no registrado",
            "Q", "sin reserva para ese laboratorio y hora",
            "R", "hora fuera del horario permitido (08 a 17)");

    public record Resultado(boolean concedido, Map<String, Boolean> proposiciones,
            List<String> motivos, List<String> razonamiento) {

    }

    public Resultado evaluar(String usuario, String lab, int hora) {
        usuario = usuario.trim().toUpperCase();
        Map<String, Boolean> v = new LinkedHashMap<>();
        v.put("P", USUARIOS.containsKey(usuario));
        v.put("Q", RESERVAS.contains(usuario + "|" + lab + "|" + hora));
        v.put("R", hora >= HORA_MIN && hora <= HORA_MAX);

        boolean a = ACCESO.eval(v);
        List<String> razon = new ArrayList<>();
        List<String> motivos = new ArrayList<>();
        String regla = "(" + ACCESO + ")";
        if (Inferencia.modusPonens(a, true) != null) {
            razon.add("MP: " + regla + " y [" + regla + " → ABRIR] ⇒ ABRIR");
        } else if (Inferencia.modusTollens(a, true) != null) {
            razon.add("MT: ¬" + regla + " y [ABRIR → " + regla + "] ⇒ ¬ABRIR");
            Expr negada = new Expr.No(ACCESO);
            razon.add("De Morgan: " + negada + " ≡ " + negada.deMorgan());
            for (String var : VARIABLES) {
                if (!v.get(var)) {
                    motivos.add(MOTIVO.get(var));
                }
            }
        }
        return new Resultado(a, v, motivos, razon);
    }
}
