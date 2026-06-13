package com.example.service

import android.content.Context
import android.content.SharedPreferences
import com.example.data.IbadahMediaStream
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * High-fidelity Simulated Firebase Live Media sync backend.
 * Provides SQLite/SharedPreferences persistence under "firebase_mock_backend" scope,
 * and publishes real-time sync status flow ("Syncing", "Synced", "Offline mode", etc.).
 */
class FirebaseMediaCenterBackend(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("firebase_mock_backend", Context.MODE_PRIVATE)
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val listType = Types.newParameterizedType(List::class.java, IbadahMediaStream::class.java)
    private val mediaAdapter = moshi.adapter<List<IbadahMediaStream>>(listType)

    private val _syncState = MutableStateFlow(SyncState.SYNCED)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val _dbFavorites = MutableStateFlow<List<IbadahMediaStream>>(emptyList())
    val dbFavorites: StateFlow<List<IbadahMediaStream>> = _dbFavorites.asStateFlow()

    private val _recentlyPlayed = MutableStateFlow<List<IbadahMediaStream>>(emptyList())
    val recentlyPlayed: StateFlow<List<IbadahMediaStream>> = _recentlyPlayed.asStateFlow()

    private val mainScope = CoroutineScope(Dispatchers.Main)

    enum class SyncState {
        IDLE, SYNCING, SYNCED, ERROR, STANDALONE
    }

    init {
        loadData()
    }

    private fun loadData() {
        try {
            val favsJson = prefs.getString("favorites_json", null)
            if (!favsJson.isNullOrEmpty()) {
                val parsed = mediaAdapter.fromJson(favsJson)
                if (parsed != null) {
                    _dbFavorites.value = parsed
                }
            }

            val recentJson = prefs.getString("recently_played_json", null)
            if (!recentJson.isNullOrEmpty()) {
                val parsed = mediaAdapter.fromJson(recentJson)
                if (parsed != null) {
                    _recentlyPlayed.value = parsed
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toggleFavorite(stream: IbadahMediaStream) {
        val current = _dbFavorites.value.toMutableList()
        val index = current.indexOfFirst { it.id == stream.id }
        if (index != -1) {
            current.removeAt(index)
        } else {
            current.add(stream)
        }
        _dbFavorites.value = current
        saveFavorites(current)
    }

    fun isFavorite(streamId: String): Boolean {
        return _dbFavorites.value.any { it.id == streamId }
    }

    fun addToRecent(stream: IbadahMediaStream) {
        val current = _recentlyPlayed.value.toMutableList()
        current.removeAll { it.id == stream.id }
        current.add(0, stream)
        val trimmed = current.take(15) // Keep last 15 streams
        _recentlyPlayed.value = trimmed
        saveRecentlyPlayed(trimmed)
    }

    fun forceSync(onComplete: () -> Unit = {}) {
        mainScope.launch {
            _syncState.value = SyncState.SYNCING
            delay(1500) // Beautiful live networking simulation
            _syncState.value = SyncState.SYNCED
            onComplete()
        }
    }

    private fun saveFavorites(list: List<IbadahMediaStream>) {
        mainScope.launch(Dispatchers.IO) {
            try {
                val json = mediaAdapter.toJson(list)
                prefs.edit().putString("favorites_json", json).apply()
                
                // Show automatic background cloud backup state
                _syncState.value = SyncState.SYNCING
                delay(800)
                _syncState.value = SyncState.SYNCED
            } catch (e: Exception) {
                _syncState.value = SyncState.ERROR
            }
        }
    }

    private fun saveRecentlyPlayed(list: List<IbadahMediaStream>) {
        mainScope.launch(Dispatchers.IO) {
            try {
                val json = mediaAdapter.toJson(list)
                prefs.edit().putString("recently_played_json", json).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
