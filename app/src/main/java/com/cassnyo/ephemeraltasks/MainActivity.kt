package com.cassnyo.ephemeraltasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.cassnyo.ephemeraltasks.data.datasource.TasksDataSource
import com.cassnyo.ephemeraltasks.ui.tasks.TaskListScreen
import com.cassnyo.ephemeraltasks.ui.theme.EphemeralTasksTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {

    private val tasksDataSource by lazy { TasksDataSource(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EphemeralTasksTheme {
                val systemUiController = rememberSystemUiController()
                val statusBarColor = MaterialTheme.colors.background
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = statusBarColor,
                    )
                }
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    TaskListScreen(tasksDataSource)
                }
            }
        }
    }
}