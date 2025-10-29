package com.smartstock.service.impl;

import com.smartstock.dto.ReporteInventarioDTO;
import com.smartstock.model.Producto;
import com.smartstock.model.Venta;
import com.smartstock.service.ProductoService;
import com.smartstock.service.ReporteService;
import com.smartstock.service.VentaService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    @Override
    public void generarReporteInventario(HttpServletResponse response) throws JRException, IOException {
        List<ReporteInventarioDTO> datos = obtenerDatosReporteInventario();
        
        try (InputStream reportStream = new ClassPathResource("reports/reporte_inventario.jrxml").getInputStream()) {
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("titulo", "REPORTE DE INVENTARIO - SMARTSTOCK");
            parameters.put("fechaGeneracion", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datos);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_inventario_" + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf");
            
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        }
    }

    @Override
    public void generarReporteVentas(LocalDate fechaInicio, LocalDate fechaFin, HttpServletResponse response) throws JRException, IOException {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);
        
        List<Venta> ventas = ventaService.findByFechaVentaBetween(inicio, fin);
        List<Map<String, Object>> datosVentas = new ArrayList<>();
        
        for (Venta venta : ventas) {
            Map<String, Object> dato = new HashMap<>();
            dato.put("numeroVenta", venta.getNumeroVenta());
            dato.put("fechaVenta", venta.getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            dato.put("cliente", venta.getCliente());
            dato.put("total", venta.getTotal());
            datosVentas.add(dato);
        }
        
        try (InputStream reportStream = new ClassPathResource("reports/reporte_ventas.jrxml").getInputStream()) {
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("titulo", "REPORTE DE VENTAS - SMARTSTOCK");
            parameters.put("fechaInicio", fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            parameters.put("fechaFin", fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            parameters.put("fechaGeneracion", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datosVentas);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_ventas_" + 
                fechaInicio.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_" +
                fechaFin.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf");
            
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        }
    }

    @Override
    public List<ReporteInventarioDTO> obtenerDatosReporteInventario() {
        List<Producto> productos = productoService.findAll();
        List<ReporteInventarioDTO> reporte = new ArrayList<>();
        
        for (Producto producto : productos) {
            ReporteInventarioDTO dto = new ReporteInventarioDTO();
            dto.setCodigo(producto.getCodigo());
            dto.setNombre(producto.getNombre());
            dto.setCategoria(producto.getCategoria().getNombre());
            dto.setMarca(producto.getMarca().getNombre());
            dto.setStock(producto.getStock());
            dto.setStockMinimo(producto.getStockMinimo());
            dto.setPrecio(producto.getPrecio());
            
            if (producto.getStock() == 0) {
                dto.setEstado("SIN STOCK");
            } else if (producto.getStock() <= producto.getStockMinimo()) {
                dto.setEstado("STOCK BAJO");
            } else {
                dto.setEstado("NORMAL");
            }
            
            reporte.add(dto);
        }
        
        return reporte;
    }
}