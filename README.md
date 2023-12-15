# MicroservicesComplete

MicroservicesComplete es una aplicación basada en la arquitectura de microservicios, construida con Spring Boot 3.2 y Java 17. Este sistema multimódulo Maven está diseñado para la gestión eficiente de productos, pedidos e inventario.

## Arquitectura del Proyecto

El sistema se compone de varios microservicios independientes, cada uno con su propia base de datos y conjunto de responsabilidades:

- **Product Microservice**: Gestiona información detallada de productos y su almacenamiento en una base de datos PostgreSQL.
- **Orders Microservice**: Responsable de la creación y gestión de pedidos de los clientes. Utiliza MySQL como su sistema de almacenamiento.
- **Inventory Microservice**: Mantiene el seguimiento del stock de productos disponibles, también apoyado por una base de datos PostgreSQL.

Estos microservicios se comunican entre sí de la siguiente manera:

- **Sincronización**: Utilizando WebClient (WebFlux), permitiendo llamadas HTTP reactivas y no bloqueantes.
- **Asíncrona**: A través de Kafka, que facilita la comunicación basada en eventos sin necesidad de respuesta inmediata.

### Componentes de Soporte

Además de los microservicios centrales, el sistema se complementa con una serie de herramientas de soporte para autenticación, descubrimiento de servicios, trazabilidad y monitoreo:

- **Spring Cloud Gateway**: Actúa como puerta de entrada única a los microservicios, proporcionando un punto de acceso y enrutamiento coherente.
- **Keycloak**: Autentica y autoriza a los usuarios a través de un servidor de autenticación dedicado.
- **Netflix Eureka**: Utilizado para el descubrimiento de servicios, permitiendo que los microservicios se encuentren y comuniquen entre sí de forma dinámica.
- **Zipkin**: Ofrece trazabilidad distribuida para monitorear y correlacionar las llamadas entre los microservicios.
- **Prometheus y Grafana**: Se utilizan para monitorear los microservicios y visualizar métricas, respectivamente.

### Infraestructura como Código

El despliegue y la configuración de estos componentes están definidos en el archivo `docker-compose.yml`, lo que permite una implementación reproducible y escalable.

### Seguridad

Todos los endpoints están protegidos por JWT, con tokens emitidos y validados por Keycloak, asegurando que solo los usuarios autenticados y autorizados puedan acceder a los recursos.

## Diseño del Sistema

La comunicación entre los microservicios y el gateway se gestiona a través de Eureka, con el gateway actuando como un intermediario que dirige las solicitudes entrantes a los servicios correspondientes.

El servicio de pedidos implementa patrones de resiliencia como Circuit Breaker utilizando Resilience4j, para manejar fallas y evitar que se propaguen en el sistema.

La mensajería asíncrona se gestiona mediante Kafka, lo que permite la comunicación de eventos entre el servicio de pedidos y el servicio de notificaciones.

Docker y Docker Compose son utilizados para contenerizar y orquestar todos los servicios y dependencias, permitiendo una fácil instalación y despliegue.



---

## Instalación

Para instalar y ejecutar este proyecto, necesitará Java 17 y Maven instalados en su sistema. Además, necesitará Docker y Docker Compose para los servicios de infraestructura.

Clone el repositorio:

```bash
git clone https://github.com/agcadu/MicroservicesComplete.git
```

Cambie al directorio del proyecto:

```bash
cd MicroservicesComplete
```
Ejecute el siguiente comando para iniciar todos los servicios con Docker Compose:

```bash
docker-compose up
```

## Servicios de Docker Compose

Al ejecutar `docker-compose up`, se desplegarán los siguientes servicios:

- **Bases de Datos**: PostgreSQL para los servicios de productos e inventario, MySQL para el servicio de pedidos.
- **Keycloak**: Servidor de autenticación para la gestión de usuarios y autorizaciones.
- **Kafka**: Sistema de mensajería para la comunicación asíncrona entre servicios.
- **Zipkin**: Herramienta de seguimiento distribuido para monitorear y resolver problemas en las llamadas entre servicios.
- **Prometheus y Grafana**: Soluciones de monitoreo y visualización para observar el rendimiento de los microservicios.

## Endpoints de Prueba

La aplicación se puede probar utilizando los siguientes endpoints a través de Postman o cualquier cliente HTTP:

### Servicio de Productos

- `POST http://localhost:8081/api/products/add`: Añadir un nuevo producto.
- `GET http://localhost:8080/api/products/all`: Listar todos los productos.

### Servicio de Pedidos

- `POST http://localhost:8080/api/order/place`: Realizar un nuevo pedido.
- `GET http://localhost:8080/api/order/all`: Listar todos los pedidos.

### Servicio de Inventario

- `GET http://localhost:8080/api/inventory/{sku}`: Verificar si un producto está en stock.

### Actuator Endpoints

- `GET http://localhost:8080/actuator/product/health`: Estado del servicio de productos.
- `GET http://localhost:8080/actuator/order/health`: Estado del servicio de pedidos.
- `GET http://localhost:8080/actuator/inventory/health`: Estado del servicio de inventario.

## Colección Postman

Puede importar la colección de Postman para probar los endpoints llamada `Microservices.postman_collection.json`


## Bases de Datos

Los detalles de conexión para las bases de datos son los siguientes:

- **Inventory DB (PostgreSQL)**: `localhost:5432/inventory`
- **Orders DB (MySQL)**: `localhost:3306/orders`
- **Product DB (PostgreSQL)**: `localhost:5432/product`

Los usuarios y contraseñas predeterminados se establecen en el archivo `docker-compose.yml`.






