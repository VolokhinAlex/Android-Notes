package com.example.java.android1.java_android_notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.java.android1.java_android_notes.model.DataNote
import com.example.java.android1.java_android_notes.repository.DetailsRepository
import com.example.java.android1.java_android_notes.repository.DetailsRepositoryImpl
import com.example.java.android1.java_android_notes.room.NotesDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditNoteViewModel(private val repository: DetailsRepository) : ViewModel() {

    fun upsertNote(dataNote: DataNote) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.upsertNote(dataNote)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class EditNoteViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EditNoteViewModel::class.java)) {
            EditNoteViewModel(
                repository = DetailsRepositoryImpl(
                    NotesDataBase.getInstance().noteDao()
                )
            ) as T
        } else {
            throw IllegalArgumentException("EditNoteViewModel not found")
        }
    }
}