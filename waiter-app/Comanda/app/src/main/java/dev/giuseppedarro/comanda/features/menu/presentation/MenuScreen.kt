package dev.giuseppedarro.comanda.features.menu.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onNavigateToCategory: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MenuScreenContent(
        categories = uiState.categories,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onNavigateToCategory = onNavigateToCategory,
        onNavigateBack = onNavigateBack,
        onRefresh = { viewModel.loadMenu() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MenuScreenContent(
    categories: List<dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory>,
    isLoading: Boolean,
    error: String?,
    onNavigateToCategory: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = onRefresh
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            ComandaTopAppBar(
                title = "Menu",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            when {
                isLoading && categories.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                categories.isEmpty() -> {
                    Text(
                        text = "No categories available",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(categories) { category ->
                            CategoryCard(
                                categoryName = category.name,
                                itemCount = category.items.size,
                                onClick = { onNavigateToCategory(category.name) }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun CategoryCard(
    categoryName: String,
    itemCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$itemCount items",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuScreenContentPreview() {
    ComandaTheme {
        MenuScreenContent(
            categories = listOf(
                MenuCategory(name = "Appetizers", items = emptyList()),
                MenuCategory(name = "Main Courses", items = emptyList()),
                MenuCategory(name = "Desserts", items = emptyList())
            ),
            isLoading = false,
            error = null,
            onNavigateToCategory = {},
            onNavigateBack = {},
            onRefresh = {}
        )
    }
}