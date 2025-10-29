package com.smartstock.service;

import com.smartstock.model.Marca;
import java.util.List;
import java.util.Optional;

public interface MarcaService {
    List<Marca> findAll();
    Optional<Marca> findById(Long id);
    Marca save(Marca marca);
    void deleteById(Long id);
    Optional<Marca> findByNombre(String nombre);
}