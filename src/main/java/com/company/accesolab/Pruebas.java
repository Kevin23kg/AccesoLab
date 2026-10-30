package com.company.accesolab;

import java.util.List;

/**
 * Pruebas sencillas sin librerias externas.
 */
public class Pruebas {

    static int total = 0, fallas = 0;

    static void verificar(String nombre, boolean condicion) {
        total++;
        if (!condicion) {
            fallas++;
            System.out.println("FALLA: " + nombre);
        }
    }

    public static void main(String[] args) {
        List<String> vars = Sistema.VARIABLES;
        long verdaderas = TablaVerdad.combinaciones(3).stream()
                .filter(f -> Sistema.ACCESO.eval(TablaVerdad.asignar(vars, f))).count();
        verificar("la tabla tiene 8 filas", TablaVerdad.combinaciones(3).size() == 8);
        verificar("solo 1 fila concede acceso", verdaderas == 1);

        Expr negado = new Expr.No(Sistema.ACCESO);
        verificar("De Morgan: no(P y Q y R) equivale a noP o noQ o noR",
                TablaVerdad.equivalentes(negado, negado.deMorgan(), vars));
        verificar("De Morgan se escribe bien", negado.deMorgan().toString().equals("¬P ∨ ¬Q ∨ ¬R"));
        Expr noO = new Expr.No(new Expr.O(new Expr.Atomo("P"), new Expr.Atomo("Q")));
        verificar("De Morgan: no(P o Q) equivale a noP y noQ",
                TablaVerdad.equivalentes(noO, noO.deMorgan(), List.of("P", "Q")));

        verificar("modus ponens concluye", Inferencia.modusPonens(true, true) == Boolean.TRUE);
        verificar("modus ponens sin antecedente", Inferencia.modusPonens(false, true) == null);
        verificar("modus tollens concluye", Inferencia.modusTollens(false, true) == Boolean.FALSE);
        verificar("modus ponens es valido", Inferencia.modusPonensEsValido());
        verificar("modus tollens es valido", Inferencia.modusTollensEsValido());
        verificar("afirmar el consecuente no es valido", !Inferencia.afirmarConsecuenteEsValido());

        Sistema s = new Sistema();
        verificar("U001 LAB1 9 concede", s.evaluar("U001", "LAB1", 9).concedido());
        verificar("usuario en minusculas", s.evaluar("u001", "LAB1", 10).concedido());
        Sistema.Resultado r = s.evaluar("U999", "LAB1", 9);
        verificar("U999 se deniega", !r.concedido() && !r.proposiciones().get("P"));
        verificar("U999 sin reserva", !r.proposiciones().get("Q"));
        verificar("sin reserva (hora 11)", s.evaluar("U001", "LAB1", 11).motivos().size() == 1);
        verificar("hora 7: sin reserva y fuera de horario", s.evaluar("U001", "LAB1", 7).motivos().size() == 2);
        verificar("reserva en otro laboratorio", !s.evaluar("U003", "LAB1", 14).concedido());
        verificar("U003 LAB2 14 concede", s.evaluar("U003", "LAB2", 14).concedido());
        verificar("razonamiento MP al conceder", s.evaluar("U001", "LAB1", 9).razonamiento().get(0).startsWith("MP"));
        verificar("razonamiento MT al negar", r.razonamiento().get(0).startsWith("MT"));

        System.out.println(total + " pruebas, " + fallas + " fallas");
        if (fallas > 0) {
            System.exit(1);
        }
    }
}
