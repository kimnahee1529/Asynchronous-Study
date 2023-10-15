package com.nhkim.dispatchers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nhkim.dispatchers.ui.theme.DispatchersTheme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

//다양한 코루틴 빌더에서의 디스패처 사용

//1.launch
suspend fun firstFun() = coroutineScope {
    println("runBLocking 컨텍스트 / ${Thread.currentThread().name}")
    launch {
        println("부모의 컨텍스트 / ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) {
        println("Default / ${Thread.currentThread().name}")
    }
    launch(Dispatchers.IO) {
        println("IO / ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) {
        println("Unconfined / ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("Fast campus")) {
        println("newSingleThreadContext / ${Thread.currentThread().name}")
    }
}
//2.async
suspend fun secondFun() = coroutineScope {
    println("runBLocking 컨텍스트 / ${Thread.currentThread().name}")
    async {
        println("부모의 컨텍스트 / ${Thread.currentThread().name}")
    }
    async(Dispatchers.Default) {
        println("Default / ${Thread.currentThread().name}")
    }
    async(Dispatchers.IO) {
        println("IO / ${Thread.currentThread().name}")
    }
    async(Dispatchers.Unconfined) {
        println("Unconfined / ${Thread.currentThread().name}")
        delay(100L)
        println("Unconfined / ${Thread.currentThread().name}")
    }
    async(newSingleThreadContext("Fast campus")) {
        println("newSingleThreadContext / ${Thread.currentThread().name}")
    }
}

suspend fun thirdFun() = coroutineScope {
    async(Dispatchers.Unconfined) {
        println("Unconfined / ${Thread.currentThread().name}")
        delay(100L)
        println("Unconfined / ${Thread.currentThread().name}")
        delay(100L)
        println("Unconfined / ${Thread.currentThread().name}")
    }
}

suspend fun jobFun() = coroutineScope {
    val job = launch {
        launch() {
            println(coroutineContext[Job])
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("3!")
        }

        launch() {
            println(coroutineContext[Job])
            println("launch2: ${Thread.currentThread().name}")
            delay(1000L)
            println("1!")
        }
    }
    delay(500L)
    job.cancelAndJoin()
    delay(1000L)

}
@OptIn(ExperimentalStdlibApi::class)
suspend fun elementFun() = coroutineScope {
    launch {
        launch(Dispatchers.IO + CoroutineName("launch1")) {
            println("launch1: ${Thread.currentThread().name}")
            println(coroutineContext[CoroutineDispatcher])
            println(coroutineContext[CoroutineName])
            delay(5000L)
        }
        launch(Dispatchers.Default + CoroutineName("launch2")) {
            println("launch2: ${Thread.currentThread().name}")
            println(coroutineContext[CoroutineDispatcher])
            println(coroutineContext[CoroutineName])
            delay(10L)
        }
    }
}

fun main() = runBlocking<Unit>{
//    firstFun()
//    secondFun()
//    thirdFun()
//    jobFun()
    elementFun()
}