package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Marca;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
public class MarcaApiController {

    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping
    public ResponseEntity<List<Marca>> getAllMarcas() {
        try {
            List<Marca> marcas = marcaRepository.findAllByOrderByNombreAsc();
            return ResponseEntity.ok(marcas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Marca> crearMarca(@RequestBody Marca marca) {
        try {
            Marca nuevaMarca = marcaRepository.save(marca);
            return ResponseEntity.ok(nuevaMarca);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
