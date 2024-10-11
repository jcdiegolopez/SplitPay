package com.economy.splitpay.ui.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HistorialScreen(navController: NavController) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Tabs titles
    val tabTitles = listOf("Tus deudores", "Pagos que debes", "Grupos completados")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar
        SearchBar(searchText, onSearchChange = { searchText = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on the selected tab
        when (selectedTabIndex) {
            0 -> TusDeudoresList()  // Show list for "Tus deudores"
            1 -> PagosQueDebesList() // Show list for "Grupos completados"
            2 -> GruposCompletadosList()  // Show list for "Pagos que debes"
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchText: TextFieldValue, onSearchChange: (TextFieldValue) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = { onSearchChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text("Buscar") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xFFF6F6F6),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun TusDeudoresList() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(debtors) { debtor ->
            DebtorItem(
                debtor = debtor
            )
        }
    }
}

@Composable
fun GruposCompletadosList() {
    // This composable will display the "Grupos completados" list
    Text("No hay grupos completados aún")
    // Add your dynamic list logic here
}

@Composable
fun PagosQueDebesList() {
    // This composable will display the "Pagos que debes" list
    Text("No tienes pagos pendientes")
    // Add your dynamic list logic here
}




@Composable
fun DebtorItem(debtor: Debtor) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = debtor.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${debtor.debtorName} Q${debtor.amount}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = { /* Acción de pedir */ },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E3EB))
        ) {
            Text(text = "Pedir", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
        }
    }
}





val debtors = listOf(
    Debtor(name = "Cena mamá", amount = 150.00, debtorName = "Manuel"),
    Debtor(name = "Boliche", amount = 150.00, debtorName = "Manuel"),
    Debtor(name = "Fiesta", amount = 150.00, debtorName = "Manuel")
)

data class Debtor(val name: String, val amount: Double, val debtorName: String)

data class Group(val name: String, val amount: Double, val debtorName: String)
