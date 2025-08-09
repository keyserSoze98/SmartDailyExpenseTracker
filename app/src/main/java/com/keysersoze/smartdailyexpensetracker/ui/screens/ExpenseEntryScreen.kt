package com.keysersoze.smartdailyexpensetracker.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.keysersoze.smartdailyexpensetracker.ui.viewmodels.ExpenseEntryViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseEntryScreen(
    viewModel: ExpenseEntryViewModel = hiltViewModel()
) {
    val ui by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val categories = listOf("Staff", "Travel", "Food", "Utility")

    var expanded by remember { mutableStateOf(false) }

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.onReceiptUrlChange(it.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Total Spent Today
        Text(
            text = "Total Spent Today: ₹${"%.2f".format(ui.todayTotal)}",
            style = MaterialTheme.typography.headlineSmall
        )

        Divider()

        // Title
        OutlinedTextField(
            value = ui.title,
            onValueChange = viewModel::onTitleChange,
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )

        // Amount
        OutlinedTextField(
            value = ui.amountText,
            onValueChange = { input ->
                val filtered = input.filter { it.isDigit() || it == '.' }
                viewModel.onAmountChange(filtered)
            },
            label = { Text("Amount (₹)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )

        // Category
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(ui.category)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                categories.forEach { c ->
                    DropdownMenuItem(
                        text = { Text(c) },
                        onClick = {
                            viewModel.onCategoryChange(c)
                            expanded = false
                        }
                    )
                }
            }
        }

        // Notes
        OutlinedTextField(
            value = ui.notes,
            onValueChange = viewModel::onNotesChange,
            label = { Text("Notes (optional)") },
            placeholder = { Text("Max 100 characters") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp, max = 140.dp),
            shape = MaterialTheme.shapes.medium
        )

        // Receipt + Submit Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Add Receipt Button
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable {
                        if (ui.receiptUrl == null) {
                            launcher.launch("image/*")
                        } else {
                            viewModel.onReceiptUrlChange(null)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (ui.receiptUrl != null) {
                    AsyncImage(
                        model = ui.receiptUrl,
                        contentDescription = "Receipt",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Receipt",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Submit Button
            Button(
                onClick = { viewModel.addExpense {} },
                modifier = Modifier
                    .height(56.dp)
                    .weight(1f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Submit")
            }
        }

        // Error Message
        ui.errorMessage?.let { err ->
            Text(
                text = err,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Check Animation
        val lastAdded = ui.lastAddedId
        AnimatedVisibility(
            visible = lastAdded != null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Surface(
                shape = CircleShape,
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Added",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        // Auto-hide check
        LaunchedEffect(lastAdded) {
            if (lastAdded != null) {
                Toast.makeText(context, "Expense added", Toast.LENGTH_SHORT).show()
                delay(800)
                viewModel.clearLastAddedFlag()
            }
        }
    }
}