package com.smartstock.controller;

import com.smartstock.model.Categoria;
import com.smartstock.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listarCategorias(Model model) {
        List<Categoria> categorias = categoriaService.findAll();
        model.addAttribute("categorias", categorias);
        return "categoria/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de categoría inválido: " + id));
        model.addAttribute("categoria", categoria);
        return "categoria/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttributes) {
        try {
            // Validar nombre único (solo para nuevas categorías)
            if (categoria.getId() == null) {
                Optional<Categoria> categoriaExistente = categoriaService.findByNombre(categoria.getNombre());
                if (categoriaExistente.isPresent()) {
                    redirectAttributes.addFlashAttribute("error", "Ya existe una categoría con este nombre");
                    return "redirect:/categorias/nuevo";
                }
            }
            
            categoriaService.save(categoria);
            redirectAttributes.addFlashAttribute("success", "Categoría guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la categoría: " + e.getMessage());
        }
        return "redirect:/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Categoría eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la categoría: " + e.getMessage());
        }
        return "redirect:/categorias";
    }
}