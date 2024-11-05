package com.economy.splitpay.model

data class Group(
    val groupId: String = "", // El ID del grupo, generado automáticamente por Firestore
    val leaderId: String = "", // ID del líder del grupo (userId)
    val groupName: String = "",
    val totalAmount: Double = 0.0, // Monto total del grupo
    val creationDate: String = "", // Fecha de creación del grupo
    val status: String = "pending", // Estado del grupo: "pending", "partially_finalized", "finalized"
    val members: List<Member> = emptyList(), // Lista de miembros del grupo
    val finalized: Boolean = false, // Si el grupo está finalizado completamente
    val paymentCompleted: Boolean = false // Si todos completaron sus pagos
)

data class Member(
    val userId: String = "", // ID del miembro del grupo (userId)
    val assignedAmount: Double = 0.0, // Monto asignado a este miembro
    val paymentStatus: String = "pending", // Estado del pago: "pending", "paid_later", "paid"
    val paymentMethod: String? = null // Método de pago (solo si ya pagó)
)
