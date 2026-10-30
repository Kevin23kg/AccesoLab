package com.company.accesolab;

/**
 * Reglas de inferencia: modus ponens y modus tollens.
 */
public final class Inferencia {

    private Inferencia() {
    }

    public static boolean implica(boolean p, boolean q) {
        return !p || q;
    }

    /**
     * De p y (p -> q) se concluye q. Devuelve TRUE o null si no se puede
     * concluir.
     */
    public static Boolean modusPonens(boolean p, boolean pImplicaQ) {
        return (p && pImplicaQ) ? Boolean.TRUE : null;
    }

    /**
     * De (no q) y (p -> q) se concluye (no p). Devuelve FALSE (p es falsa) o
     * null.
     */
    public static Boolean modusTollens(boolean q, boolean pImplicaQ) {
        return (!q && pImplicaQ) ? Boolean.FALSE : null;
    }

    /**
     * [(p -> q) y p] -> q es tautologia.
     */
    public static boolean modusPonensEsValido() {
        for (boolean[] f : TablaVerdad.combinaciones(2)) {
            boolean p = f[0], q = f[1];
            if (!implica(implica(p, q) && p, q)) {
                return false;
            }
        }
        return true;
    }

    /**
     * [(p -> q) y no q] -> no p es tautologia.
     */
    public static boolean modusTollensEsValido() {
        for (boolean[] f : TablaVerdad.combinaciones(2)) {
            boolean p = f[0], q = f[1];
            if (!implica(implica(p, q) && !q, !p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * [(p -> q) y q] -> p NO es tautologia: afirmar el consecuente es invalido.
     */
    public static boolean afirmarConsecuenteEsValido() {
        for (boolean[] f : TablaVerdad.combinaciones(2)) {
            boolean p = f[0], q = f[1];
            if (!implica(implica(p, q) && q, p)) {
                return false;
            }
        }
        return true;
    }
}
