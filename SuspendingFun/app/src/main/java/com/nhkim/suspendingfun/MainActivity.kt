package com.nhkim.suspendingfun

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
import com.nhkim.suspendingfun.ui.theme.SuspendingFunTheme
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.system.measureTimeMillis

suspend fun getRandom1(): Int{
    delay(1000L)
    return Random.nextInt(0,500)
}

suspend fun getRandom2(): Int{
    delay(1000L)
    return Random.nextInt(0,500)
}

fun main() = runBlocking {
    val elapsedTime = measureTimeMillis {

        //1.getRandom1이 먼저 수행되고 getRandom2이 수행됨
//        val value1 =  getRandom1()
//        val value2 =  getRandom2()
//        println("${value1} + ${value2} = ${value1 + value2}") //->2060

        //2.getRandom1와 getRandom2이 동시 수행
//        val value1 = async{ getRandom1() }
//        val value2 = async{ getRandom2() }
//        println("${value1.await()} + ${value2.await()} = ${value1.await() + value2.await()}") //->1066

        //3.생성시기와 수행시기를 분리함
        val value1 = async(start = CoroutineStart.LAZY ) { getRandom1() }
        val value2 = async(start = CoroutineStart.LAZY ) { getRandom2() }

        value1.start()
        value2.start()

        println("${value1.await()} + ${value2.await()} = ${value1.await() + value2.await()}") //->1088
    }

    println(elapsedTime)
}