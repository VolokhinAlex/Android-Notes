package com.example.java.android1.java_android_notes.viewmodel

import androidx.lifecycle.*
import com.example.java.android1.java_android_notes.model.DataNote
import com.example.java.android1.java_android_notes.repository.MainRepository
import com.example.java.android1.java_android_notes.repository.MainRepositoryImpl
import com.example.java.android1.java_android_notes.room.NoteEntity
import com.example.java.android1.java_android_notes.room.NotesDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    private val searchNotes: MutableLiveData<String> = MutableLiveData("")
    val notesData: LiveData<List<NoteEntity>> = Transformations.switchMap(searchNotes) { text ->
        if (text.isEmpty()) {
            getAllNotes()
        } else {
            repository.getNotesByQuery(text)
        }
    }

    private fun getAllNotes() = repository.getAllNotes()

    fun insert(dataNote: DataNote) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertNote(dataNote)
    }

    fun update(dataNote: DataNote) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(dataNote)
    }

    fun deleteNote(dataNote: DataNote) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(dataNote)
        }
    }

    fun searchChangedQuery(newQuery: String) {
        searchNotes.value = newQuery
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(
                repository = MainRepositoryImpl(
                    NotesDataBase.getInstance().noteDao()
                )
            ) as T
        } else {
            throw IllegalArgumentException("MainViewModel not found")
        }
    }
}