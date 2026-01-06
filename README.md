Este repositorio contiene un sistema robusto diseñado para gestionar el ciclo de vida completo de las notificaciones. El servicio actúa como un intermediario entre la lógica de negocio y los proveedores finales de mensajería, asegurando la entrega eficiente y el seguimiento de cada alerta enviada.

Arquitectura del Sistema
El sistema está construido bajo una arquitectura orientada a servicios, permitiendo que las notificaciones se procesen de forma asíncrona para no bloquear el flujo principal de las aplicaciones cliente.

Funcionalidades Principales
Despacho Multi-canal: Soporte integrado para diferentes canales de comunicación (Correo electrónico, notificaciones Push y SMS).

Gestión de Plantillas: Motor de plantillas dinámicas que permite personalizar el contenido de los mensajes según el usuario o evento.

Procesamiento Asíncrono: Implementación de colas para manejar picos de tráfico y garantizar que ninguna notificación se pierda.

Seguimiento de Estado: Registro detallado del estado de cada notificación (Pendiente, Enviado, Fallido, Leído).

Proveedores de Terceros: Integración modular con servicios externos (como Twilio, SendGrid o Firebase Cloud Messaging).
