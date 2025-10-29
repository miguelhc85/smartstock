package com.smartstock.service;

import com.smartstock.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> findAll();
    Optional<Categoria> findById(Long id);
    Categoria save(Categoria categoria);
    void deleteById(Long id);
    Optional<Categoria> findByNombre(String nombre);
}