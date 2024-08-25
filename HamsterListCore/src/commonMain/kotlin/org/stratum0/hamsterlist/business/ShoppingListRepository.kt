package org.stratum0.hamsterlist.business

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import org.stratum0.hamsterlist.models.Item
import org.stratum0.hamsterlist.models.SyncResponse
import org.stratum0.hamsterlist.utils.FetchState
import kotlinx.coroutines.flow.StateFlow

interface ShoppingListRepository {
    @NativeCoroutinesState
    val syncState: StateFlow<FetchState<SyncResponse>>

    @NativeCoroutines
    suspend fun loadListById(id: String)
    @NativeCoroutines
    suspend fun deleteItem(listId: String, item: Item)
    @NativeCoroutines
    suspend fun addItem(listId: String, item: Item)
    @NativeCoroutines
    suspend fun changeItem(listId: String, item: Item)
    companion object {
        // TODO temporary solution for fast setup/testing, use dependency injection
        val instance: ShoppingListRepository = ShoppingListRepositoryImpl()
    }
}