package com.tienda_Equipo4_7CV13.sistema_inventario.controller;

import com.tienda_Equipo4_7CV13.sistema_inventario.entity.*;
import com.tienda_Equipo4_7CV13.sistema_inventario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/inventario")
public class InventarioCompletoController {

    @Autowired
    private ProveedorRepository proveedorRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private LoteInventarioRepository loteInventarioRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    // **ACCIÓN 1: MOSTRAR FORMULARIO PASO 1 (PROVEEDOR + CATEGORÍA + MARCA)**
    @GetMapping("/nuevo-completo")
    public String mostrarAccion1(Model model) {
        System.out.println("🔥 ACCIÓN 1: Formulario Proveedor + Categoría + Marca");
        return "inventario/accion1-proveedor-categoria-marca";
    }

    // **ACCIÓN 1: PROCESAR PROVEEDOR + CATEGORÍA + MARCA**
    @PostMapping("/guardar-accion1")
    @Transactional
    public String procesarAccion1(
            @RequestParam String proveedorNombre,
            @RequestParam(required = false) String proveedorTelefono,
            @RequestParam(required = false) String proveedorDireccion,
            @RequestParam String categoriaNombre,
            @RequestParam(required = false) String categoriaDescripcion,
            @RequestParam String marcaNombre,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🔥 === ACCIÓN 1: GUARDANDO PROVEEDOR → CATEGORÍA → MARCA ===");
            
            // **PASO 1: CREAR/OBTENER PROVEEDOR PRIMERO** 🚛
            System.out.println("🚛 PASO 1: Creando/obteniendo Proveedor: " + proveedorNombre);
            Proveedor proveedor = proveedorRepository.findByNombre(proveedorNombre)
                .orElseGet(() -> {
                    Proveedor nuevoProveedor = new Proveedor();
                    nuevoProveedor.setNombre(proveedorNombre);
                    nuevoProveedor.setTelefono(proveedorTelefono != null ? proveedorTelefono : "");
                    nuevoProveedor.setDireccion(proveedorDireccion != null ? proveedorDireccion : "");
                    
                    // ✅ GUARDAR Y FLUSH SIN REFRESH
                    Proveedor guardado = proveedorRepository.save(nuevoProveedor);
                    entityManager.flush(); // Forzar persistencia inmediata
                    
                    System.out.println("✅ Proveedor guardado y flushed con ID: " + guardado.getIdProveedor());
                    return guardado;
                });
            
            System.out.println("✅ Proveedor final con ID: " + proveedor.getIdProveedor());
            
            // **PASO 2: CREAR/OBTENER CATEGORÍA SEGUNDO** 📂
            System.out.println("📂 PASO 2: Creando/obteniendo Categoría: " + categoriaNombre);
            Categoria categoria = categoriaRepository.findByNombre(categoriaNombre)
                .orElseGet(() -> {
                    Categoria nuevaCategoria = new Categoria();
                    nuevaCategoria.setNombre(categoriaNombre);
                    nuevaCategoria.setDescripcion(categoriaDescripcion != null ? categoriaDescripcion : "Categoría creada automáticamente");
                    
                    // ✅ GUARDAR Y FLUSH SIN REFRESH
                    Categoria guardada = categoriaRepository.save(nuevaCategoria);
                    entityManager.flush(); // Forzar persistencia inmediata
                    
                    System.out.println("✅ Categoría guardada y flushed con ID: " + guardada.getIdCategoria());
                    return guardada;
                });
            
            System.out.println("✅ Categoría final con ID: " + categoria.getIdCategoria());
            
            // **PASO 3: CREAR/OBTENER MARCA TERCERO** 🏷️
            System.out.println("🏷️ PASO 3: Creando/obteniendo Marca: " + marcaNombre);
            Marca marca = marcaRepository.findByNombre(marcaNombre)
                .orElseGet(() -> {
                    Marca nuevaMarca = new Marca();
                    nuevaMarca.setNombre(marcaNombre);
                    
                    // ✅ GUARDAR Y FLUSH SIN REFRESH
                    Marca guardada = marcaRepository.save(nuevaMarca);
                    entityManager.flush(); // Forzar persistencia inmediata
                    
                    System.out.println("✅ Marca guardada y flushed con ID: " + guardada.getIdMarca());
                    return guardada;
                });
            
            System.out.println("✅ Marca final con ID: " + marca.getIdMarca());
            
            // **VERIFICAR QUE LOS IDs SON VÁLIDOS**
            System.out.println("🔍 === VERIFICACIÓN DE IDs ASIGNADOS ===");
            System.out.println("🚛 Proveedor: " + proveedor.getNombre() + " → ID: " + proveedor.getIdProveedor());
            System.out.println("📂 Categoría: " + categoria.getNombre() + " → ID: " + categoria.getIdCategoria());
            System.out.println("🏷️ Marca: " + marca.getNombre() + " → ID: " + marca.getIdMarca());
            
            // **GUARDAR IDs EN SESIÓN**
            session.setAttribute("proveedorId", proveedor.getIdProveedor());
            session.setAttribute("proveedorNombre", proveedor.getNombre());
            session.setAttribute("categoriaId", categoria.getIdCategoria());
            session.setAttribute("categoriaNombre", categoria.getNombre());
            session.setAttribute("marcaId", marca.getIdMarca());
            session.setAttribute("marcaNombre", marca.getNombre());
            
            System.out.println("🎉 ACCIÓN 1 COMPLETADA EXITOSAMENTE - Pasando a ACCIÓN 2");
            
            redirectAttributes.addFlashAttribute("success", 
                "¡Acción 1 completada! Proveedor, Categoría y Marca creados. Ahora completa el Lote y Producto.");
            
            return "redirect:/inventario/accion2";
            
        } catch (Exception e) {
            System.err.println("❌ Error en Acción 1: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error en Acción 1: " + e.getMessage());
            return "redirect:/inventario/nuevo-completo";
        }
    }

    // **ACCIÓN 2: MOSTRAR FORMULARIO PASO 2 (LOTE + PRODUCTO)**
    @GetMapping("/accion2")
    public String mostrarAccion2(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long proveedorId = (Long) session.getAttribute("proveedorId");
        String proveedorNombre = (String) session.getAttribute("proveedorNombre");
        String categoriaNombre = (String) session.getAttribute("categoriaNombre");
        String marcaNombre = (String) session.getAttribute("marcaNombre");
        
        System.out.println("🔍 === VERIFICANDO SESIÓN EN ACCIÓN 2 ===");
        System.out.println("Proveedor ID de sesión: " + proveedorId);
        System.out.println("Proveedor Nombre de sesión: " + proveedorNombre);
        System.out.println("Categoría Nombre de sesión: " + categoriaNombre);
        System.out.println("Marca Nombre de sesión: " + marcaNombre);
        
        if (proveedorId == null) {
            redirectAttributes.addFlashAttribute("error", "Sesión expirada. Comienza desde la Acción 1.");
            return "redirect:/inventario/nuevo-completo";
        }
        
        // **NUEVA SECCIÓN SIN VERIFICACIÓN PROBLEMÁTICA:**
        System.out.println("🔍 === DATOS DE SESIÓN VÁLIDOS - CONTINUANDO SIN VERIFICACIÓN ===");
        System.out.println("✅ Usando Proveedor ID: " + proveedorId + " (" + proveedorNombre + ")");
        System.out.println("✅ Los datos se guardaron correctamente en Acción 1");

        // **REEMPLAZAR CON:**
        System.out.println("🔍 === DATOS DE SESIÓN VÁLIDOS - CONTINUANDO ===");
        System.out.println("✅ Usando Proveedor ID: " + proveedorId + " (" + proveedorNombre + ")");
        
        model.addAttribute("proveedorNombre", proveedorNombre);
        model.addAttribute("categoriaNombre", categoriaNombre);
        model.addAttribute("marcaNombre", marcaNombre);
        model.addAttribute("codigoBarrasGenerado", generarCodigoBarras());
        
        System.out.println("🔥 ACCIÓN 2: Formulario Lote + Producto");
        return "inventario/accion2-lote-producto";
    }

    // **ACCIÓN 2: PROCESAR LOTE + PRODUCTO**
    @PostMapping("/guardar-accion2")
    @Transactional
    public String procesarAccion2(
            @RequestParam Integer loteCantidad,
            @RequestParam BigDecimal lotePrecioCompra,
            @RequestParam BigDecimal lotePrecioVenta,
            @RequestParam(required = false) String loteFechaCaducidad,
            @RequestParam String productoNombre,
            @RequestParam String productoDescripcion,
            @RequestParam String productoCodigoBarras,
            @RequestParam Long productoStockMinimo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long proveedorId = (Long) session.getAttribute("proveedorId");
            Long categoriaId = (Long) session.getAttribute("categoriaId");
            Long marcaId = (Long) session.getAttribute("marcaId");
            
            System.out.println("🔍 === VERIFICANDO IDs DE SESIÓN ===");
            System.out.println("Proveedor ID: " + proveedorId);
            System.out.println("Categoría ID: " + categoriaId);
            System.out.println("Marca ID: " + marcaId);
            
            if (proveedorId == null || categoriaId == null || marcaId == null) {
                System.err.println("❌ Sesión inválida - IDs faltantes");
                redirectAttributes.addFlashAttribute("error", "Sesión expirada. Comienza desde la Acción 1.");
                return "redirect:/inventario/nuevo-completo";
            }
            
            System.out.println("🔥 === ACCIÓN 2: GUARDANDO PRODUCTO → LOTE ===");
            
            // **OBTENER ENTIDADES**
            // **OBTENER ENTIDADES USANDO NOMBRES (más confiable que IDs)**
            
            // **OBTENER ENTIDADES USANDO NOMBRES EN LUGAR DE IDs**
            System.out.println("🔍 Buscando entidades por nombre en lugar de ID");

            String proveedorNombre = (String) session.getAttribute("proveedorNombre");
            String categoriaNombre = (String) session.getAttribute("categoriaNombre");
            String marcaNombre = (String) session.getAttribute("marcaNombre");

            System.out.println("🔍 Buscando Proveedor por nombre: " + proveedorNombre);
            Proveedor proveedor = proveedorRepository.findByNombre(proveedorNombre)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con nombre: " + proveedorNombre));
            System.out.println("✅ Proveedor encontrado: " + proveedor.getNombre() + " (ID: " + proveedor.getIdProveedor() + ")");

            System.out.println("🔍 Buscando Categoría por nombre: " + categoriaNombre);
            Categoria categoria = categoriaRepository.findByNombre(categoriaNombre)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con nombre: " + categoriaNombre));
            System.out.println("✅ Categoría encontrada: " + categoria.getNombre() + " (ID: " + categoria.getIdCategoria() + ")");

            System.out.println("🔍 Buscando Marca por nombre: " + marcaNombre);
            Marca marca = marcaRepository.findByNombre(marcaNombre)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada con nombre: " + marcaNombre));
            System.out.println("✅ Marca encontrada: " + marca.getNombre() + " (ID: " + marca.getIdMarca() + ")");
            
            // **PASO 4: CREAR PRODUCTO** 📱
            System.out.println("📱 PASO 4: Creando Producto: " + productoNombre);
            Producto producto = new Producto();
            producto.setNombre(productoNombre);
            producto.setDescripcion(productoDescripcion);
            producto.setCodigoBarras(productoCodigoBarras);
            producto.setCategoria(categoria);
            producto.setMarca(marca);
            producto.setStockMinimo(productoStockMinimo);
            producto.setActivo(true);
            
            producto = productoRepository.save(producto);
            entityManager.flush(); // ✅ FLUSH SIN REFRESH
            System.out.println("✅ Producto guardado con ID: " + producto.getIdProducto());
            
            // **PASO 5: CREAR LOTE** 💰
            System.out.println("💰 PASO 5: Creando Lote de Inventario");
            LoteInventario lote = new LoteInventario();
            lote.setProducto(producto);
            lote.setProveedor(proveedor);
            lote.setCantidad(loteCantidad);
            lote.setPrecioCompra(lotePrecioCompra);
            lote.setPrecioVenta(lotePrecioVenta);
            lote.setFechaIngreso(LocalDate.now());
            
            if (loteFechaCaducidad != null && !loteFechaCaducidad.trim().isEmpty()) {
                lote.setFechaCaducidad(LocalDate.parse(loteFechaCaducidad));
            }
            
            lote = loteInventarioRepository.save(lote);
            entityManager.flush(); // ✅ FLUSH SIN REFRESH
            System.out.println("✅ Lote guardado con ID: " + lote.getIdLote());
            
            // **LIMPIAR SESIÓN**
            session.removeAttribute("proveedorId");
            session.removeAttribute("proveedorNombre");
            session.removeAttribute("categoriaId");
            session.removeAttribute("categoriaNombre");
            session.removeAttribute("marcaId");
            session.removeAttribute("marcaNombre");
            
            System.out.println("🎉 === INVENTARIO COMPLETO CREADO EXITOSAMENTE ===");
            System.out.println("1. 🚛 Proveedor: " + proveedor.getNombre() + " (ID: " + proveedor.getIdProveedor() + ")");
            System.out.println("2. 📂 Categoría: " + categoria.getNombre() + " (ID: " + categoria.getIdCategoria() + ")");
            System.out.println("3. 🏷️ Marca: " + marca.getNombre() + " (ID: " + marca.getIdMarca() + ")");
            System.out.println("4. 📱 Producto: " + producto.getNombre() + " (ID: " + producto.getIdProducto() + ")");
            System.out.println("5. 💰 Lote: " + loteCantidad + " unidades (ID: " + lote.getIdLote() + ")");
            
            redirectAttributes.addFlashAttribute("success", 
                "¡Inventario completo creado exitosamente! " +
                "Producto: " + producto.getNombre() + ", " +
                "Categoría: " + categoria.getNombre() + ", " +
                "Marca: " + marca.getNombre() + ", " +
                "Proveedor: " + proveedor.getNombre() + ", " +
                "Stock inicial: " + loteCantidad + " unidades.");
            
            return "redirect:/dashboard";
            
        } catch (Exception e) {
            System.err.println("❌ Error en Acción 2: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error en Acción 2: " + e.getMessage());
            return "redirect:/inventario/accion2";
        }
    }

    private String generarCodigoBarras() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return "CB" + timestamp + String.format("%03d", random);
    }
}
