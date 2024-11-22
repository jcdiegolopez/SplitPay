package com.economy.splitpay.model

data class PaymentHistory(
    val paymentId: String? = "", // ID del pago, generado automáticamente por Firestore
    val userId: String = "", // ID del usuario que realizó o debe realizar el pago
    val groupId: String = "", // ID del grupo relacionado con el pago
    val amountPaid: Double = 0.0, // Monto pagado
    val datePaid: String = "", // Fecha del pago
    val status: String = "pending", // Estado del pago: "pending", "paid_later", "paid"
    val paymentMethod: String? = null, // Método de pago, si ya se realizó
    val isLeader: Boolean = false, // Si el usuario fue líder en el grupo
    val whoOwesMe: List<Debt> = emptyList() // Si es líder, lista de miembros que deben dinero
)

data class Debt(
    val userId: String = "", // ID del usuario que debe dinero
    val amountDue: Double = 0.0 // Cantidad adeudada
)
