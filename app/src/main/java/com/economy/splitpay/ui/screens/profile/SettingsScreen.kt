package com.economy.splitpay.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.economy.splitpay.ui.theme.primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Términos y Condiciones",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Bienvenido a nuestra aplicación. A continuación se detallan los términos y condiciones de uso de la aplicación:",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = """
                    1. Aceptación de los Términos.
                    
                    Al usar esta aplicación, aceptas los términos y condiciones aquí especificados. Si no estás de acuerdo, por favor no utilices la aplicación.
                    
                    2. Uso de la Aplicación.
                    
                    Esta aplicación debe ser utilizada conforme a las leyes aplicables y con fines legítimos. Nos reservamos el derecho de suspender el acceso en caso de uso indebido.
                    
                    3. Privacidad de los Datos.
                    
                    Respetamos tu privacidad y protegemos tus datos de acuerdo con nuestra política de privacidad.
                    
                    4. Modificaciones a los Términos.
                    
                    Nos reservamos el derecho de modificar los términos en cualquier momento. Notificaremos cualquier cambio importante.
                    
                    5. Limitación de Responsabilidad.
                    
                    No somos responsables por daños o pérdidas derivados del uso de esta aplicación.
                    
                    Estos son solo ejemplos de términos y condiciones. Debes consultar a un abogado para personalizar este contenido según sea necesario.
                """.trimIndent(),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Button to Accept or Go Back
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primary)
            ) {
                Text(text = "Aceptar", color = Color.White)
            }
        }
    }
}