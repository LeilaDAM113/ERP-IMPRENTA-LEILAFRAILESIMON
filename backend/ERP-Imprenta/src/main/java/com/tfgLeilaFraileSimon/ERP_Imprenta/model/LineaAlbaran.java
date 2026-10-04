package com.tfgLeilaFraileSimon.ERP_Imprenta.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "linea_albaran")
public class LineaAlbaran {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_albaran")
    private Albaran albaran;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Catalogo producto;
    private Integer cantidad;

   
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Albaran getAlbaran() { return albaran; }
    public void setAlbaran(Albaran albaran) { this.albaran = albaran; }

    public Catalogo getProducto() { return producto; }
    public void setProducto(Catalogo producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

}
