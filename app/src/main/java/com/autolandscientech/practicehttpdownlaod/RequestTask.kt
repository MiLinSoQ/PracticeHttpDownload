package com.autolandscientech.practicehttpdownlaod

import android.content.Context
import android.util.Log
import java.lang.Exception
import java.net.URL

@Suppress("SENSELESS_COMPARISON")
class RequestTask(context: Context) {

    private val TAG = "RequestTask"



    companion object TaskStatus {
        const val IDLE = 0
        const val WORKING = 1
        const val FINISH = 2
        const val FAIL = 10
    }

    private lateinit var myThread: Thread

    private lateinit var myTaskListener: TaskProcess
    private var myContext: Context = context
    private var myStatus: Int
    private var myResult: Result

    init {
        myStatus = IDLE
        myResult = Result()
        // myThread = Thread.currentThread()
    }

    fun run() {

//        myThread = Thread(this)
//
//        if (myStatus == WORKING) return
//
//        myResult = this.onRunning()
//        if (myResult.status == Result.ResultStatus.SUCCESS) onFinish() else onFail()

        try {
            // 執行
            myThread = Thread(Runnable {
                myResult = this.onRunning()
                if (myResult.status == Result.ResultStatus.SUCCESS) onFinish() else onFail()
            })

            myThread.start()
        }
        catch (ex: Exception) {
            Log.d(TAG, "[Error]-> ${ ex.message }")
        }
    }

    private fun onRunning(): Result {
        if (myTaskListener == null) return myResult
        myStatus = WORKING
        return myTaskListener.onRunning(myResult)
    }

    private fun onFinish() {
        if (myTaskListener == null) return
        myStatus = FINISH
        myTaskListener.onFinish(myResult)
    }

    private fun onFail() {
        if (myTaskListener == null) return
        myStatus = FAIL
        myTaskListener.onFail(myResult)
    }

    fun setOnProcess(listener: TaskProcess?): RequestTask {

        if (listener == null) return this
        let {
            myTaskListener = listener
            return this
        }
    }

    interface TaskProcess {

        val currentContext: Context

        fun onRunning(result: Result): Result
        fun onFinish(result: Result)
        fun onFail(result: Result)
    }

    class Result {

        companion object ResultStatus {
            const val UNKNOWN   = 0
            const val SUCCESS   = 1
            const val FAIL      = 2
            const val ERROR     = 3
        }

        var status: Int = UNKNOWN
        var data: String = ""
        var message: String = ""

    }
}