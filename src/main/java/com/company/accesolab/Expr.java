package com.company.accesolab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Expresion logica:(proposicion simple), Y, O, No. /
public sealed interface Expr permits Expr.Atomo, Expr.Y, Expr.O, Expr.No {

    boolean eval(Map<String, Boolean> valores);

    //Aplica la ley de De Morgan para llevar las negaciones hasta los atomos. /
    Expr deMorgan();

    record Atomo(String nombre) implements Expr {

        public boolean eval(Map<String, Boolean> v) {
            return v.get(nombre);
        }

        public Expr deMorgan() {
            return this;
        }

        public String toString() {
            return nombre;
        }
    }

    record Y(Expr izq, Expr der) implements Expr {

        public boolean eval(Map<String, Boolean> v) {
            return izq.eval(v) && der.eval(v);
        }

        public Expr deMorgan() {
            return new Y(izq.deMorgan(), der.deMorgan());
        }

        public String toString() {
            return unir(this, " ∧ ");
        }
    }

    record O(Expr izq, Expr der) implements Expr {

        public boolean eval(Map<String, Boolean> v) {
            return izq.eval(v) || der.eval(v);
        }

        public Expr deMorgan() {
            return new O(izq.deMorgan(), der.deMorgan());
        }

        public String toString() {
            return unir(this, " ∨ ");
        }
    }

    record No(Expr e) implements Expr {

        public boolean eval(Map<String, Boolean> v) {
            return !e.eval(v);
        }

        public Expr deMorgan() {
            if (e instanceof Y y) // no(a y b) = no a o no b
            {
                return new O(new No(y.izq()).deMorgan(), new No(y.der()).deMorgan());
            }
            if (e instanceof O o) // no(a o b) = no a y no b
            {
                return new Y(new No(o.izq()).deMorgan(), new No(o.der()).deMorgan());
            }
            if (e instanceof No n) // no no a = a
            {
                return n.e().deMorgan();
            }
            return this;            // no a (a es una proposicion simple)
        }

        public String toString() {
            return (e instanceof Atomo || e instanceof No) ? "¬" + e : "¬(" + e + ")";
        }
    }

    /**
     * Escribe cadenas de un mismo conectivo sin parentesis repetidos: P ∧ Q ∧
     * R.
     */
    private static String unir(Expr raiz, String conectivo) {
        List<Expr> partes = new ArrayList<>();
        aplanar(raiz, raiz instanceof Y, partes);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < partes.size(); i++) {
            Expr p = partes.get(i);
            if (i > 0) {
                sb.append(conectivo);
            }
            sb.append(p instanceof Y || p instanceof O ? "(" + p + ")" : p.toString());
        }
        return sb.toString();
    }

    private static void aplanar(Expr e, boolean esY, List<Expr> salida) {
        if (esY && e instanceof Y y) {
            aplanar(y.izq(), true, salida);
            aplanar(y.der(), true, salida);
        } else if (!esY && e instanceof O o) {
            aplanar(o.izq(), false, salida);
            aplanar(o.der(), false, salida);
        } else {
            salida.add(e);
        }
    }
}
