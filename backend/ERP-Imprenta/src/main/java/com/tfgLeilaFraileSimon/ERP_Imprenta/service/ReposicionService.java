package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoPedido;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Inventario;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPedido;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Pedido;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Proveedor;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.LineaPedidoRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PedidoRepository;

// reponer el almacen: cuando un material baja de su stock minimo,
// preparo solo un pedido en BORRADOR a su proveedor (el encargado lo revisa y lo envia)
@Service
public class ReposicionService {
    // pedidos que todavia no han llegado: si el material ya esta en uno, no lo vuelvo a pedir
    private static final List<EstadoPedido> PEDIDOS_ABIERTOS =
            List.of(EstadoPedido.BORRADOR, EstadoPedido.PENDIENTE, EstadoPedido.ENVIADO);

    private final PedidoRepository pedidoRepositorio;
    private final LineaPedidoRepository lineaPedidoRepositorio;

    public ReposicionService(PedidoRepository pedidoRepositorio, LineaPedidoRepository lineaPedidoRepositorio) {
        this.pedidoRepositorio = pedidoRepositorio;
        this.lineaPedidoRepositorio = lineaPedidoRepositorio;
    }

    // miro si hay que pedir este material y, si hace falta, lo pido
    public void revisar(Inventario material) {
        BigDecimal stock = material.getStockActual() == null ? BigDecimal.ZERO : material.getStockActual();
        BigDecimal minimo = material.getStockMinimo();

        // si no tiene minimo o todavia hay suficiente, no hago nada
        if (minimo == null || stock.compareTo(minimo) >= 0) {
            return;
        }
        // sin proveedor no se a quien pedirselo
        if (material.getProveedor() == null) {
            return;
        }
        // si ya esta pedido y todavia no ha llegado, no lo pido otra vez
        if (lineaPedidoRepositorio.existsByMaterialAndPedido_EstadoIn(material, PEDIDOS_ABIERTOS)) {
            return;
        }

        // si ya hay un pedido automatico en borrador para ese proveedor lo meto ahi, si no hago uno nuevo
        Pedido pedido = pedidoRepositorio
                .findFirstByProveedorAndEstadoAndAutomaticoTrue(material.getProveedor(), EstadoPedido.BORRADOR)
                .orElseGet(() -> nuevoPedido(material.getProveedor()));

        // pido lo que falta para llegar al doble del minimo
        // (minimo 500 y quedan 300 -> pido 700 y vuelvo a tener 1000)
        BigDecimal cantidad = minimo.multiply(BigDecimal.valueOf(2)).subtract(stock);

        LineaPedido linea = new LineaPedido();
        linea.setPedido(pedido);
        linea.setMaterial(material);
        linea.setCantidad(cantidad);
        linea.setPrecioUnitario(material.getPrecioProducto());
        lineaPedidoRepositorio.save(linea);

        // sumo la linea al importe del pedido (si se lo que cuesta el material)
        if (material.getPrecioProducto() != null) {
            BigDecimal importe = pedido.getImporte() == null ? BigDecimal.ZERO : pedido.getImporte();
            pedido.setImporte(importe.add(cantidad.multiply(material.getPrecioProducto())));
            pedidoRepositorio.save(pedido);
        }
    }

    // pedido nuevo en BORRADOR, marcado como automatico
    private Pedido nuevoPedido(Proveedor proveedor) {
        Pedido pedido = new Pedido();
        pedido.setProveedor(proveedor);
        pedido.setFechaPedido(LocalDate.now());
        pedido.setEstado(EstadoPedido.BORRADOR);
        pedido.setAutomatico(true);
        pedido.setDescripcion("Pedido automatico: material por debajo del stock minimo");
        pedido.setImporte(BigDecimal.ZERO);
        pedido = pedidoRepositorio.save(pedido);
        // el numero sale del id que le ha dado la base de datos: PED-0001, PED-0002...
        pedido.setNumero(String.format("PED-%04d", pedido.getId()));
        return pedidoRepositorio.save(pedido);
    }
}
