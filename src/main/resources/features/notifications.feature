Feature: API de Notificaciones - Aceptación
  Como usuario autenticado
  Quiero poder crear y consultar mis notificaciones
  Para verificar el funcionamiento del microservicio de notificaciones

  Background:
    Given la API base de notificaciones está disponible en "http://localhost:8081"
    And poseo un token JWT válido con subject "j.kamilo3020@gmail.com"

  @create_notification
  Scenario: Crear una notificación de tipo EMAIL exitosamente
    When envío POST a "/api/notifications" con el JSON:
      """
      {
        "recipient": "j.kamilo3020@gmail.com",
        "channel": "EMAIL",
        "title": "Bienvenida",
        "message": "Hola, esta es una prueba de notificación por correo."
      }
      """
    Then la respuesta debe tener código 200
    And el cuerpo debe contener el campo "id"
    And el campo "channel" debe ser "EMAIL"
    And el campo "recipient" debe ser "j.kamilo3020@gmail.com"

  @get_by_id
  Scenario: Consultar una notificación por id
    Given que ya creé una notificación y guardé su "id"
    When envío GET a "/api/notifications/{id}"
    Then la respuesta debe tener código 200
    And el cuerpo debe contener el campo "id" con el mismo valor
    And el campo "message" debe existir

  @my_notifications_email
  Scenario: Listar mi historial filtrado por canal=EMAIL
    When envío GET a "/api/notifications/me?channel=EMAIL&page=0&size=10&sort=createdAt,desc"
    Then la respuesta debe tener código 200
    And el cuerpo debe contener una página de resultados
    And todos los elementos deben tener "channel" = "EMAIL"
    And todos los elementos deben tener "recipient" = "j.kamilo3020@gmail.com"

  @unauthorized
  Scenario: Acceso no autorizado sin token
    When envío GET a "/api/notifications/me" SIN encabezado Authorization
    Then la respuesta debe tener código 401
