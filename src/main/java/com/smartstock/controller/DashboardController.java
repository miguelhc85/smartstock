package com.smartstock.controller;

import com.smartstock.service.ProductoService;
import com.smartstock.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class DashboardController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalProductos = productoService.findAll().size();
        long productosStockBajo = productoService.findProductosStockBajo().size();
        
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finMes = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth())
                .withHour(23).withMinute(59).withSecond(59);
        
        double totalVentasMes = ventaService.getTotalVentasPorPeriodo(inicioMes, finMes);
        
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("productosStockBajo", productosStockBajo);
        model.addAttribute("totalVentasMes", totalVentasMes);
        
        return "dashboard-simple"; // Cambia esto según el archivo que uses
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}