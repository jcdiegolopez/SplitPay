package com.economy.splitpay.model

data class Notification(
    val notificationId: String = "", // ID de la notificación, generado automáticamente por Firestore
    val userId: String = "", // ID del usuario destinatario
    val type: String = "", // Tipo de notificación: "groupInvite", "friendRequest"
    val message: String = "", // Mensaje de la notificación
    val groupId: String? = null, // ID del grupo si es una invitación de grupo
    val status: String = "unread", // Estado de la notificación: "unread", "read"
    val date: String = "" // Fecha de la notificación
)
