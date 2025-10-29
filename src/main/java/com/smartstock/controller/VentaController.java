package com.smartstock.controller;

import com.smartstock.model.Venta;
import com.smartstock.model.DetalleVenta;
import com.smartstock.model.Producto;
import com.smartstock.service.VentaService;
import com.smartstock.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String listarVentas(Model model) {
        List<Venta> ventas = ventaService.findAll();
        model.addAttribute("ventas", ventas);
        return "venta/listar";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        Venta venta = new Venta();
        venta.setNumeroVenta(ventaService.generarNumeroVenta());
        
        List<Producto> productos = productoService.findAll();
        
        model.addAttribute("venta", venta);
        model.addAttribute("productos", productos);
        model.addAttribute("detalle", new DetalleVenta());
        
        return "venta/formulario";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute Venta venta, RedirectAttributes redirectAttributes) {
        try {
            // Calcular total
            BigDecimal total = venta.getDetalles().stream()
                .map(DetalleVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            venta.setTotal(total);
            
            // Actualizar stock de productos
            for (DetalleVenta detalle : venta.getDetalles()) {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() - detalle.getCantidad());
                productoService.save(producto);
            }
            
            ventaService.save(venta);
            redirectAttributes.addFlashAttribute("success", "Venta registrada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar la venta: " + e.getMessage());
        }
        return "redirect:/ventas";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleVenta(@PathVariable Long id, Model model) {
        Venta venta = ventaService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de venta inválido: " + id));
        model.addAttribute("venta", venta);
        return "venta/detalle";
    }
}