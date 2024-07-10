package de.evylon.shoppinglist.viewmodel.shoppinglist

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import de.evylon.shoppinglist.business.ShoppingListRepository
import de.evylon.shoppinglist.models.Item
import de.evylon.shoppinglist.models.SyncedShoppingList
import de.evylon.shoppinglist.viewmodel.LoadingState
import de.evylon.shoppinglist.utils.FetchState
import de.evylon.shoppinglist.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShoppingListViewModel : BaseViewModel() {
    private val shoppingListRepository: ShoppingListRepository = ShoppingListRepository.instance
    private val _uiState = MutableStateFlow(ShoppingListState.empty)

    @NativeCoroutinesState
    val uiState = _uiState.asStateFlow()

    init {
        shoppingListRepository.shoppingList.onEach { networkResult ->
            when (networkResult) {
                is FetchState.Success -> updateList(networkResult.value)
                is FetchState.Failure -> _uiState.update { oldState ->
                    networkResult.throwable.printStackTrace()
                    oldState.copy(loadingState = LoadingState.Error)
                }
                is FetchState.Loading -> _uiState.update { oldState ->
                    oldState.copy(loadingState = LoadingState.Loading)
                }
            }
        }.launchIn(scope)
    }

    fun fetchList(listId: String) {
        scope.launch {
            shoppingListRepository.loadListById(listId)
        }
    }

    fun deleteItem(item: Item) {
        scope.launch {
            shoppingListRepository.deleteItem(_uiState.value.shoppingList.id, item)
        }
    }

    fun addItem(newItem: String) {
        scope.launch {
            shoppingListRepository.addItem(_uiState.value.shoppingList.id, Item.Text(newItem))
        }
    }

    fun changeItem(id: String, newItem: String) {
        scope.launch {
            shoppingListRepository.changeItem(_uiState.value.shoppingList.id, Item.Text(id, newItem))
        }
    }

    private fun updateList(shoppingList: SyncedShoppingList) {
        scope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    shoppingList = shoppingList,
                    loadingState = LoadingState.Done
                )
            )
        }
    }
}
