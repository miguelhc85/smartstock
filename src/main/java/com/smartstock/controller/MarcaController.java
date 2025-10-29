package com.smartstock.controller;

import com.smartstock.model.Marca;
import com.smartstock.service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/marcas")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @GetMapping
    public String listarMarcas(Model model) {
        List<Marca> marcas = marcaService.findAll();
        model.addAttribute("marcas", marcas);
        return "marca/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("marca", new Marca());
        return "marca/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Marca marca = marcaService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de marca inválido: " + id));
        model.addAttribute("marca", marca);
        return "marca/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMarca(@ModelAttribute Marca marca, RedirectAttributes redirectAttributes) {
        try {
            // Validar nombre único (solo para nuevas marcas)
            if (marca.getId() == null) {
                Optional<Marca> marcaExistente = marcaService.findByNombre(marca.getNombre());
                if (marcaExistente.isPresent()) {
                    redirectAttributes.addFlashAttribute("error", "Ya existe una marca con este nombre");
                    return "redirect:/marcas/nuevo";
                }
            }
            
            marcaService.save(marca);
            redirectAttributes.addFlashAttribute("success", "Marca guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la marca: " + e.getMessage());
        }
        return "redirect:/marcas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMarca(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            marcaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Marca eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la marca: " + e.getMessage());
        }
        return "redirect:/marcas";
    }
}