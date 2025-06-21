package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Cliente;
import com.tienda_Equipo4_7CV13.sistema_inventario.service.ClienteService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        try {
            List<Cliente> clientes = clienteService.findAll();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.findById(id);
            return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.save(cliente);
            return ResponseEntity.ok(nuevoCliente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        try {
            cliente.setIdCliente(id);
            Cliente clienteActualizado = clienteService.save(cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        try {
            clienteService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarClientes(@RequestParam String termino) {
        try {
            List<Cliente> clientes = clienteService.buscarPorNombre(termino);
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
