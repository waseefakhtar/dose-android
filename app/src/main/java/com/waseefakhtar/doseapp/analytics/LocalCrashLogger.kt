package com.waseefakhtar.doseapp.analytics

import android.content.Context
import android.util.Log
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Local crash logger that writes crash reports to device storage.
 * For personal use - all devices are accessible for debugging.
 */
class LocalCrashLogger(
    private val context: Context
) {
    private val crashLogDir: File by lazy {
        File(context.filesDir, "crash_logs").apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * Record an exception to local storage and logcat
     */
    fun recordException(exception: Throwable) {
        try {
            val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
            val logFile = File(crashLogDir, "crash_$timestamp.log")

            val stackTrace = StringWriter()
            exception.printStackTrace(PrintWriter(stackTrace))

            val logContent = buildString {
                appendLine("=== Crash Report ===")
                appendLine("Timestamp: ${Date()}")
                appendLine("Exception: ${exception.javaClass.simpleName}")
                appendLine("Message: ${exception.message}")
                appendLine("\nStack Trace:")
                appendLine(stackTrace.toString())
                appendLine("\n=== End Report ===")
            }

            logFile.writeText(logContent)
            Log.e(TAG, "Exception recorded to: ${logFile.absolutePath}", exception)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write crash log", e)
        }
    }

    /**
     * Log a message to local storage
     */
    fun log(message: String) {
        try {
            val timestamp = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val logFile = File(crashLogDir, "app_log_$timestamp.log")

            val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            logFile.appendText("[$timeStr] $message\n")
            Log.d(TAG, message)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write log", e)
        }
    }

    /**
     * Get all crash log files for debugging
     */
    fun getCrashLogs(): List<File> {
        return crashLogDir.listFiles()?.filter { it.name.startsWith("crash_") }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }

    /**
     * Clear old crash logs (optional - keep last 30 days)
     */
    fun clearOldLogs(daysToKeep: Int = 30) {
        val cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L)
        crashLogDir.listFiles()?.forEach { file ->
            if (file.lastModified() < cutoffTime) {
                file.delete()
            }
        }
    }

    companion object {
        private const val TAG = "LocalCrashLogger"
    }
}
