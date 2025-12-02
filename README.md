Bici-Reparo App

Bici-Reparo es una aplicación móvil nativa para Android desarrollada en Kotlin, diseñada para gestionar solicitudes de reparación de bicicletas.

Este proyecto implementa una arquitectura moderna basada en Microservicios y MVVM, permitiendo una clara separación de responsabilidades entre el cliente móvil y el backend.

📱 Características Principales

Para Clientes:

Registro e Inicio de Sesión: Autenticación segura conectada a una base de datos en la nube.

Catálogo de Servicios: Visualización de servicios de reparación disponibles con precios actualizados.

Carrito de Compras: Selección de múltiples servicios y cálculo de total.

Solicitudes Inteligentes:

📸 Cámara: Captura de fotos del problema mecánico.

📍 GPS: Geolocalización automática para el retiro de la bicicleta.

Conversión de Moneda: Visualización del precio total en USD mediante consumo de API externa.

Historial: Visualización de solicitudes anteriores filtradas por usuario.

Para Administradores:

Panel de Control Exclusivo: Acceso diferenciado por roles.

Gestión de Usuarios (CRUD): Ver, editar y eliminar usuarios registrados.

Gestión de Servicios (CRUD): Crear y modificar el catálogo de reparaciones.

Visión Global: Acceso a todas las solicitudes de reparación del sistema.

🛠️ Arquitectura y Tecnologías

El proyecto sigue una arquitectura de Microservicios con un cliente Android robusto.

Frontend (Android)

Lenguaje: Kotlin.

Patrón de Diseño: MVVM (Model-View-ViewModel).

Interfaz de Usuario: XML Layouts, Material Design Components.

Red y Datos:

Retrofit: Para consumo de APIs REST (Backend propio y APIs externas).

Corrutinas: Para manejo eficiente de hilos y tareas asíncronas.

ViewBinding: Para una interacción segura con la vista.

Recursos Nativos:

CameraX / Intent Camera: Captura de imágenes.

FusedLocationProvider: Geolocalización GPS.

Backend (Microservicios)

El sistema se apoya en dos microservicios independientes desarrollados en Spring Boot (Kotlin):

User Service (Puerto 8080):

Gestión de autenticación y perfiles de usuario.

Base de Datos

Supabase (PostgreSQL): Base de datos relacional en la nube para persistencia de usuarios y servicios.

🚀 Instalación y Ejecución

Requisitos Previos

Android Studio Iguana o superior.

JDK 17.

Conexión a Internet (para Supabase y APIs).

Pasos

Clonar el Repositorio:

git clone [https://github.com/tu-usuario/BiciReparoApp.git](https://github.com/tu-usuario/BiciReparoApp.git)


Configurar Backend:

Asegúrate de tener el microservicio (userservice) corriendo en tu red local.

Actualiza la IP en network/RetrofitClient.kt con la dirección de tu máquina (ej. 192.168.1.X).

Compilar en Android Studio:

Abre el proyecto.

Sincroniza con Gradle.

Ejecuta en un emulador o dispositivo físico.

🧪 Testing

El proyecto incluye una suite de pruebas unitarias con una cobertura de lógica de negocio superior al 80%.

Tecnologías: JUnit 4, Mockito, Kotlinx-Coroutines-Test.

Ejecución:

Desde Android Studio: Clic derecho en la carpeta test -> "Run Tests".

👥 Autores: Matias Molina Carlos Caceres

Proyecto desarrollado para la asignatura de Desarrollo de Aplicaciones Móviles (DSY1105) - Duoc UC.
