package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Marca;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    public List<Marca> findAll() {
        return marcaRepository.findAll();
    }

    public List<Marca> findAllOrdenadas() {
        return marcaRepository.findAllByOrderByNombreAsc();
    }

    public Marca findById(Long id) {
        return marcaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + id));
    }

    public Marca save(Marca marca) {
        return marcaRepository.save(marca);
    }

    public void deleteById(Long id) {
        marcaRepository.deleteById(id);
    }

    public List<Marca> buscarPorNombre(String nombre) {
        return marcaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Optional<Marca> findByNombre(String nombre) {
        return marcaRepository.findByNombre(nombre);
    }
}
