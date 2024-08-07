package de.evylon.shoppinglist.android.gui.shoppinglist

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.evylon.shoppinglist.android.ShoppingListTheme
import de.evylon.shoppinglist.models.Item
import de.evylon.shoppinglist.models.Order
import de.evylon.shoppinglist.viewmodel.LoadingState
import de.evylon.shoppinglist.viewmodel.shoppinglist.ItemState
import de.evylon.shoppinglist.viewmodel.shoppinglist.ShoppingListState

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Suppress("LongParameterList", "LongMethod")
fun ShoppingListView(
    uiState: ShoppingListState,
    deleteItem: (Item) -> Unit,
    changeItem: (id: String, item: String) -> Unit,
    addItem: (item: String) -> Unit,
    selectOrder: (Order) -> Unit,
    refresh: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.loadingState is LoadingState.Loading,
        onRefresh = refresh
    )
    Column(modifier = modifier.fillMaxSize()) {
        Surface(color = MaterialTheme.colors.background) {
            Column {
                Text(
                    text = uiState.shoppingList.title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
                OrdersMenu(
                    orders = uiState.orders,
                    selectedOrder = uiState.selectedOrder,
                    selectOrder = selectOrder,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LazyColumn(
                state = rememberLazyListState(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .pullRefresh(pullRefreshState)
            ) {
                items(
                    uiState.shoppingList.items,
                    key = { it.id }
                ) { item ->
                    ShoppingListItem(
                        itemState = ItemState(
                            item = item,
                            categoryDefinition = ItemState.getCategory(item, uiState.categories)
                        ),
                        deleteItem = deleteItem,
                        changeItem = { itemText -> changeItem(item.id, itemText) },
                        isEnabled = isEnabled
                    )
                }
            }
            PullRefreshIndicator(
                refreshing = false,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                scale = true
            )
        }
        AddItemView(
            addItem = addItem,
            isEnabled = isEnabled,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
private fun AddItemView(
    addItem: (item: String) -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    var itemText by remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = itemText,
                placeholder = { Text("New Item") },
                onValueChange = { itemText = it },
                singleLine = true,
                enabled = isEnabled,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                enabled = isEnabled,
                onClick = {
                    addItem(itemText)
                    itemText = ""
                    focusManager.clearFocus()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add item",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, group = "light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, group = "dark")
@Composable
fun ShoppingListViewPreview() {
    ShoppingListTheme {
        Surface(color = MaterialTheme.colors.background) {
            ShoppingListView(
                uiState = ShoppingListState.mock,
                deleteItem = {},
                changeItem = { _, _ -> },
                selectOrder = {},
                isEnabled = true,
                addItem = {},
                refresh = {}
            )
        }
    }
}
