package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.ConsumoMaterialRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.ConsumoMaterial;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ConsumoMaterialRepository;

// servicio de ConsumoMaterial
// recibe el DTO, busca las cosas relacionadas por su id y guarda
@Service
public class ConsumoMaterialService {
    private final ConsumoMaterialRepository repositorio;
    private final Buscador buscador;

    public ConsumoMaterialService(ConsumoMaterialRepository repositorio, Buscador buscador) {
        this.repositorio = repositorio;
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

    // guardar uno nuevo con los datos del DTO
    public ConsumoMaterial guardar(ConsumoMaterialRequest datos) {
        ConsumoMaterial objeto = new ConsumoMaterial();
        copiarDatos(datos, objeto);
        return repositorio.save(objeto);
    }

    // modificar uno: cojo el que ya hay y le cambio los datos
    public ConsumoMaterial actualizar(Integer id, ConsumoMaterialRequest datos) {
        // si no existe da 404
        ConsumoMaterial objeto = repositorio.findById(id).orElseThrow();
        copiarDatos(datos, objeto);
        return repositorio.save(objeto);
    }

    // borrar uno
    public void eliminar(Integer id) {
        // si no existe da 404
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        repositorio.deleteById(id);
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
