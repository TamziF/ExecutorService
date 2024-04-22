import kotlin.concurrent.thread

class MyExecutorService(threadNumbers: Int) {

    private val threads: ArrayList<Thread> = ArrayList()
    private val tasksQueue: ArrayDeque<Runnable> = ArrayDeque()

    private val objectToSendSleepThisShit = Object()

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
        synchronized(objectToSendSleepThisShit) {
            tasksQueue.add(command)
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
            var runnable: Runnable? = null

            synchronized(objectToSendSleepThisShit) {
                tasksQueue.removeFirstOrNull()?.let {
                    runnable = it
                } ?: sendThreadSleep()
            }

            runnable?.run()
        }
        thread.interrupt()
    }

    private fun sendThreadSleep() {
        objectToSendSleepThisShit.wait()
    }
}