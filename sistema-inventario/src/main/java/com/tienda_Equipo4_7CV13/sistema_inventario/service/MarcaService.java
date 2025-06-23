package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Marca;
import java.util.List;

public interface MarcaService {
    List<Marca> findAll();
    Marca findById(Long id);
    Marca save(Marca marca);
    void delete(Long id);
}
