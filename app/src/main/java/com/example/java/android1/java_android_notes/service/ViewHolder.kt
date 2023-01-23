package com.example.java.android1.java_android_notes.service

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.java.android1.java_android_notes.data.DataNote
import com.example.java.android1.java_android_notes.data.DataNoteSource
import com.example.java.android1.java_android_notes.databinding.NoteItemBinding
import com.example.java.android1.java_android_notes.ui.ListOfNotesFragment

class ViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindCard(
        fragment: ListOfNotesFragment,
        dataNote: DataNote?,
        onItemLongClickListener: ((position: Int) -> Unit)?
    ) {
        dataNote?.apply {
            binding.cardItemTitle.text = noteName
            binding.cardItemDate.text = noteDate
            binding.cardItemDescription.text = noteDescription
            binding.cardItemFavorite.isChecked = noteFavorite
        }
        itemView.setOnLongClickListener {
            fragment.setLastSelectedPosition(layoutPosition)
            onItemLongClickListener?.invoke(layoutPosition)
            return@setOnLongClickListener false
        }
    }

    fun clear(fragment: Fragment?) {
        itemView.setOnClickListener(null)
    }

    fun setFavoriteNote(dataNoteSource: DataNoteSource, index: Int) {
        binding.cardItemFavorite.setOnCheckedChangeListener { _, isFavoriteNote ->
            val dataNote = dataNoteSource.getItem(index)
            dataNote?.let {
                it.noteFavorite = isFavoriteNote
                dataNoteSource.addAndRemoveFavoriteNote(it)
            }
            itemView.post { dataNoteSource.sortByFavorite() }
        }
    }
}