package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Recipe
import com.example.ui.screens.CustomRecipeDialog
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.PantryMatcherScreen
import com.example.ui.screens.RecipeDetailScreen
import com.example.ui.screens.SavedAndGroceryScreen
import com.example.ui.theme.BudgetCookTheme
import com.example.ui.viewmodel.BudgetCookViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetCookTheme {
                BudgetCookApp()
            }
        }
    }
}

@Composable
fun BudgetCookApp(
    viewModel: BudgetCookViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Discover, 1 = Pantry, 2 = Favorites & Grocery
    var selectedRecipeDetail by remember { mutableStateOf<Recipe?>(null) }
    var showCustomRecipeDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Collect snackbar events
    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val navItems = listOf(
        NavItem("Discover", Icons.Filled.Explore, Icons.Outlined.Explore, "discover_nav_tab"),
        NavItem("Pantry Match", Icons.Filled.Kitchen, Icons.Outlined.Kitchen, "pantry_nav_tab"),
        NavItem("Grocery & Saved", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart, "grocery_nav_tab")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (selectedRecipeDetail == null) {
                NavigationBar(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_navigation_bar")
                ) {
                    navItems.forEachIndexed { index, item ->
                        val isSelected = selectedTab == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTab = index },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            modifier = Modifier.testTag(item.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (selectedRecipeDetail != null) {
                RecipeDetailScreen(
                    recipe = selectedRecipeDetail!!,
                    onBackClick = { selectedRecipeDetail = null },
                    onFavoriteToggle = { recipe -> viewModel.toggleFavorite(recipe) },
                    onAddIngredientsToGrocery = { recipe -> viewModel.addIngredientsToGrocery(recipe) }
                )
            } else {
                when (selectedTab) {
                    0 -> DiscoverScreen(
                        viewModel = viewModel,
                        onRecipeSelect = { selectedRecipeDetail = it },
                        onCreateRecipeClick = { showCustomRecipeDialog = true }
                    )
                    1 -> PantryMatcherScreen(
                        viewModel = viewModel,
                        onRecipeSelect = { selectedRecipeDetail = it }
                    )
                    2 -> SavedAndGroceryScreen(
                        viewModel = viewModel,
                        onRecipeSelect = { selectedRecipeDetail = it }
                    )
                }
            }

            if (showCustomRecipeDialog) {
                CustomRecipeDialog(
                    onDismiss = { showCustomRecipeDialog = false },
                    onSubmit = { title, category, cost, servings, prep, cook, desc, ing, inst, bTips, cTips ->
                        viewModel.addCustomRecipe(
                            title, category, cost, servings, prep, cook, desc, ing, inst, bTips, cTips
                        )
                    }
                )
            }
        }
    }
}

data class NavItem(
    val label: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
)
