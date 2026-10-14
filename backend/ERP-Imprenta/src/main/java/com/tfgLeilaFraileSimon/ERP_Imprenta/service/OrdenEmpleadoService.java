package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenEmpleado;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.OrdenEmpleadoRepository;

// Lo que se puede hacer con OrdenEmpleado (lo usa OrdenEmpleadoController)
@Service
public class OrdenEmpleadoService {
    private final OrdenEmpleadoRepository repositorio;

    public OrdenEmpleadoService(OrdenEmpleadoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<OrdenEmpleado> listar() {
        return repositorio.findAll();
    }

    public OrdenEmpleado obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    public OrdenEmpleado guardar(OrdenEmpleado objeto) {
        return repositorio.save(objeto);
    }

       // cambiar los datos de uno que ya existe
    public OrdenEmpleado actualizar(Integer id, OrdenEmpleado objeto) {
        // si no existe ese id, error 404 (antes creaba uno nuevo sin querer)
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        objeto.setId(id);
        return repositorio.save(objeto);
    }

    // borrar uno
    public void eliminar(Integer id) {
        // si no existe ese id, error 404
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        repositorio.deleteById(id);
    }
}
