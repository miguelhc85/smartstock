package com.smartstock.service;

import com.smartstock.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> findAll();
    Optional<Producto> findById(Long id);
    Optional<Producto> findByCodigo(String codigo);
    Producto save(Producto producto);
    void deleteById(Long id);
    List<Producto> findByNombreContaining(String nombre);
    List<Producto> findProductosStockBajo();
    boolean existsByCodigo(String codigo);
}