package com.economy.splitpay.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PaymentMethodScreen(navController: NavController) {
    var isPayNowChecked by remember { mutableStateOf(false) } // Para manejar si selecciona "Pagar ahora"
    var selectedPaymentMethod by remember { mutableStateOf("Apple Pay") } // Para seleccionar el método de pago

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Sección "Escoge un método de pago"
        Text(
            text = "Escoge un método de pago",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )

        // Opción "Pagar luego" con icono de cheque
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { isPayNowChecked = false }, // Cambiar selección
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pagar luego",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            if (!isPayNowChecked) {
                Icon(
                    imageVector = Icons.Default.Check, // Icono de cheque
                    contentDescription = "Pagar luego seleccionado",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Opción "Pagar ahora" con icono de cheque
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { isPayNowChecked = true }, // Cambiar selección
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pagar ahora",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            if (isPayNowChecked) {
                Icon(
                    imageVector = Icons.Default.Check, // Icono de cheque
                    contentDescription = "Pagar ahora seleccionado",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Mostrar opciones de pago solo si selecciona "Pagar ahora"
        if (isPayNowChecked) {
            // Opción para agregar tarjeta de crédito o débito
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Agregar tarjeta de débito o crédito",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "Es fácil y seguro. También puedes agregar una cuenta de banco",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Ir a agregar tarjeta",
                    modifier = Modifier.size(24.dp)
                )
            }

            // Sección "Paga con un toque"
            Text(
                text = "Paga con un toque",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start)
            )

            // Métodos de pago con icono de cheque
            PaymentOptionItem("Apple Pay", selectedPaymentMethod) { selectedPaymentMethod = "Apple Pay" }
            PaymentOptionItem("Google Pay", selectedPaymentMethod) { selectedPaymentMethod = "Google Pay" }
        }
    }
}

@Composable
fun PaymentOptionItem(name: String, selectedPaymentMethod: String, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onSelect() }, // Cambiar selección
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
        if (selectedPaymentMethod == name) {
            Icon(
                imageVector = Icons.Default.Check, // Icono de cheque
                contentDescription = "$name seleccionado",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
