package com.lipapayments.nfc.sdk.capacitor.plugin

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.lipa.tap.utils.Env
import com.lipapayments.nfc.sdk.capacitor.plugin.LipaNFCSdkPlugin.Companion.TAG
import java.io.InputStreamReader

@SuppressLint("DiscouragedApi")
internal fun Context.readConfigFile(): SdkInitializationConfig? {
    Log.i(TAG, "Reading config file...")

    val jsonString = try {
        resources.assets.open("lipa-nfc-sdk-config.json").use { inputStream ->
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
    val env: Env,
    val apiKey: String,
    val tenantId: String,
    val getInTouchLink: String,
    val getInTouchText: String,
    val paymentLauncherActivityName: String,
    val enableBuiltInReceipt: Boolean = true
)