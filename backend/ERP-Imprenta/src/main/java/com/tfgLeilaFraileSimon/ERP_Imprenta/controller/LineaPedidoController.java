package com.tfgLeilaFraileSimon.ERP_Imprenta.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPedido;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.LineaPedidoService;

@RestController
@RequestMapping("/api/linea-pedido")
public class LineaPedidoController {
    private final LineaPedidoService servicio;

    public LineaPedidoController(LineaPedidoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<LineaPedido> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public LineaPedido obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public LineaPedido anadir(@RequestBody LineaPedido objeto) {
        return servicio.guardar(objeto);
    }

    @PutMapping("/{id}")
    public LineaPedido actualizar(@PathVariable Integer id, @RequestBody LineaPedido objeto) {
        return servicio.actualizar(id, objeto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
