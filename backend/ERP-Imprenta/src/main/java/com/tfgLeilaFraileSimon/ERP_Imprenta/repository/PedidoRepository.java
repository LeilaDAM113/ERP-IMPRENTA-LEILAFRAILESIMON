package com.tfgLeilaFraileSimon.ERP_Imprenta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoPedido;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Pedido;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Proveedor;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    // el pedido automatico que esta en borrador para un proveedor (si hay)
    // spring hace la consulta solo por el nombre: proveedor = ? y estado = ? y automatico = true
    Optional<Pedido> findFirstByProveedorAndEstadoAndAutomaticoTrue(Proveedor proveedor, EstadoPedido estado);
}
