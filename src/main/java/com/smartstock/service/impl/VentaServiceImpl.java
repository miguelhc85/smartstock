package com.smartstock.service.impl;

import com.smartstock.model.Venta;
import com.smartstock.repository.VentaRepository;
import com.smartstock.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> findById(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> findByNumeroVenta(String numeroVenta) {
        return ventaRepository.findByNumeroVenta(numeroVenta);
    }

    @Override
    public List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
    }

    @Override
    public Double getTotalVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        Double total = ventaRepository.findTotalVentasPorPeriodo(inicio, fin);
        return total != null ? total : 0.0;
    }

    @Override
    public String generarNumeroVenta() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = ventaRepository.count() + 1;
        return "VTA-" + fecha + "-" + String.format("%04d", count);
    }
}