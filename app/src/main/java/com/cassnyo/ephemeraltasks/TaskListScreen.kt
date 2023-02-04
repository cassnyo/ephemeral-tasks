package com.cassnyo.ephemeraltasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@Composable
fun TaskListScreen(
    dataSource: TasksDataSource,
    modifier: Modifier = Modifier
) {
    val tasks by dataSource.observeTasks().collectAsState(initial = emptyList())
    var newTaskDescription by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = stringResource(id = R.string.app_name),
        )
        TaskList(
            tasks = tasks,
            onTaskClicked = { task ->
                val updatedTask = task.copy(completed = !task.completed)
                dataSource.updateTask(updatedTask)
            },
            modifier = Modifier.weight(1f)
        )
        AddTaskFooter(
            description = newTaskDescription,
            onDescriptionChanged = { newTaskDescription = it },
            onAddTaskClicked = {
                dataSource.addTask(newTaskDescription)
                newTaskDescription = ""
            }
        )
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
            .height(64.dp)
    ) {
        Text(
            text = title,
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
    LazyColumn(modifier = modifier) {
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
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        Checkbox(
            checked = task.completed,
            onCheckedChange = { onClicked(task) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task.description,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AddTaskFooter(
    description: String,
    onDescriptionChanged: (String) -> Unit,
    onAddTaskClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.secondary)
            .padding(4.dp)
    ) {
        TextField(
            value = description,
            onValueChange = onDescriptionChanged,
            placeholder = {
                Text(text = "Enter your task here")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.CheckBoxOutlineBlank, contentDescription = "Check box")
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrect = false,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { onAddTaskClicked() }
            ),
            modifier = Modifier.weight(1f),
        )

        IconButton(
            onClick = { onAddTaskClicked() },
        ) {
            Icon(
                imageVector = Icons.Default.AddCircleOutline,
                tint = Color.White,
                contentDescription = "Add new task"
            )
        }
    }
}