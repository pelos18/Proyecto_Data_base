package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Cliente;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public List<Cliente> findAllOrdenados() {
        return clienteRepository.findAllOrderByNombre();
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    public Cliente save(Cliente cliente) {
        if (cliente.getIdCliente() == null) {
            // Nuevo cliente
            if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
                if (clienteRepository.existsByEmail(cliente.getEmail())) {
                    throw new RuntimeException("Ya existe un cliente con ese email");
                }
            }
        } else {
            // Actualización
            Cliente clienteExistente = findById(cliente.getIdCliente());
            if (cliente.getEmail() != null && !cliente.getEmail().isEmpty() &&
                !clienteExistente.getEmail().equals(cliente.getEmail()) &&
                clienteRepository.existsByEmail(cliente.getEmail())) {
                throw new RuntimeException("Ya existe un cliente con ese email");
            }
        }
        return clienteRepository.save(cliente);
    }

    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Long countTotalClientes() {
        return clienteRepository.countTotalClientes();
    }

    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public List<Cliente> findByTelefono(String telefono) {
        return clienteRepository.findByTelefonoContaining(telefono);
    }

    public List<Cliente> buscarPorTermino(String termino) {
        return clienteRepository.buscarPorTermino(termino);
    }

    public List<Cliente> findClientesConMasCompras() {
        // Implementación simple - devolver todos los clientes ordenados
        return clienteRepository.findAllOrderByNombre();
    }

    public List<Object[]> getEstadisticasClientes() {
        // Implementación simple - devolver lista vacía por ahora
        return List.of();
    }

    public List<Cliente> findClientesPorRangoCompras(BigDecimal montoMinimo, BigDecimal montoMaximo) {
        // Implementación simple - devolver todos los clientes
        return clienteRepository.findAllOrderByNombre();
    }

    public List<Cliente> findClientesConComprasEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        // Implementación simple - devolver todos los clientes
        return clienteRepository.findAllOrderByNombre();
    }
}
