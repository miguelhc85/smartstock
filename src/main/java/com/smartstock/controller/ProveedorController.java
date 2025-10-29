package com.smartstock.controller;

import com.smartstock.model.Proveedor;
import com.smartstock.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listarProveedores(Model model) {
        List<Proveedor> proveedores = proveedorService.findAll();
        model.addAttribute("proveedores", proveedores);
        return "proveedor/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "proveedor/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Proveedor proveedor = proveedorService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de proveedor inválido: " + id));
        model.addAttribute("proveedor", proveedor);
        return "proveedor/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor, RedirectAttributes redirectAttributes) {
        try {
            // Validar RUC único (solo para nuevos proveedores)
            if (proveedor.getId() == null) {
                Optional<Proveedor> proveedorExistente = proveedorService.findByRuc(proveedor.getRuc());
                if (proveedorExistente.isPresent()) {
                    redirectAttributes.addFlashAttribute("error", "Ya existe un proveedor con este RUC");
                    return "redirect:/proveedores/nuevo";
                }
            }
            
            proveedorService.save(proveedor);
            redirectAttributes.addFlashAttribute("success", "Proveedor guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el proveedor: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            proveedorService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Proveedor eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el proveedor: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }
}