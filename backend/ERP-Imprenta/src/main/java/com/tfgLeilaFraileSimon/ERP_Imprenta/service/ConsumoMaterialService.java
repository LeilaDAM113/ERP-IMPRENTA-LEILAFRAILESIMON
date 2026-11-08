package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.ConsumoMaterialRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ConsumoMaterial;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Inventario;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ConsumoMaterialRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.InventarioRepository;

// servicio de ConsumoMaterial (el material que el operario gasta en una orden)
// cada vez que se apunta un consumo, se resta del stock del almacen.
// si se modifica o se borra, se corrige el stock
@Service
public class ConsumoMaterialService {
    private final ConsumoMaterialRepository repositorio;
    private final InventarioRepository inventarioRepositorio;
    private final Buscador buscador;

    public ConsumoMaterialService(ConsumoMaterialRepository repositorio, InventarioRepository inventarioRepositorio,
            Buscador buscador) {
        this.repositorio = repositorio;
        this.inventarioRepositorio = inventarioRepositorio;
        this.buscador = buscador;
    }

    // todos
    public List<ConsumoMaterial> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public ConsumoMaterial obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // apuntar un consumo nuevo: lo guardo y lo resto del almacen
    // @Transactional: o se hacen las dos cosas o ninguna
    @Transactional
    public ConsumoMaterial guardar(ConsumoMaterialRequest datos) {
        ConsumoMaterial objeto = new ConsumoMaterial();
        copiarDatos(datos, objeto);
        // si no me dicen la fecha, pongo ahora mismo
        if (objeto.getFecha() == null) {
            objeto.setFecha(LocalDateTime.now());
        }
        restarStock(objeto.getMaterial(), objeto.getCantidad());
        return repositorio.save(objeto);
    }

    // modificar un consumo: devuelvo al almacen lo que se habia gastado
    // y resto lo nuevo (asi vale aunque cambie la cantidad o el material)
    @Transactional
    public ConsumoMaterial actualizar(Integer id, ConsumoMaterialRequest datos) {
        // si no existe da 404
        ConsumoMaterial objeto = repositorio.findById(id).orElseThrow();
        LocalDateTime fechaAntes = objeto.getFecha();
        sumarStock(objeto.getMaterial(), objeto.getCantidad());
        copiarDatos(datos, objeto);
        // si no me mandan fecha, se queda la que tenia
        if (objeto.getFecha() == null) {
            objeto.setFecha(fechaAntes);
        }
        restarStock(objeto.getMaterial(), objeto.getCantidad());
        return repositorio.save(objeto);
    }

    // borrar un consumo (por ejemplo si se apunto mal): devuelvo el material al almacen
    @Transactional
    public void eliminar(Integer id) {
        // si no existe da 404
        ConsumoMaterial objeto = repositorio.findById(id).orElseThrow();
        sumarStock(objeto.getMaterial(), objeto.getCantidad());
        repositorio.delete(objeto);
    }

    // quito material del almacen. Si no hay suficiente, no dejo (409)
    private void restarStock(Inventario material, BigDecimal cantidad) {
        BigDecimal stock = stockDe(material);
        if (stock.compareTo(cantidad) < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No hay suficiente " + material.getNombre() + ": quedan " + stock
                            + " y se quieren gastar " + cantidad);
        }
        material.setStockActual(stock.subtract(cantidad));
        material.setFechaActualizacion(LocalDate.now());
        inventarioRepositorio.save(material);
    }

    // devuelvo material al almacen
    private void sumarStock(Inventario material, BigDecimal cantidad) {
        material.setStockActual(stockDe(material).add(cantidad));
        material.setFechaActualizacion(LocalDate.now());
        inventarioRepositorio.save(material);
    }

    // si el material no tiene stock apuntado, cuento que hay 0
    private BigDecimal stockDe(Inventario material) {
        return material.getStockActual() == null ? BigDecimal.ZERO : material.getStockActual();
    }

    // paso los datos del DTO al objeto (lo uso al crear y al modificar)
    private void copiarDatos(ConsumoMaterialRequest datos, ConsumoMaterial objeto) {
        objeto.setOrdenTrabajo(buscador.ordenTrabajo(datos.idOrdenTrabajo()));
        objeto.setMaterial(buscador.material(datos.idMaterial()));
        objeto.setTrabajador(buscador.trabajador(datos.idTrabajador()));
        objeto.setCantidad(datos.cantidad());
        objeto.setFecha(datos.fecha());
    }
}
