package com.example.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CustomRecipeDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        category: String,
        costPerServing: Double,
        servings: Int,
        prepTime: Int,
        cookTime: Int,
        description: String,
        ingredients: String,
        instructions: String,
        budgetTips: String,
        cookingTips: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Lunch & Dinner") }
    var costText by remember { mutableStateOf("1.50") }
    var servingsText by remember { mutableStateOf("2") }
    var prepTimeText by remember { mutableStateOf("10") }
    var cookTimeText by remember { mutableStateOf("15") }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var budgetTips by remember { mutableStateOf("") }
    var cookingTips by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("custom_recipe_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Add Custom Budget Idea 🍳",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Recipe Title") },
                    placeholder = { Text("e.g. Garlic Egg Fried Noodles") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_recipe_title_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Short Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = ingredients,
                    onValueChange = { ingredients = it },
                    label = { Text("Ingredients (1 per line)") },
                    placeholder = { Text("2 Eggs\n1 cup Rice\n1 tbsp Soy sauce") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("custom_recipe_ingredients_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Cooking Steps (1 per line)") },
                    placeholder = { Text("Boil noodles al dente.\nMix garlic & peanut butter.\nToss noodles and serve.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("custom_recipe_instructions_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = budgetTips,
                    onValueChange = { budgetTips = it },
                    label = { Text("Budget Saving Tip (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cookingTips,
                    onValueChange = { cookingTips = it },
                    label = { Text("Cooking Pro Tip (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            val cost = costText.toDoubleOrNull() ?: 1.50
                            val servings = servingsText.toIntOrNull() ?: 2
                            val prep = prepTimeText.toIntOrNull() ?: 10
                            val cook = cookTimeText.toIntOrNull() ?: 15

                            onSubmit(
                                title,
                                category,
                                cost,
                                servings,
                                prep,
                                cook,
                                description,
                                ingredients,
                                instructions,
                                budgetTips,
                                cookingTips
                            )
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("save_custom_recipe_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Save Recipe")
                    }
                }
            }
        }
    }
}
