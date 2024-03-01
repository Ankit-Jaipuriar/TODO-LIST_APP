package com.kidsplex.notes.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidsplex.notes.data.Note
import com.kidsplex.notes.data.NoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.concurrent.timerTask

class NoteViewModel(
    private var dao: NoteDao
) :ViewModel(){
    private var isSortedByDateAdded = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private var notes = isSortedByDateAdded.flatMapLatest {
        if(it){
            dao.getOrderedByDateAddedBy()
        }else{
            dao.getOrderedByTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var _state = MutableStateFlow(NoteState())
    var state = combine(_state,isSortedByDateAdded,notes){
        state, isSortedByDateAdded, notes ->
        NoteState(
            notes = notes,
            title = state.title,
            disp = state.disp
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),NoteState())

    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }
            is NotesEvent.SaveNote -> {
                val note = Note(
                    title = event.title,
                    disp = event.disp,
                    dateAdded = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    dao.upsertNote(note = note)
                }
                val newState = NoteState(
                    title = mutableStateOf(""),
                    disp = mutableStateOf("")
                )
                _state.value = newState
            }
            NotesEvent.SortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }

            is NotesEvent.UpdateNote -> {
                viewModelScope.launch {
                    dao.upsertNote(event.note)
                }
            }
        }

        }
    }
