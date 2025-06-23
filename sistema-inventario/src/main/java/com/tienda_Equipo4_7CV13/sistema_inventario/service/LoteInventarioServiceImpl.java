package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.LoteInventario;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.LoteInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoteInventarioServiceImpl implements LoteInventarioService {

    @Autowired
    private LoteInventarioRepository loteInventarioRepository;

    @Override
    public List<LoteInventario> findAll() {
        return loteInventarioRepository.findAll();
    }

    @Override
    public LoteInventario findById(Long id) {
        return loteInventarioRepository.findById(id).orElse(null);
    }

    @Override
    public LoteInventario save(LoteInventario lote) {
        return loteInventarioRepository.save(lote);
    }

    @Override
    public void delete(Long id) {
        loteInventarioRepository.deleteById(id);
    }

    @Override
    public Long countLotesActivos() {
        return loteInventarioRepository.countLotesActivos();
    }

    @Override
    public Long countLotesProximosAVencer(int dias) {
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return loteInventarioRepository.countLotesProximosAVencer(fechaLimite);
    }

    @Override
    public List<LoteInventario> findLotesProximosACaducar(int dias) {
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return loteInventarioRepository.findByFechaCaducidadBetween(LocalDate.now(), fechaLimite);
    }

    @Override
    public List<LoteInventario> findLotesCaducados() {
        return loteInventarioRepository.findByFechaCaducidadBefore(LocalDate.now());
    }

    @Override
    public BigDecimal getValorTotalInventario() {
        BigDecimal valor = loteInventarioRepository.getValorTotalInventario();
        return valor != null ? valor : BigDecimal.ZERO;
    }
}
