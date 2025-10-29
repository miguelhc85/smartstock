package com.smartstock.service;

import com.smartstock.model.Venta;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaService {
    List<Venta> findAll();
    Optional<Venta> findById(Long id);
    Venta save(Venta venta);
    Optional<Venta> findByNumeroVenta(String numeroVenta);
    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);
    Double getTotalVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin);
    String generarNumeroVenta();
}