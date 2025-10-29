package com.smartstock.service.impl;

import com.smartstock.model.Marca;
import com.smartstock.repository.MarcaRepository;
import com.smartstock.service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaServiceImpl implements MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Override
    public List<Marca> findAll() {
        return marcaRepository.findAll();
    }

    @Override
    public Optional<Marca> findById(Long id) {
        return marcaRepository.findById(id);
    }

    @Override
    public Marca save(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Override
    public void deleteById(Long id) {
        marcaRepository.deleteById(id);
    }

    @Override
    public Optional<Marca> findByNombre(String nombre) {
        return marcaRepository.findByNombre(nombre);
    }
}