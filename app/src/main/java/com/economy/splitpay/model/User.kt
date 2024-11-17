package com.economy.splitpay.model


data class User(
    val userId: String = "", // El ID del usuario, generado por Firebase
    val firstname: String = "", // Nombre del usuario
    val lastname: String = "", // Nombre del usuario
    val username: String = "", // Nombre de usuario
    val email: String = "", // Correo electrónico del usuario
    val tel:String = "", // Telefono del usuario
    val password: String = "", // Contraseña encriptada (puedes usar algún algoritmo de hash)
    val friends: List<String> = emptyList(), // Lista de IDs de amigos
    val pendingFriendRequests: List<String> = emptyList(), // Lista de solicitudes de amistad pendientes
    val notifications: List<String> = emptyList(), // Lista de notificaciones
    val paymentHistory: List<String> = emptyList(), // Historial de pagos (IDs o detalles)
    val groupsLed: List<String> = emptyList(), // Grupos liderados (IDs de grupos)
    val groupsJoined: List<String> = emptyList() // Grupos a los que pertenece el usuario (IDs de grupos)
)
