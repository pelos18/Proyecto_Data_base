// Funciones JavaScript para el dashboard del administrador

// Cargar categorías en el dashboard de cliente
function cargarCategorias() {
  fetch("/api/categorias")
    .then((response) => response.json())
    .then((categorias) => {
      const select = document.getElementById("filtroCategoria")
      if (select) {
        categorias.forEach((categoria) => {
          const option = document.createElement("option")
          option.value = categoria.idCategoria
          option.textContent = categoria.nombre
          select.appendChild(option)
        })
      }
    })
    .catch((error) => console.error("Error cargando categorías:", error))
}

// Cargar marcas en el dashboard de cliente
function cargarMarcas() {
  fetch("/api/marcas")
    .then((response) => response.json())
    .then((marcas) => {
      const select = document.getElementById("filtroMarca")
      if (select) {
        marcas.forEach((marca) => {
          const option = document.createElement("option")
          option.value = marca.idMarca
          option.textContent = marca.nombre
          select.appendChild(option)
        })
      }
    })
    .catch((error) => console.error("Error cargando marcas:", error))
}

// Función para buscar productos con filtros
function buscarProductos() {
  const termino = document.getElementById("buscarProducto")?.value || ""
  const categoria = document.getElementById("filtroCategoria")?.value || ""
  const marca = document.getElementById("filtroMarca")?.value || ""

  let url = "/api/productos/disponibles"
  const params = new URLSearchParams()

  if (termino) params.append("q", termino)
  if (categoria) params.append("categoria", categoria)
  if (marca) params.append("marca", marca)

  if (params.toString()) {
    url += "?" + params.toString()
  }

  fetch(url)
    .then((response) => response.json())
    .then((productos) => {
      if (typeof mostrarProductos === "function") {
        mostrarProductos(productos)
      }
    })
    .catch((error) => console.error("Error:", error))
}

// Funciones para modales de administrador
function nuevoProducto() {
  window.location.href = "/api/productos/nuevo"
}

function editarProducto(id) {
  window.location.href = `/api/productos/editar/${id}`
}

function eliminarProducto(id) {
  if (confirm("¿Estás seguro de eliminar este producto?")) {
    fetch(`/api/productos/eliminar/${id}`, {
      method: "POST",
    })
      .then((response) => {
        if (response.ok) {
          alert("Producto eliminado exitosamente")
          cargarProductos()
        } else {
          alert("Error al eliminar producto")
        }
      })
      .catch((error) => {
        console.error("Error:", error)
        alert("Error al eliminar producto")
      })
  }
}

function nuevoLote() {
  // Aquí se abriría un modal para crear nuevo lote
  alert("Funcionalidad de nuevo lote - implementar según necesidades")
}

function cargarInventario() {
  fetch("/api/admin/inventario-alertas")
    .then((response) => response.json())
    .then((data) => {
      document.getElementById("productosVencer").textContent = data.productosVencer || 0
      document.getElementById("stockMinimo").textContent = data.stockMinimo || 0
      document.getElementById("lotesActivos").textContent = data.lotesActivos || 0
    })
    .catch((error) => console.error("Error:", error))
}

// Función para buscar venta anterior
function buscarVentaAnterior() {
  const modal = new bootstrap.Modal(document.getElementById("buscarVentaModal"))
  modal.show()
}

function procesarDevolucion() {
  alert("Funcionalidad de devolución - implementar según políticas de la tienda")
}

function verReporteDiario() {
  fetch("/api/admin/reporte-ventas")
    .then((response) => response.json())
    .then((data) => {
      alert(`Reporte del día:\nVentas: $${data.ventasHoy}\nTransacciones: ${data.totalTransacciones}`)
    })
    .catch((error) => console.error("Error:", error))
}
