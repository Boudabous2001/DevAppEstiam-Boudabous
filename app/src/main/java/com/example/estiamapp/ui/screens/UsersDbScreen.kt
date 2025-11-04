package com.example.estiamapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.estiamapp.ui.users.UsersDbViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersDbScreen() {
    val vm: UsersDbViewModel = viewModel()
    val state by vm.users.collectAsState()

    Scaffold() { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var firstName by remember { mutableStateOf("") }
            var lastName by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }

            OutlinedTextField(
                value = firstName, onValueChange = { firstName = it },
                label = { Text("First Name") }, modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName, onValueChange = { lastName = it },
                label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email") }, modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        if (firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank()) {
                            vm.addUser(firstName, lastName, email)
                            firstName = ""; lastName = ""; email = ""
                        }
                    }
                ) { Text("Add User") }

                OutlinedButton(onClick = { vm.clearAll() }) { Text("Clear DB") }
            }

            Divider()

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) { }
        }
    }
}