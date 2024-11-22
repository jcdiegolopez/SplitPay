package com.economy.splitpay.model

data class FriendRequest(
    val requestId: String? = "", // ID de la solicitud de amistad, generado automáticamente por Firestore
    val fromUserId: String = "", // ID del usuario que envía la solicitud
    val toUserId: String = "", // ID del usuario que recibe la solicitud
    val status: String = "pending", // Estado de la solicitud: "pending", "accepted", "declined"
    val dateSent: String = "" // Fecha en la que se envió la solicitud
)
