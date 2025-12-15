# [LevelUpApp]

**Asignatura:** Desarrollo de Aplicaciones Móviles  
**Sección:** [DSY1105]  
**Semestre:** 2025-1

---

## Integrantes del Equipo
* **[Sebastian Navarrete]**
* **[Rodrigo Gallardo]** 
* **[Sebastian Rojas]**

---

## Descripción del Proyecto
LevelUp Store es una aplicación móvil nativa desarrollada en **Kotlin** con **Jetpack Compose**, diseñada para gestionar la venta de productos tecnológicos. 

El proyecto implementa una arquitectura moderna con **Microservicios en Spring Boot**, base de datos **Oracle Cloud** y autenticación segura mediante **Firebase Auth**.

###Tecnologías Utilizadas
* **Lenguaje:** Kotlin (Android Nativo)
* **UI:** Jetpack Compose (Material Design 3)
* **Arquitectura:** MVVM (Model-View-ViewModel) + Clean Architecture
* **Backend:** Spring Boot (Java 17) + Oracle Database
* **Seguridad:** Firebase Authentication (Tokens JWT)
* **Red:** Retrofit 2 + OkHttp (Interceptors)
* **Imágenes:** Base64 y Coil para carga asíncrona
* **Persistencia Local:** DataStore (para sesión)

---

## Funcionalidades Principales
1.  **Autenticación Híbrida:** Login y Registro con Firebase Auth + Sincronización automática con Oracle DB.
2.  **Catálogo Dinámico:** Visualización de productos con imágenes reales, precios y stock en tiempo real.
3.  **Carrito de Compras:** Lógica de negocio local para agregar, sumar totales y validar stock.
4.  **Gestión de Ventas:** Confirmación de compra que descuenta stock en el Backend y guarda el historial.
5.  **Panel de Administración:** Permite crear y editar productos (CRUD) con validación de roles.
6.  **Búsqueda Inteligente:** Filtros por categoría y búsqueda por nombre/código.
7.  **Recursos Nativos:** Uso de Cámara (para perfil) y Ubicación (GPS para despacho).

---

###. Microservicios Propios (Spring Boot)
Base URL: `http://161.153.201.46:8080/api/v1` (Oracle Cloud)

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **POST** | `/auth/sync` | Sincroniza usuario Firebase -> Oracle |
| **GET** | `/products` | Obtiene el catálogo completo |
| **GET** | `/products/{code}` | Busca producto por código SKU |
| **POST** | `/products` | Crea un nuevo producto (Admin) |
| **PUT** | `/products/{id}` | Actualiza stock/precio (Admin) |
| **POST** | `/sales` | Registra una nueva venta |
| **GET** | `/sales/history` | Obtiene el historial del usuario |

---

## Instrucciones de Ejecución

### Requisitos Previos
* Android Studio Koala o superior.
* JDK 17 configurado.
* Dispositivo Android (Físico o Emulador) con API 26+.

### Pasos para compilar
1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/](https://github.com/)[ragallardom]/LevelUpApp.git
    ```
2.  **Abrir en Android Studio:** Esperar a que Gradle sincronice las dependencias.
3.  **Configurar `local.properties` (Opcional):** Si usas variables de entorno para las API Keys.
4.  **Ejecutar:** Seleccionar el dispositivo y presionar `Run` (▶️).

> **Nota:** La aplicación se conecta por defecto a la IP de la Nube Oracle. Si desea probar en local, cambie la `BASE_URL` en `RetrofitClient.kt`.

---

## APK Firmado
El archivo instalable (`app-release.apk`) se encuentra disponible en la carpeta raíz del repositorio o en la ruta:
`app/release/app-release.apk`

* **Keystore:** Ubicada en `keystore/levelup-key.jks`
* **Alias:** `levelup`
* **Password:** (Solicitar al equipo por seguridad)

---

## Evidencia de Pruebas
El proyecto incluye pruebas unitarias funcionales:
* `CarritoViewModelTest`: Valida cálculos de totales.
* `ProductsViewModelTest`: Simula respuestas de la API.
* `HomeScreenTest`: Valida la UI de navegación.

Para ejecutarlas: `./gradlew testDebugUnitTest`

---
**© 2025 LevelUp Team - Duoc UC**