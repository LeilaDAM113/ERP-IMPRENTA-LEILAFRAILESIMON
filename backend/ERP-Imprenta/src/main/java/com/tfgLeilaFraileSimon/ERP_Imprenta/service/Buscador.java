package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Catalogo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Cliente;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Inventario;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Maquinaria;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;

import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.CatalogoRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ClienteRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.FacturaRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.InventarioRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.MaquinariaRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.OrdenTrabajoRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PresupuestoRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.TrabajadorRepository;

// en los DTOs las cosas relacionadas vienen solo con su id (idCliente, idTrabajador...)
// esta clase las busca en la base de datos para tener el objeto de verdad.
// si el id viene vacio devuelvo null, y si no existe doy error 400 con un mensaje claro
@Component
public class Buscador {
    private final CatalogoRepository catalogoRepositorio;
    private final ClienteRepository clienteRepositorio;
    private final FacturaRepository facturaRepositorio;
    private final InventarioRepository inventarioRepositorio;
    private final MaquinariaRepository maquinariaRepositorio;
    private final OrdenTrabajoRepository ordenTrabajoRepositorio;
    private final PresupuestoRepository presupuestoRepositorio;
    private final TrabajadorRepository trabajadorRepositorio;

    public Buscador(
            CatalogoRepository catalogoRepositorio,
            ClienteRepository clienteRepositorio,
            FacturaRepository facturaRepositorio,
            InventarioRepository inventarioRepositorio,
            MaquinariaRepository maquinariaRepositorio,
            OrdenTrabajoRepository ordenTrabajoRepositorio,
            PresupuestoRepository presupuestoRepositorio,
            TrabajadorRepository trabajadorRepositorio) {
        this.catalogoRepositorio = catalogoRepositorio;
        this.clienteRepositorio = clienteRepositorio;
        this.facturaRepositorio = facturaRepositorio;
        this.inventarioRepositorio = inventarioRepositorio;
        this.maquinariaRepositorio = maquinariaRepositorio;
        this.ordenTrabajoRepositorio = ordenTrabajoRepositorio;
        this.presupuestoRepositorio = presupuestoRepositorio;
        this.trabajadorRepositorio = trabajadorRepositorio;
    }

    public Cliente cliente(Integer id) {
        return buscar(clienteRepositorio, id, "el cliente");
    }

    public Trabajador trabajador(Integer id) {
        return buscar(trabajadorRepositorio, id, "el trabajador");
    }

    public Presupuesto presupuesto(Integer id) {
        return buscar(presupuestoRepositorio, id, "el presupuesto");
    }

    public OrdenTrabajo ordenTrabajo(Integer id) {
        return buscar(ordenTrabajoRepositorio, id, "la orden de trabajo");
    }

    public Catalogo producto(Integer id) {
        return buscar(catalogoRepositorio, id, "el producto");
    }

    public Maquinaria maquina(Integer id) {
        return buscar(maquinariaRepositorio, id, "la maquina");
    }

    public Inventario material(Integer id) {
        return buscar(inventarioRepositorio, id, "el material");
    }

    public Factura factura(Integer id) {
        return buscar(facturaRepositorio, id, "la factura");
    }

    private <T> T buscar(JpaRepository<T, Integer> repositorio, Integer id, String que) {
        if (id == null) {
            return null;
        }
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No existe " + que + " con id " + id));
    }
}
