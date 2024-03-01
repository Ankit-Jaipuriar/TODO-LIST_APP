package com.kidsplex.notes.presentation

import com.kidsplex.notes.data.Note

sealed interface NotesEvent {
    object SortNotes:NotesEvent
    data class UpdateNote(var note: Note):NotesEvent
    data class DeleteNote(var note: Note):NotesEvent
    data class SaveNote(
        var title:String,
        var disp:String
    ):NotesEvent
}