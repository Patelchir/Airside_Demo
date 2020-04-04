
package com.example.airside_demo.utils

import android.util.Log


object DebugLog {
    private var className: String? = null
    private var lineNumber: Int = 0
    private var methodName: String? = null
    var DO_SOP = true

    private fun isDebuggable(): Boolean {
        return DO_SOP
    }

    private fun createLog(log: String): String {
        return "[" +
                methodName +
                ":" +
                lineNumber +
                "]" +
                log
    }

    private fun getMethodNames(sElements: Array<StackTraceElement>) {
        className = sElements[1].fileName
        methodName = sElements[1].methodName
        lineNumber = sElements[1].lineNumber
    }

    fun e(message: String) {
        if (isDebuggable()) {
            getMethodNames(Throwable().stackTrace)
            Log.e(className, createLog(message))
        }
    }

    fun i(message: String) {
        if (isDebuggable()) {
            getMethodNames(Throwable().stackTrace)
            Log.i(className, createLog(message))
        }
    }

    fun d(message: String) {
        if (isDebuggable()) {
            getMethodNames(Throwable().stackTrace)
            Log.d(className, createLog(message))
        }
    }

    fun v(message: String) {
        if (isDebuggable()) {
            getMethodNames(Throwable().stackTrace)
            Log.v(className, createLog(message))
        }
    }

    fun w(message: String) {
        if (isDebuggable()) {
            getMethodNames(Throwable().stackTrace)
            Log.w(className, createLog(message))
        }
    }

    fun wtf(message: String) {
        if (isDebuggable()) {
            getMethodNames(Throwable().stackTrace)
            Log.wtf(className, createLog(message))
        }
    }

    fun print_i(mesg: String) {
        if (isDebuggable()) {
            Log.i("Info", mesg)
        }
    }

    fun print_e(title: String, mesg: String) {
        if (isDebuggable()) {
            Log.e(title, mesg)
        }
    }

    fun print_v(title: String, mesg: String) {
        if (isDebuggable()) {
            Log.v(title, mesg)
        }
    }

    fun print_w(title: String, mesg: String) {
        if (isDebuggable()) {
            Log.w(title, mesg)
        }
    }

    fun print(title: String, e: Exception) {
        if (isDebuggable()) {
            println(
                "=========================" + title
                        + "========================="
            )
            e.printStackTrace()
        }
    }

    fun print(e: Exception) {
        if (isDebuggable()) {
            e.printStackTrace()
        }
    }

    fun htmlEncode(str: String): String {
        return str.replace(">".toRegex(), "&lt;").replace("<".toRegex(), "&gt;")
            .replace("&".toRegex(), "&amp;").replace("\"".toRegex(), "&quot;")
            .replace("'".toRegex(), "&#039;")
    }


    fun print(mesg: String) {
        if (isDebuggable()) {
            /*int maxLogSize = 1000;
			for(int i = 0; i <= mesg.length() / maxLogSize; i++) {
				int start = i * maxLogSize;
				int end = (i+1) * maxLogSize;
				end = end > mesg.length() ? mesg.length() : end;
				System.out.println(mesg.substring(start, end));
			}*/
            println(mesg)
        }
    }

    fun print(title: String, mesg: String) {
        if (isDebuggable()) {
            println("$title :: $mesg")
        }
    }

    fun print(title: String, i: Int) {
        if (isDebuggable()) {
            println("$title :: $i")
        }
    }

}