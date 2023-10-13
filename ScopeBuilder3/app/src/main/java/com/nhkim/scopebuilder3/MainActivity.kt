package com.nhkim.scopebuilder3

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
import com.nhkim.scopebuilder3.ui.theme.ScopeBuilder3Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


suspend fun doCount() = coroutineScope {
    val job1 = launch(Dispatchers.Default) {
        var i = 1
        var nextTime = System.currentTimeMillis() + 100L

//        while(i<=10){
        while(i<=10){ //cancle이 동작되는지 확인하려면 isActive를 사용해야 함
            val currentTime = System.currentTimeMillis()
            if(currentTime >= nextTime){
                println(i)
                nextTime = currentTime + 100L
                i++
            }
        }
    }

    delay(200L)
//    job1.cancelAndJoin()
    job1.cancel()
    println("doCount Done!")
}

// Dispatchers.Default로 하면 다른 스레드에서 실행되서 cancel이 안 먹는다고 했는데 아래의 코드는 왜 작동 되지???
suspend fun doOneTwoThree() = coroutineScope {
    val job1 = launch() {
        try {
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("3!")
        }finally {
            println("job1 is finishing!")
        }
    }
    val job2 = launch() {
        try {
            println("launch2: ${Thread.currentThread().name}")
            delay(1000L)
            println("1!")
        }finally {
            println("job2 is finishing!")
        }
    }
    val job3 = launch() {
        try {
            println("launch3: ${Thread.currentThread().name}")
            delay(1000L)
            println("2!")
        }finally {
            println("job1 is finishing!")
        }
    }
    println("delay 직전")
    delay(800L)
    println("delay 직후")
    job1.cancelAndJoin()
    job2.cancelAndJoin()
    job3.cancelAndJoin()
}

fun main() = runBlocking {
    doOneTwoThree()
//    doCount()
    println("runBlocking: ${Thread.currentThread().name}")
    println("5!")
}