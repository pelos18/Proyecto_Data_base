// Gestión de Productos JavaScript

let productos = []
let categorias = []
let marcas = []

// Inicialización
document.addEventListener("DOMContentLoaded", () => {
  initializeProductos()
  loadCategorias()
  loadMarcas()
  loadProductos()
})

// Inicializar módulo de productos
function initializeProductos() {
  setupProductoEventListeners()
  initializeDataTable()
}

// Configurar event listeners
function setupProductoEventListeners() {
  // Búsqueda de productos
  const searchInput = document.getElementById("buscarProducto")
  if (searchInput) {
    searchInput.addEventListener("input", debounce(filtrarProductos, 300))
  }

  // Filtros
  const filtros = ["filtroCategoria", "filtroMarca", "filtroStock"]
  filtros.forEach((filtroId) => {
    const filtro = document.getElementById(filtroId)
    if (filtro) {
      filtro.addEventListener("change", filtrarProductos)
    }
  })

  // Formulario nuevo producto
  const form = document.getElementById("nuevoProductoForm")
  if (form) {
    form.addEventListener("submit", handleSubmitProducto)
  }
}

// Inicializar DataTable
function initializeDataTable() {
  const table = document.getElementById("productosTable")
  if (table && typeof DataTable !== "undefined") {
    window.productosDataTable = new DataTable(table, {
      language: {
        url: "//cdn.datatables.net/plug-ins/1.13.7/i18n/es-ES.json",
      },
      responsive: true,
      pageLength: 25,
      order: [[0, "desc"]],
      columnDefs: [
        { targets: -1, orderable: false }, // Columna de acciones no ordenable
      ],
    })
  }
}

// Cargar productos
async function loadProductos() {
  try {
    showLoading()
    const response = await fetch("/api/productos")

    if (!response.ok) {
      throw new Error("Error al cargar productos")
    }

    productos = await response.json()
    renderProductos(productos)
    hideLoading()
  } catch (error) {
    console.error("Error:", error)
    showNotification("Error al cargar productos", "error")
    hideLoading()
  }
}

// Renderizar productos en la tabla
function renderProductos(productosData) {
  const tbody = document.querySelector("#productosTable tbody")
  if (!tbody) return

  tbody.innerHTML = ""

  productosData.forEach((producto) => {
    const row = createProductoRow(producto)
    tbody.appendChild(row)
  })

  // Actualizar DataTable si está inicializado
  if (window.productosDataTable) {
    window.productosDataTable.clear().rows.add(tbody.querySelectorAll("tr")).draw()
  }
}

// Crear fila de producto
function createProductoRow(producto) {
  const row = document.createElement("tr")

  const stockClass =
    producto.stockActual <= producto.stockMinimo
      ? "bg-warning"
      : producto.stockActual === 0
        ? "bg-danger"
        : "bg-success"

  const estadoClass = producto.activo ? "bg-success" : "bg-secondary"

  row.innerHTML = `
        <td>${producto.idProducto}</td>
        <td>${producto.codigoBarras || "N/A"}</td>
        <td>${producto.nombre}</td>
        <td>${producto.categoria?.nombre || "N/A"}</td>
        <td>${producto.marca?.nombre || "N/A"}</td>
        <td><span class="badge ${stockClass}">${producto.stockActual}</span></td>
        <td>${producto.stockMinimo}</td>
        <td>${formatCurrency(producto.precioVenta || 0)}</td>
        <td><span class="badge ${estadoClass}">${producto.activo ? "Activo" : "Inactivo"}</span></td>
        <td>
            <div class="btn-group" role="group">
                <button class="btn btn-sm btn-outline-primary" onclick="editarProducto(${producto.idProducto})" title="Editar">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-outline-info" onclick="verProducto(${producto.idProducto})" title="Ver">
                    <i class="fas fa-eye"></i>
                </button>
                <button class="btn btn-sm btn-outline-warning" onclick="gestionarStock(${producto.idProducto})" title="Stock">
                    <i class="fas fa-boxes"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" onclick="eliminarProducto(${producto.idProducto})" title="Eliminar">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </td>
    `

  return row
}

// Filtrar productos
function filtrarProductos() {
  const searchTerm = document.getElementById("buscarProducto")?.value.toLowerCase() || ""
  const categoriaFilter = document.getElementById("filtroCategoria")?.value || ""
  const marcaFilter = document.getElementById("filtroMarca")?.value || ""
  const stockFilter = document.getElementById("filtroStock")?.value || ""

  const productosFiltrados = productos.filter((producto) => {
    const matchesSearch =
      producto.nombre.toLowerCase().includes(searchTerm) ||
      (producto.codigoBarras && producto.codigoBarras.toLowerCase().includes(searchTerm))

    const matchesCategoria = !categoriaFilter || producto.categoria?.idCategoria == categoriaFilter
    const matchesMarca = !marcaFilter || producto.marca?.idMarca == marcaFilter

    let matchesStock = true
    if (stockFilter === "bajo") {
      matchesStock = producto.stockActual <= producto.stockMinimo
    } else if (stockFilter === "sin") {
      matchesStock = producto.stockActual === 0
    }

    return matchesSearch && matchesCategoria && matchesMarca && matchesStock
  })

  renderProductos(productosFiltrados)
}

// Cargar categorías
async function loadCategorias() {
  try {
    const response = await fetch("/api/categorias")
    if (response.ok) {
      categorias = await response.json()
      populateSelect("categoria", categorias, "idCategoria", "nombre")
      populateSelect("filtroCategoria", categorias, "idCategoria", "nombre")
    }
  } catch (error) {
    console.error("Error cargando categorías:", error)
  }
}

// Cargar marcas
async function loadMarcas() {
  try {
    const response = await fetch("/api/marcas")
    if (response.ok) {
      marcas = await response.json()
      populateSelect("marca", marcas, "idMarca", "nombre")
      populateSelect("filtroMarca", marcas, "idMarca", "nombre")
    }
  } catch (error) {
    console.error("Error cargando marcas:", error)
  }
}

// Poblar select con opciones
function populateSelect(selectId, data, valueField, textField) {
  const select = document.getElementById(selectId)
  if (!select) return

  // Mantener la primera opción (placeholder)
  const firstOption = select.querySelector('option[value=""]')
  select.innerHTML = ""

  if (firstOption) {
    select.appendChild(firstOption)
  }

  data.forEach((item) => {
    const option = document.createElement("option")
    option.value = item[valueField]
    option.textContent = item[textField]
    select.appendChild(option)
  })
}

// Manejar envío del formulario
function handleSubmitProducto(event) {
  event.preventDefault()

  if (!validateForm("nuevoProductoForm")) {
    showNotification("Por favor complete todos los campos requeridos", "warning")
    return
  }

  const formData = new FormData(event.target)
  const producto = Object.fromEntries(formData.entries())

  guardarProducto(producto)
}

// Guardar producto
async function guardarProducto(producto) {
  try {
    showLoading()

    const response = await fetch("/api/productos", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(producto),
    })

    if (!response.ok) {
      throw new Error("Error al guardar producto")
    }

    const nuevoProducto = await response.json()

    showNotification("Producto guardado exitosamente", "success")
    closeModal("nuevoProductoModal")
    clearForm("nuevoProductoForm")
    loadProductos() // Recargar lista

    hideLoading()
  } catch (error) {
    console.error("Error:", error)
    showNotification("Error al guardar producto", "error")
    hideLoading()
  }
}

// Editar producto
function editarProducto(id) {
  const producto = productos.find((p) => p.idProducto === id)
  if (!producto) {
    showNotification("Producto no encontrado", "error")
    return
  }

  // Llenar formulario con datos del producto
  fillForm("nuevoProductoForm", producto)

  // Cambiar título del modal
  const modalTitle = document.querySelector("#nuevoProductoModal .modal-title")
  if (modalTitle) {
    modalTitle.textContent = "Editar Producto"
  }

  // Cambiar texto del botón
  const submitBtn = document.querySelector("#nuevoProductoModal .btn-primary")
  if (submitBtn) {
    submitBtn.textContent = "Actualizar Producto"
    submitBtn.onclick = () => actualizarProducto(id)
  }

  // Mostrar modal
  showModal("nuevoProductoModal")
}

// Actualizar producto
async function actualizarProducto(id) {
  if (!validateForm("nuevoProductoForm")) {
    showNotification("Por favor complete todos los campos requeridos", "warning")
    return
  }

  const formData = new FormData(document.getElementById("nuevoProductoForm"))
  const producto = Object.fromEntries(formData.entries())
  producto.idProducto = id

  try {
    showLoading()

    const response = await fetch(`/api/productos/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(producto),
    })

    if (!response.ok) {
      throw new Error("Error al actualizar producto")
    }

    showNotification("Producto actualizado exitosamente", "success")
    closeModal("nuevoProductoModal")
    resetForm()
    loadProductos()

    hideLoading()
  } catch (error) {
    console.error("Error:", error)
    showNotification("Error al actualizar producto", "error")
    hideLoading()
  }
}

// Ver producto
function verProducto(id) {
  const producto = productos.find((p) => p.idProducto === id)
  if (!producto) {
    showNotification("Producto no encontrado", "error")
    return
  }

  // Crear modal de vista
  const modalContent = `
        <div class="modal fade" id="verProductoModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Detalles del Producto</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>ID:</strong> ${producto.idProducto}</p>
                                <p><strong>Nombre:</strong> ${producto.nombre}</p>
                                <p><strong>Código de Barras:</strong> ${producto.codigoBarras || "N/A"}</p>
                                <p><strong>Categoría:</strong> ${producto.categoria?.nombre || "N/A"}</p>
                                <p><strong>Marca:</strong> ${producto.marca?.nombre || "N/A"}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Stock Actual:</strong> ${producto.stockActual}</p>
                                <p><strong>Stock Mínimo:</strong> ${producto.stockMinimo}</p>
                                <p><strong>Precio:</strong> ${formatCurrency(producto.precioVenta || 0)}</p>
                                <p><strong>Estado:</strong> ${producto.activo ? "Activo" : "Inactivo"}</p>
                            </div>
                        </div>
                        ${producto.descripcion ? `<div class="row"><div class="col-12"><p><strong>Descripción:</strong></p><p>${producto.descripcion}</p></div></div>` : ""}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                        <button type="button" class="btn btn-primary" onclick="editarProducto(${producto.idProducto}); closeModal('verProductoModal');">Editar</button>
                    </div>
                </div>
            </div>
        </div>
    `

  // Remover modal anterior si existe
  const existingModal = document.getElementById("verProductoModal")
  if (existingModal) {
    existingModal.remove()
  }

  // Agregar nuevo modal
  document.body.insertAdjacentHTML("beforeend", modalContent)
  showModal("verProductoModal")
}

// Gestionar stock
function gestionarStock(id) {
  const producto = productos.find((p) => p.idProducto === id)
  if (!producto) {
    showNotification("Producto no encontrado", "error")
    return
  }

  const nuevoStock = prompt(`Stock actual: ${producto.stockActual}\nIngrese el nuevo stock:`, producto.stockActual)

  if (nuevoStock !== null && !isNaN(nuevoStock) && nuevoStock >= 0) {
    actualizarStock(id, Number.parseInt(nuevoStock))
  }
}

// Actualizar stock
async function actualizarStock(id, nuevoStock) {
  try {
    showLoading()

    const response = await fetch(`/api/productos/${id}/stock`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ stock: nuevoStock }),
    })

    if (!response.ok) {
      throw new Error("Error al actualizar stock")
    }

    showNotification("Stock actualizado exitosamente", "success")
    loadProductos()

    hideLoading()
  } catch (error) {
    console.error("Error:", error)
    showNotification("Error al actualizar stock", "error")
    hideLoading()
  }
}

// Eliminar producto
function eliminarProducto(id) {
  const producto = productos.find((p) => p.idProducto === id)
  if (!producto) {
    showNotification("Producto no encontrado", "error")
    return
  }

  if (confirm(`¿Está seguro de que desea eliminar el producto "${producto.nombre}"?`)) {
    eliminarProductoConfirmado(id)
  }
}

// Eliminar producto confirmado
async function eliminarProductoConfirmado(id) {
  try {
    showLoading()

    const response = await fetch(`/api/productos/${id}`, {
      method: "DELETE",
    })

    if (!response.ok) {
      throw new Error("Error al eliminar producto")
    }

    showNotification("Producto eliminado exitosamente", "success")
    loadProductos()

    hideLoading()
  } catch (error) {
    console.error("Error:", error)
    showNotification("Error al eliminar producto", "error")
    hideLoading()
  }
}

// Funciones de utilidad
function fillForm(formId, data) {
  const form = document.getElementById(formId)
  if (!form) return

  Object.keys(data).forEach((key) => {
    const input = form.querySelector(`[name="${key}"]`)
    if (input) {
      input.value = data[key] || ""
    }
  })
}

function resetForm() {
  // Restaurar título y botón del modal
  const modalTitle = document.querySelector("#nuevoProductoModal .modal-title")
  if (modalTitle) {
    modalTitle.textContent = "Nuevo Producto"
  }

  const submitBtn = document.querySelector("#nuevoProductoModal .btn-primary")
  if (submitBtn) {
    submitBtn.textContent = "Guardar Producto"
    submitBtn.onclick = null
  }

  clearForm("nuevoProductoForm")
}

function showModal(modalId) {
  const modal = new bootstrap.Modal(document.getElementById(modalId))
  modal.show()
}

function closeModal(modalId) {
  const modal = bootstrap.Modal.getInstance(document.getElementById(modalId))
  if (modal) {
    modal.hide()
  }
}

// Funciones auxiliares del dashboard
function showLoading() {
  // Implementar loading spinner
}

function hideLoading() {
  // Ocultar loading spinner
}

function showNotification(message, type) {
  if (window.dashboard && window.dashboard.showNotification) {
    window.dashboard.showNotification(message, type)
  } else {
    alert(message)
  }
}

function validateForm(formId) {
  if (window.dashboard && window.dashboard.validateForm) {
    return window.dashboard.validateForm(formId)
  }
  return true
}

function clearForm(formId) {
  if (window.dashboard && window.dashboard.clearForm) {
    window.dashboard.clearForm(formId)
  }
}

function formatCurrency(amount) {
  if (window.dashboard && window.dashboard.formatCurrency) {
    return window.dashboard.formatCurrency(amount)
  }
  return `$${amount.toFixed(2)}`
}

function debounce(func, wait) {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}
