package com.tienda_Equipo4_7CV13.sistema_inventario.service;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.LoteInventario;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LoteInventarioService {
    List<LoteInventario> findAll();
    LoteInventario findById(Long id);
    LoteInventario save(LoteInventario lote);
    void delete(Long id);
    Long countLotesActivos();
    Long countLotesProximosAVencer(int dias);
    List<LoteInventario> findLotesProximosACaducar(int dias);
    List<LoteInventario> findLotesCaducados();
    BigDecimal getValorTotalInventario();
}
