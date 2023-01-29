package com.example.java.android1.java_android_notes.view.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.recyclerview.widget.RecyclerView
import com.example.java.android1.java_android_notes.databinding.NoteItemBinding
import com.example.java.android1.java_android_notes.model.DataNote

class NotesAdapter(
    private val onLongClickListener: ((DataNote) -> Unit)?,
    private val onItemClickListener: ((DataNote) -> Unit)?,
    private val onCheckedChangeListener: (DataNote, Boolean) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private val notesData: MutableList<DataNote> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setNotesList(notes: List<DataNote>) {
        notesData.clear()
        notesData.addAll(notes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            NoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(notesData[layoutPosition])
            }
            itemView.setOnLongClickListener {
                onLongClickListener?.invoke(notesData[layoutPosition])
                return@setOnLongClickListener true
            }
        }

    override fun getItemCount(): Int = notesData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardBind(dataNote = notesData[position], onCheckedChangeListener)
    }

}

class ViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun cardBind(dataNote: DataNote, onCheckedChangeListener: (DataNote, Boolean) -> Unit) {
        binding.cardItemTitle.text = dataNote.noteTitle
        binding.cardItemDescription.text = dataNote.noteDescription
        binding.cardItemDate.text = dataNote.noteDate
        binding.cardItemFavorite.apply {
            isChecked = dataNote.noteFavorite ?: false
            setOnCheckedChangeListener { _, isChecked ->
                onCheckedChangeListener.invoke(dataNote, isChecked)
            }
        }
    }

}