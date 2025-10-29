package com.smartstock.service.impl;

import com.smartstock.model.Producto;
import com.smartstock.repository.ProductoRepository;
import com.smartstock.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Optional<Producto> findByCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo);
    }

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> findByNombreContaining(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Producto> findProductosStockBajo() {
        return productoRepository.findProductosStockBajo();
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo).isPresent();
    }
}