package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Pago;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PagoRepository;

// servicio de Pago
@Service
public class PagoService {
    private final PagoRepository repositorio;

    public PagoService(PagoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // todos
    public List<Pago> listar() {
        return repositorio.findAll();
    }

    // uno por su id
    public Pago obtener(Integer id) {
        return repositorio.findById(id).orElseThrow();
    }

    // guardar uno nuevo
    public Pago guardar(Pago objeto) {
        return repositorio.save(objeto);
    }

        // modificar uno
    public Pago actualizar(Integer id, Pago objeto) {
        // si no existe da 404
        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException();
        }
        objeto.setId(id);
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
}
