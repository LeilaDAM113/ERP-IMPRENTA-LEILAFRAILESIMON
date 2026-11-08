package com.tfgLeilaFraileSimon.ERP_Imprenta.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoPedido;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Inventario;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPedido;

public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Integer> {
    // dice si un material ya esta en algun pedido con uno de esos estados
    // (Pedido_Estado = el estado del pedido de la linea)
    boolean existsByMaterialAndPedido_EstadoIn(Inventario material, Collection<EstadoPedido> estados);
}
