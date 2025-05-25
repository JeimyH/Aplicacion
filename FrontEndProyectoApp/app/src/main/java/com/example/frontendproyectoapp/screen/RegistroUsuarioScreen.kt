package com.example.frontendproyectoapp.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendproyectoapp.model.Usuario
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel
import android.widget.Toast
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.KeyboardType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun RegistroUsuarioScreen(viewModel: UsuarioViewModel = viewModel()) {
    val usuarios by viewModel.usuarios.observeAsState(emptyList())
    var idBusqueda by remember { mutableStateOf("") }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var usuarioAEliminar by remember { mutableStateOf<Usuario?>(null) }

    // Campos del formulario
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }

    // DatePickerDialog
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            fechaNacimiento = format.format(selectedDate.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(true) {
        viewModel.obtenerUsuarios()
    }

    val usuariosFiltrados = usuarios.filter {
        it.idUsuario?.toString()?.contains(idBusqueda, ignoreCase = true) ?: false
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Registrar nuevo usuario", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura (m)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha de Nacimiento") },
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar fecha")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // Validaciones
                if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank() ||
                    peso.isBlank() || altura.isBlank() || fechaNacimiento.isBlank()
                ) {
                    Toast.makeText(context, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val pesoFloat = peso.toFloatOrNull()
                val alturaFloat = altura.toFloatOrNull()
                if (pesoFloat == null || alturaFloat == null) {
                    Toast.makeText(context, "Peso y altura deben ser números válidos.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val usuario = Usuario(
                    nombre = nombre,
                    correo = correo,
                    contrasena = contrasena,
                    peso = pesoFloat,
                    altura = alturaFloat,
                    fechaNacimiento = fechaNacimiento
                )

                viewModel.guardarUsuario(usuario)
                Toast.makeText(context, "Usuario registrado", Toast.LENGTH_SHORT).show()

                // Limpiar campos
                nombre = ""
                correo = ""
                contrasena = ""
                peso = ""
                altura = ""
                fechaNacimiento = ""

                viewModel.obtenerUsuarios()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar usuario")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Registrar Usuario")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Lista de Usuarios", style = MaterialTheme.typography.headlineSmall)

        TextField(
            value = idBusqueda,
            onValueChange = { idBusqueda = it },
            label = { Text("Buscar por ID") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            if (usuariosFiltrados.isEmpty()) {
                item { Text("No hay usuarios registrados.") }
            } else {
                items(usuariosFiltrados) { usuario ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${usuario.idUsuario}", style = MaterialTheme.typography.titleMedium)
                            Text("Nombre: ${usuario.nombre}")
                            Text("Correo: ${usuario.correo}")
                            Text("Contraseña: ${usuario.contrasena}")
                            Text("Fecha Nacimiento: ${usuario.fechaNacimiento}")
                            Text("Peso: ${usuario.peso}")
                            Text("Altura: ${usuario.altura}")

                            Button(
                                onClick = {
                                    usuarioAEliminar = usuario
                                    showDialog = true
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmación") },
            text = { Text("¿Está seguro que desea eliminar a este usuario?") },
            confirmButton = {
                TextButton(onClick = {
                    usuarioAEliminar?.let {
                        viewModel.eliminarUsuario(it.idUsuario)
                        Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                        viewModel.obtenerUsuarios()
                    }
                    showDialog = false
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistroUsuarioScreen(viewModel: UsuarioViewModel = viewModel()) {
    RegistroUsuarioScreen(viewModel = viewModel)
}