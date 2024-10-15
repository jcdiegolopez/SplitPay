package com.economy.splitpay.model

data class User(
    val userId: String = "", // El ID del usuario, generado automáticamente por Firestore
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "", // Contraseña encriptada
    val friends: List<String> = emptyList(), // Lista de IDs de amigos
    val pendingFriendRequests: List<String> = emptyList(), // Lista de IDs de solicitudes de amistad pendientes
    val notifications: List<String> = emptyList(), // Lista de IDs de notificaciones
    val paymentHistory: List<String> = emptyList(), // Lista de IDs de historial de pagos
    val groupsLed: List<String> = emptyList(), // Lista de IDs de grupos liderados
    val groupsJoined: List<String> = emptyList() // Lista de IDs de grupos donde es miembro
)
