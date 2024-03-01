package com.kidsplex.notes.presentation


import android.graphics.Color.rgb
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kidsplex.notes.R


@Composable
fun NoteScreen(
    state: NoteState,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit
) {

            Scaffold(
                topBar = {
                    Row(
                        Modifier
                            .background(Color(0xFF8AAAE5))
                            .fillMaxWidth()
                            .height(56.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "TO-DO LIST",
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                        IconButton(onClick = {
                            onEvent(NotesEvent.SortNotes)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.sort), contentDescription = null,
                                modifier = Modifier.size(35.dp),
                                tint = Color.Black
                            )
                        }
                    }

                },
                floatingActionButton = {
                    FloatingActionButton(
                        containerColor =  Color( 0xFF8AAAE5),
                        onClick = {
                            state.title.value = ""
                            state.disp.value = ""
                            navController.navigate("AddNoteScreen")
                        }) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                    }
                }

            ) {
                Box (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                ){
                LazyColumn(
                    contentPadding = it,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.notes.size) {
                        NoteItem(
                            state = state,
                            index = it,
                            onEvent = onEvent,
                            navController = navController
                        )
                    }
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    state: NoteState,
    index: Int,
    onEvent: (NotesEvent) -> Unit,
    navController: NavController
) {
    Card(
        onClick = {
            onEvent(
                NotesEvent.UpdateNote(
                    state.notes.get(index = index)
                )
            )
            navController.navigate("AddNoteScreen")
        },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
           Color( 0xFF8AAAE5)
        ),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = state.notes[index].title,color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = {
                        onEvent(
                            NotesEvent.DeleteNote(
                                state.notes.get(index = index)
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(Color(rgb(73, 93, 146)))
                ){
                    Text(text = "Mark as done", color = Color.White)

                }
            }
            Text(
                text = state.notes[index].disp, color = Color.Black,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,

            )
        }
    }
}



