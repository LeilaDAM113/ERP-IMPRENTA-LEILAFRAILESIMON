package com.tfgLeilaFraileSimon.ERP_Imprenta.dto;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Catalogo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Cliente;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Inventario;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Maquinaria;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;

// lo que sale de una cosa relacionada
// asi la app tiene lo que necesita para pintar la pantalla, sin todos los datos de esa cosa
public record Resumen(Integer id, String nombre) {

    // un "de" para cada tipo de cosa. Si no hay nada (null), devuelvo null
    public static Resumen de(Cliente c) {
        return c == null ? null : new Resumen(c.getId(), c.getNombreCompleto());
    }

    public static Resumen de(Trabajador t) {
        return t == null ? null : new Resumen(t.getId(), t.getNombreCompleto());
    }

    public static Resumen de(Catalogo c) {
        return c == null ? null : new Resumen(c.getId(), c.getNombre());
    }

    public static Resumen de(Maquinaria m) {
        return m == null ? null : new Resumen(m.getId(), m.getNombre());
    }

    public static Resumen de(Inventario i) {
        return i == null ? null : new Resumen(i.getId(), i.getNombre());
    }

    public static Resumen de(Presupuesto p) {
        return p == null ? null : new Resumen(p.getId(), p.getNumero());
    }

    public static Resumen de(OrdenTrabajo o) {
        return o == null ? null : new Resumen(o.getId(), o.getNumero());
    }

    public static Resumen de(Factura f) {
        return f == null ? null : new Resumen(f.getId(), f.getNumero());
    }
}
