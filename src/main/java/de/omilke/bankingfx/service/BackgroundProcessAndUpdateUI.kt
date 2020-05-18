package de.omilke.bankingfx.service

import javafx.application.Platform
import javafx.concurrent.Task
import javafx.concurrent.WorkerStateEvent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BackgroundProcessAndUpdateUI<R>(private val task: Task<R>, private val updateUI: (R) -> Runnable) {

    init {

        //perform the task on a ground thread
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED) {
            //... let the UI Thread render the result
            Platform.runLater(updateUI(task.value))
        }

    }

    fun process() {
        //start it off
        es.submit(task)

    }

    companion object

    private

    val es: ExecutorService by lazy {

        Executors.newCachedThreadPool {
            Thread(it).apply {
                //creating daemon threads in the ThreadPool doesn't prevent the application shutdown
                isDaemon = true
            }
        }
    }

}