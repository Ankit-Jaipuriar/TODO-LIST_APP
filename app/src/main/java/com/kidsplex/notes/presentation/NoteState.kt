package com.kidsplex.notes.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kidsplex.notes.data.Note

class NoteState (
    val notes:List<Note> = emptyList(),
    val title:MutableState<String> = mutableStateOf(""),
    val disp:MutableState<String> = mutableStateOf("")

)