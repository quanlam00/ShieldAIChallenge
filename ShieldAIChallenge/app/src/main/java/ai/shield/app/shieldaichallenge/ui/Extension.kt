package ai.shield.app.shieldaichallenge.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Shorthand to create a coroutine in background and run a task with it
fun LifecycleOwner.doInBackground(task: suspend () -> Unit) {
    this.lifecycleScope.launch(Dispatchers.IO) {
        task()
    }
}

//Shorthand to create a coroutine in background and run a task with it for ViewModel
fun ViewModel.doInBackground(task: suspend () -> Unit) {
    this.viewModelScope.launch(Dispatchers.IO) {
        task()
    }
}