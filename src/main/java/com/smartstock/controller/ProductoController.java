package com.smartstock.controller;

import com.smartstock.model.Producto;
import com.smartstock.model.Categoria;
import com.smartstock.model.Marca;
import com.smartstock.model.Proveedor;
import com.smartstock.service.ProductoService;
import com.smartstock.service.CategoriaService;
import com.smartstock.service.MarcaService;
import com.smartstock.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "producto/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Producto producto = new Producto();
        List<Categoria> categorias = categoriaService.findAll();
        List<Marca> marcas = marcaService.findAll();
        List<Proveedor> proveedores = proveedorService.findAll();
        
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categorias);
        model.addAttribute("marcas", marcas);
        model.addAttribute("proveedores", proveedores);
        
        return "producto/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Producto producto = productoService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de producto inválido: " + id));
        
        List<Categoria> categorias = categoriaService.findAll();
        List<Marca> marcas = marcaService.findAll();
        List<Proveedor> proveedores = proveedorService.findAll();
        
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categorias);
        model.addAttribute("marcas", marcas);
        model.addAttribute("proveedores", proveedores);
        
        return "producto/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            productoService.save(producto);
            redirectAttributes.addFlashAttribute("success", "Producto guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el producto: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productoService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    @GetMapping("/buscar")
    public String buscarProductos(@RequestParam String nombre, Model model) {
        List<Producto> productos = productoService.findByNombreContaining(nombre);
        model.addAttribute("productos", productos);
        model.addAttribute("terminoBusqueda", nombre);
        return "producto/listar";
    }

    @GetMapping("/stock-bajo")
    public String productosStockBajo(Model model) {
        List<Producto> productos = productoService.findProductosStockBajo();
        model.addAttribute("productos", productos);
        model.addAttribute("tituloEspecial", "Productos con Stock Bajo");
        return "producto/listar";
    }
    
 // Agregar este método al ProductoController.java
    @GetMapping("/detalle/{id}")
    public String verDetalleProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de producto inválido: " + id));
        model.addAttribute("producto", producto);
        return "producto/detalle";
    }
}