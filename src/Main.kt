import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

fun main() {
    val executor = ExecutorService21(5)

    val runnable = Runnable {
        println("${Thread.currentThread().name} ${System.currentTimeMillis()}")
    }

    val array = ArrayList<Runnable>(Collections.nCopies(5, runnable))

    val start = System.currentTimeMillis()
    println(start)

    for(runnable in array){
        executor.submit(runnable)
    }

    Thread.sleep(1000L)

    executor.interrupt()
}