import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.thread
import kotlin.concurrent.write

class ExecutorService21(threadNumbers: Int) {

    private val threads: ArrayList<Thread> = ArrayList()
    private val tasksQueue: ArrayDeque<Runnable> = ArrayDeque()

    private val objectToSendSleepThisShit = Object()

    private val lock = ReentrantReadWriteLock()

    init {
        repeat(threadNumbers) {
            val thread = thread {
                Thread.currentThread().name = it.toString()
                bindThread()
            }
            threads.add(thread)
        }
    }

    fun submit(command: Runnable) {
        lock.write { tasksQueue.add(command) }

        synchronized(objectToSendSleepThisShit) {
            objectToSendSleepThisShit.notify()
        }
    }

    fun interrupt() {
        for (thread in threads) {
            thread.interrupt()
        }
        synchronized(objectToSendSleepThisShit) {
            objectToSendSleepThisShit.notifyAll()
        }
    }

    private fun bindThread() {
        val thread = Thread.currentThread()
        while (!thread.isInterrupted) {
            var runnable: Runnable?

            lock.read {
                runnable = tasksQueue.removeFirstOrNull()
            }

            runnable?.run() ?: sendThreadSleep(thread)
        }
        println("Я спать ${thread.name}")
        thread.interrupt()
    }

    private fun sendThreadSleep(thread: Thread) {
        synchronized(objectToSendSleepThisShit) {
            try {
                objectToSendSleepThisShit.wait()
            } catch (e: InterruptedException) {
                thread.interrupt()
            }
        }
    }
}