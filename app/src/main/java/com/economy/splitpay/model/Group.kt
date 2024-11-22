package com.economy.splitpay.model

data class Group(
    var groupId: String? = null, // El ID del grupo, generado automáticamente por Firestore
    val leaderId: String = "", // ID del líder del grupo (userId)
    val groupName: String = "", // Nombre del grupo
    val token: String = "", // Token del grupo
    val totalAmount: Double = 0.0, // Monto total del grupo
    val creationDate: String = "", // Fecha de creación del grupo
    val status: String = "pending", // Estado del grupo: "pending", "partially_finalized", "finalized"
    val members: List<Member> = emptyList() // Lista de miembros del grupo
) {
    // Constructor sin argumentos requerido por Firestore
    constructor() : this(
        groupId = null,
        leaderId = "",
        groupName = "",
        token = "",
        totalAmount = 0.0,
        creationDate = "",
        status = "pending",
        members = emptyList()
    )
}

data class Member(
    val userId: String = "", // ID del miembro del grupo (userId)
    val name: String = "", // Nombre del miembro
    var assignedAmount: Double = 0.0, // Monto asignado a este miembro
    val paymentStatus: String = "pending", // Estado del pago: "pending", "paid_later", "paid"
    val paymentMethod: String? = null, // "tarjeta", "google pay"
    val accepted: Boolean = false // Si el miembro acepto el monto asignado
) {
    // Constructor sin argumentos requerido por Firestore
    constructor() : this(
        userId = "",
        name = "",
        assignedAmount = 0.0,
        paymentStatus = "pending",
        paymentMethod = null,
        accepted = false
    )
}

