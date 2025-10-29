package com.smartstock.repository;

import com.smartstock.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    Optional<Venta> findByNumeroVenta(String numeroVenta);
    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fechaVenta BETWEEN ?1 AND ?2")
    Double findTotalVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin);
}