package com.lipapayments.nfc.sdk.capacitor.plugin

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.lipapayments.nfc.sdk.capacitor.plugin.LipaNFCSdkPlugin.Companion.TAG
import java.io.InputStreamReader

@SuppressLint("DiscouragedApi")
internal fun Context.readConfigFile(): SdkInitializationConfig? {
    Log.i(TAG, "Reading config file...")

    val jsonString = try {
        resources.assets.open("config.json").use { inputStream ->
            InputStreamReader(inputStream).readText()
        }
    } catch (ex: Exception) {
        Log.e(TAG, "Failed to open config file config.json.", ex)
        return null
    }

    Log.d(TAG, "The json string: $jsonString")
    val gson = Gson()
    return try {
        gson.fromJson(jsonString, SdkInitializationConfig::class.java)
    } catch (ex: Exception) {
        Log.e(TAG, "Failed to read config from config.json", ex)
        return null
    }
}

internal data class SdkInitializationConfig(
    val apiKey: String,
    val getInTouchLink: String,
    val getInTouchText: String,
    val tenantId: String
)