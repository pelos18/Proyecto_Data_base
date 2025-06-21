package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.Cliente;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.BusinessException;
import com.tienda_Equipo4_7CV13.sistema_inventario.exception.ValidationException;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    // Obtener todos los clientes
    public List<Cliente> findAll() {
        return clienteRepository.findAllByOrderByNombreAsc();
    }
    
    // Obtener cliente por ID
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Cliente con ID " + id + " no encontrado"));
    }
    
    // Crear nuevo cliente
    public Cliente save(Cliente cliente) {
        // Validar email único si se proporciona
        if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
            if (clienteRepository.existsByEmail(cliente.getEmail())) {
                throw new ValidationException("Ya existe un cliente con el email: " + cliente.getEmail());
            }
        }
        
        return clienteRepository.save(cliente);
    }
    
    // Actualizar cliente
    public Cliente update(Cliente cliente) {
        Cliente clienteExistente = findById(cliente.getIdCliente());
        
        // Verificar email único si cambió
        if (cliente.getEmail() != null && !cliente.getEmail().isEmpty() &&
            !cliente.getEmail().equals(clienteExistente.getEmail()) &&
            clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ValidationException("Ya existe un cliente con el email: " + cliente.getEmail());
        }
        
        return clienteRepository.save(cliente);
    }
    
    // Eliminar cliente
    public void deleteById(Long id) {
        Cliente cliente = findById(id);
        
        // Verificar si el cliente tiene ventas asociadas
        // Esto se podría implementar con una consulta adicional si es necesario
        
        clienteRepository.deleteById(id);
    }
    
    // Buscar clientes por nombre
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Buscar cliente por email
    public Cliente findByEmail(String email) {
        return clienteRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("Cliente con email '" + email + "' no encontrado"));
    }
    
    // Buscar clientes por teléfono
    public List<Cliente> buscarPorTelefono(String telefono) {
        return clienteRepository.findByTelefonoContaining(telefono);
    }
    
    // Buscar por término general (nombre, email, teléfono)
    public List<Cliente> buscarPorTermino(String termino) {
        return clienteRepository.buscarPorTermino(termino);
    }
    
    // Obtener clientes con más compras
    public List<Cliente> getClientesConMasCompras() {
        return clienteRepository.findClientesConMasCompras();
    }
    
    // Obtener estadísticas de clientes
    public List<Object[]> getEstadisticasClientes() {
        return clienteRepository.getEstadisticasClientes();
    }
    
    // Buscar clientes por rango de compras
    public List<Cliente> findClientesPorRangoCompras(BigDecimal montoMinimo, BigDecimal montoMaximo) {
        return clienteRepository.findClientesPorRangoCompras(montoMinimo, montoMaximo);
    }
    
    // Clientes con compras en un período
    public List<Cliente> findClientesConComprasEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        return clienteRepository.findClientesConComprasEnPeriodo(fechaInicio, fechaFin);
    }
    
    // Validar datos del cliente
    private void validarCliente(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new ValidationException.CampoRequeridoException("nombre");
        }
        
        if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
            if (!cliente.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new ValidationException.FormatoInvalidoException("email", "email válido");
            }
        }
    }
}
