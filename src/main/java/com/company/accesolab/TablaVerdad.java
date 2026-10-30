package com.company.accesolab;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tablas de verdad y comprobacion de equivalencias.
 */
public final class TablaVerdad {

    private TablaVerdad() {
    }

    /**
     * Todas las combinaciones de n variables, empezando por todas verdaderas.
     */
    public static List<boolean[]> combinaciones(int n) {
        List<boolean[]> filas = new ArrayList<>();
        for (int i = 0; i < (1 << n); i++) {
            boolean[] f = new boolean[n];
            for (int j = 0; j < n; j++) {
                f[j] = ((i >> (n - 1 - j)) & 1) == 0;
            }
            filas.add(f);
        }
        return filas;
    }

    public static Map<String, Boolean> asignar(List<String> vars, boolean[] fila) {
        Map<String, Boolean> m = new LinkedHashMap<>();
        for (int i = 0; i < vars.size(); i++) {
            m.put(vars.get(i), fila[i]);
        }
        return m;
    }

    /**
     * a y b son equivalentes si valen lo mismo en todas las filas.
     */
    public static boolean equivalentes(Expr a, Expr b, List<String> vars) {
        for (boolean[] f : combinaciones(vars.size())) {
            Map<String, Boolean> v = asignar(vars, f);
            if (a.eval(v) != b.eval(v)) {
                return false;
            }
        }
        return true;
    }
}
