package com.example.java.android1.java_android_notes.service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.java.android1.java_android_notes.data.DataNoteSource
import com.example.java.android1.java_android_notes.databinding.NoteItemBinding
import com.example.java.android1.java_android_notes.ui.ListOfNotesFragment

class NotesAdapter(
    private val notesFragment: ListOfNotesFragment,
    private val dataNoteSource: DataNoteSource?,
    private val onItemClickListener: ((position: Int) -> Unit)?,
    private val onItemLongClickListener: ((position: Int) -> Unit)?
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataNote = dataNoteSource?.getItem(position)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
        holder.bindCard(notesFragment, dataNote, onItemLongClickListener)
        dataNoteSource?.let { holder.setFavoriteNote(it, position) }
    }

    override fun getItemCount(): Int = dataNoteSource?.dataNoteCount ?: 0
}