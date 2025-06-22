import { Chart } from "@/components/ui/chart"
// Dashboard JavaScript Functions

// Variables globales
const notificationCount = 0
const currentUser = null

// Inicialización cuando el DOM está listo
document.addEventListener("DOMContentLoaded", () => {
  console.log("Dashboard cargado correctamente")

  // Actualizar fecha y hora cada minuto
  setInterval(() => {
    const now = new Date()
    const dateString = now.toLocaleDateString("es-ES") + " " + now.toLocaleTimeString("es-ES")
    console.log("Fecha actual:", dateString)
  }, 60000)
  initializeDashboard()
  loadNotifications()
  startRealTimeUpdates()
})

// Inicializar dashboard
function initializeDashboard() {
  // Actualizar fecha y hora
  updateDateTime()
  setInterval(updateDateTime, 1000)

  // Inicializar tooltips
  initializeTooltips()

  // Configurar eventos
  setupEventListeners()

  // Cargar datos iniciales
  loadDashboardData()
}

// Actualizar fecha y hora
function updateDateTime() {
  const now = new Date()
  const timeElement = document.getElementById("current-time")
  const dateElement = document.getElementById("current-date")

  if (timeElement) {
    timeElement.textContent = now.toLocaleTimeString("es-ES")
  }

  if (dateElement) {
    dateElement.textContent = now.toLocaleDateString("es-ES", {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }
}

// Inicializar tooltips de Bootstrap
function initializeTooltips() {
  const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
  tooltipTriggerList.map((tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl))
}

// Configurar event listeners
function setupEventListeners() {
  // Búsqueda en tiempo real
  const searchInputs = document.querySelectorAll('input[type="search"], input[id*="buscar"]')
  searchInputs.forEach((input) => {
    input.addEventListener("input", debounce(handleSearch, 300))
  })

  // Filtros
  const filterSelects = document.querySelectorAll('select[id*="filtro"]')
  filterSelects.forEach((select) => {
    select.addEventListener("change", handleFilter)
  })

  // Sidebar toggle para móvil
  const sidebarToggle = document.querySelector(".navbar-toggler")
  if (sidebarToggle) {
    sidebarToggle.addEventListener("click", toggleSidebar)
  }
}

// Función debounce para optimizar búsquedas
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

// Manejar búsqueda
function handleSearch(event) {
  const searchTerm = event.target.value.toLowerCase()
  const tableId = event.target.getAttribute("data-table") || "dataTable"
  const table = document.getElementById(tableId)

  if (table) {
    const rows = table.querySelectorAll("tbody tr")
    rows.forEach((row) => {
      const text = row.textContent.toLowerCase()
      row.style.display = text.includes(searchTerm) ? "" : "none"
    })
  }
}

// Manejar filtros
function handleFilter(event) {
  const filterValue = event.target.value
  const filterType = event.target.id

  // Implementar lógica de filtrado específica
  console.log(`Filtro ${filterType}: ${filterValue}`)
}

// Toggle sidebar para móvil
function toggleSidebar() {
  const sidebar = document.querySelector(".sidebar")
  if (sidebar) {
    sidebar.classList.toggle("show")
  }
}

// Cargar datos del dashboard
async function loadDashboardData() {
  try {
    showLoading()

    // Simular carga de datos (reemplazar con llamadas reales a la API)
    const data = await fetchDashboardData()

    updateDashboardStats(data)
    updateCharts(data)

    hideLoading()
  } catch (error) {
    console.error("Error cargando datos del dashboard:", error)
    showNotification("Error al cargar los datos del dashboard", "error")
    hideLoading()
  }
}

// Simular fetch de datos (reemplazar con llamada real)
async function fetchDashboardData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        totalProductos: 150,
        ventasHoy: 25,
        ingresosDiarios: 2450.0,
        stockBajo: 8,
        ventasSemanales: [12, 19, 15, 25, 22, 18, 30],
        productosStockBajo: [
          { nombre: "Producto A", stock: 2, minimo: 10 },
          { nombre: "Producto B", stock: 1, minimo: 5 },
        ],
      })
    }, 1000)
  })
}

// Actualizar estadísticas del dashboard
function updateDashboardStats(data) {
  const stats = {
    totalProductos: data.totalProductos,
    ventasHoy: data.ventasHoy,
    ingresosDiarios: `$${data.ingresosDiarios.toFixed(2)}`,
    stockBajo: data.stockBajo,
  }

  Object.entries(stats).forEach(([key, value]) => {
    const element = document.querySelector(`[data-stat="${key}"]`)
    if (element) {
      animateNumber(element, value)
    }
  })
}

// Animar números
function animateNumber(element, targetValue) {
  const isNumber = !isNaN(targetValue)
  if (!isNumber) {
    element.textContent = targetValue
    return
  }

  const startValue = 0
  const duration = 1000
  const startTime = performance.now()

  function updateNumber(currentTime) {
    const elapsed = currentTime - startTime
    const progress = Math.min(elapsed / duration, 1)

    const currentValue = Math.floor(startValue + (targetValue - startValue) * progress)
    element.textContent = currentValue

    if (progress < 1) {
      requestAnimationFrame(updateNumber)
    }
  }

  requestAnimationFrame(updateNumber)
}

// Actualizar gráficos
function updateCharts(data) {
  // Actualizar gráfico de ventas si existe
  if (typeof Chart !== "undefined" && window.ventasChart) {
    window.ventasChart.data.datasets[0].data = data.ventasSemanales
    window.ventasChart.update()
  }
}

// Cargar notificaciones
async function loadNotifications() {
  try {
    const notifications = await fetchNotifications()
    updateNotificationBadge(notifications.length)
    renderNotifications(notifications)
  } catch (error) {
    console.error("Error cargando notificaciones:", error)
  }
}

// Simular fetch de notificaciones
async function fetchNotifications() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve([
        {
          id: 1,
          type: "warning",
          title: "Stock Bajo",
          message: "Producto XYZ tiene stock bajo",
          time: "5 min ago",
        },
        {
          id: 2,
          type: "info",
          title: "Nueva Venta",
          message: "Venta #001 procesada exitosamente",
          time: "10 min ago",
        },
      ])
    }, 500)
  })
}

// Actualizar badge de notificaciones
function updateNotificationBadge(count) {
  const badge = document.getElementById("notification-count")
  if (badge) {
    badge.textContent = count
    badge.style.display = count > 0 ? "block" : "none"
  }
}

// Renderizar notificaciones
function renderNotifications(notifications) {
  const container = document.querySelector("#notificationsDropdown + .dropdown-menu")
  if (!container) return

  const notificationItems = notifications
    .map(
      (notification) => `
        <li>
            <a class="dropdown-item" href="#" onclick="markAsRead(${notification.id})">
                <div class="d-flex">
                    <div class="flex-shrink-0">
                        <i class="fas fa-${getNotificationIcon(notification.type)} text-${notification.type}"></i>
                    </div>
                    <div class="flex-grow-1 ms-3">
                        <div class="fw-bold">${notification.title}</div>
                        <div class="small text-muted">${notification.message}</div>
                        <div class="small text-muted">${notification.time}</div>
                    </div>
                </div>
            </a>
        </li>
    `,
    )
    .join("")

  container.innerHTML = `
        <li><h6 class="dropdown-header">Notificaciones</h6></li>
        <li><hr class="dropdown-divider"></li>
        ${notificationItems}
        <li><hr class="dropdown-divider"></li>
        <li><a class="dropdown-item text-center" href="#">Ver todas las notificaciones</a></li>
    `
}

// Obtener icono de notificación
function getNotificationIcon(type) {
  const icons = {
    warning: "exclamation-triangle",
    info: "info-circle",
    success: "check-circle",
    error: "times-circle",
  }
  return icons[type] || "bell"
}

// Marcar notificación como leída
function markAsRead(notificationId) {
  console.log(`Marcando notificación ${notificationId} como leída`)
  // Implementar llamada a la API
}

// Mostrar notificación toast
function showNotification(message, type = "info", duration = 5000) {
  const toastContainer = getOrCreateToastContainer()
  const toastId = "toast-" + Date.now()

  const toast = document.createElement("div")
  toast.id = toastId
  toast.className = `toast align-items-center text-white bg-${type} border-0`
  toast.setAttribute("role", "alert")
  toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                <i class="fas fa-${getNotificationIcon(type)} me-2"></i>
                ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `

  toastContainer.appendChild(toast)

  const bsToast = new bootstrap.Toast(toast, {
    autohide: true,
    delay: duration,
  })

  bsToast.show()

  // Remover el toast del DOM después de que se oculte
  toast.addEventListener("hidden.bs.toast", () => {
    toast.remove()
  })
}

// Obtener o crear contenedor de toasts
function getOrCreateToastContainer() {
  let container = document.getElementById("toast-container")
  if (!container) {
    container = document.createElement("div")
    container.id = "toast-container"
    container.className = "toast-container position-fixed top-0 end-0 p-3"
    container.style.zIndex = "9999"
    document.body.appendChild(container)
  }
  return container
}

// Mostrar loading
function showLoading() {
  const loading = document.getElementById("loading")
  if (loading) {
    loading.style.display = "flex"
  }
}

// Ocultar loading
function hideLoading() {
  const loading = document.getElementById("loading")
  if (loading) {
    loading.style.display = "none"
  }
}

// Iniciar actualizaciones en tiempo real
function startRealTimeUpdates() {
  // Actualizar cada 30 segundos
  setInterval(() => {
    loadNotifications()
    // Actualizar otras métricas si es necesario
  }, 30000)
}

// Funciones de utilidad para formularios
function validateForm(formId) {
  const form = document.getElementById(formId)
  if (!form) return false

  const inputs = form.querySelectorAll("input[required], select[required], textarea[required]")
  let isValid = true

  inputs.forEach((input) => {
    if (!input.value.trim()) {
      input.classList.add("is-invalid")
      isValid = false
    } else {
      input.classList.remove("is-invalid")
      input.classList.add("is-valid")
    }
  })

  return isValid
}

// Limpiar formulario
function clearForm(formId) {
  const form = document.getElementById(formId)
  if (form) {
    form.reset()
    form.querySelectorAll(".is-valid, .is-invalid").forEach((input) => {
      input.classList.remove("is-valid", "is-invalid")
    })
  }
}

// Formatear moneda
function formatCurrency(amount) {
  return new Intl.NumberFormat("es-MX", {
    style: "currency",
    currency: "MXN",
  }).format(amount)
}

// Formatear fecha
function formatDate(date) {
  return new Intl.DateTimeFormat("es-MX", {
    year: "numeric",
    month: "long",
    day: "numeric",
  }).format(new Date(date))
}

// Exportar funciones para uso global
window.dashboard = {
  showNotification,
  validateForm,
  clearForm,
  formatCurrency,
  formatDate,
  loadDashboardData,
  toggleSidebar,
}
