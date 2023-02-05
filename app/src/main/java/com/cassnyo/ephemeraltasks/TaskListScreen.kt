package com.cassnyo.ephemeraltasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun TaskListScreen(
    dataSource: TasksDataSource,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val tasks by dataSource.observeTasks().collectAsState(initial = emptyList())
    var newTaskDescription by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = stringResource(id = R.string.app_name),
        )
        Divider()
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            TaskList(
                tasks = tasks,
                onTaskClicked = { task ->
                    coroutineScope.launch {
                        val updatedTask = task.copy(completed = !task.completed)
                        dataSource.updateTask(updatedTask)
                    }
                },
                modifier = Modifier
            )
            AddTaskFooter(
                description = newTaskDescription,
                onDescriptionChanged = { newTaskDescription = it },
                isAddTaskEnabled = newTaskDescription.isNotBlank(),
                onAddTaskClicked = {
                    coroutineScope.launch {
                        dataSource.addTask(newTaskDescription)
                        newTaskDescription = ""
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun TaskList(
    tasks: List<Task>,
    onTaskClicked: (Task) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = 12.dp,
            bottom = 90.dp,
        ),
        modifier = modifier,
    ) {
        items(
            count = tasks.size,
            key = { index -> index },
        ) { index ->
            TaskItem(
                task = tasks[index],
                onClicked = onTaskClicked,
            )
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onClicked: (Task) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClicked(task) }
            .padding(
                horizontal = 8.dp,
            )
    ) {
        Checkbox(
            checked = task.completed,
            onCheckedChange = { onClicked(task) }
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = task.description,
            color = if (task.completed) Color.LightGray else MaterialTheme.colors.onBackground,
            style = LocalTextStyle.current.copy(
                textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AddTaskFooter(
    description: String,
    onDescriptionChanged: (String) -> Unit,
    isAddTaskEnabled: Boolean,
    onAddTaskClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Divider()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(
                    vertical = 4.dp,
                    horizontal = 16.dp
                )
        ) {
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChanged,
                placeholder = {
                    Text(
                        text = "Enter your task here",
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = false,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onAddTaskClicked() }
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = { onAddTaskClicked() },
                enabled = isAddTaskEnabled
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircleOutline,
                    tint = if (isAddTaskEnabled) MaterialTheme.colors.primary else Color.LightGray,
                    contentDescription = "Add new task",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}