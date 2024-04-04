package com.lipapayments.nfc.sdk.capacitor.plugin

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.getcapacitor.*
import com.getcapacitor.annotation.CapacitorPlugin
import com.google.gson.Gson
import com.lipa.tap.attestation.domain.models.SDKTransactionEvent
import com.lipa.tap.transaction.domain.SdkLifeCycleEvent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@CapacitorPlugin(name = "LipaNFCSdk")
internal class LipaNFCSdkPlugin : Plugin() {
    private val config by lazy { activity.readConfigFile() }

    companion object {
        const val TAG = "LipaNFCSdkPlugin"
    }

    private val application by lazy { activity.application }

    override fun load() {
        Log.i(TAG, "Loading...")
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

            override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
                super.onActivityPostCreated(activity, savedInstanceState)
                Log.i(TAG, "Config: $config...")
                if (config == null) {
                    Log.i(
                        TAG,
                        "Initialization parameters are missing, please check if the config.json file is set if you wanna start the sdk when the plugin loads"
                    )
                } else {
                    with(config as SdkInitializationConfig) {
                        val key = this.apiKey
                        val tenant = this.tenantId
                        val link = this.getInTouchLink
                        val text = this.getInTouchText
                        Log.i(TAG, "Load initializing...")
                        init(
                            env = Env.TESTING,
                            apiKey = key,
                            tenantId = tenant,
                            getInTouchLink = link,
                            getInTouchText = text,
                            enableBuiltInReceipt = true,
                        )
                    }
                }
            }

            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityResumed(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
        })

        super.load()
    }

    private fun init(
        apiKey: String,
        tenantId: String,
        env: Env,
        getInTouchLink: String?,
        getInTouchText: String,
        enableBuiltInReceipt: Boolean = false,
        call: PluginCall? = null
    ) {
        LipaNfcSDK.initialise(
            application = application,
            apiKey = apiKey,
            tenantId = tenantId,
            env = env,
            getInTouchLink = getInTouchLink,
            getInTouchText = getInTouchText,
            enableBuiltInReceiptScreen = enableBuiltInReceipt
        ) { sdkLifecycleEvent ->
            when(sdkLifecycleEvent) {
                SdkLifeCycleEvent.SdkConfigured,
                is SdkLifeCycleEvent.SdkDeviceState,
                SdkLifeCycleEvent.SdkInitialised,
                SdkLifeCycleEvent.SdkSetOperatorInfoSuccess,
                SdkLifeCycleEvent.SdkStartUpInitialised,
                is SdkLifeCycleEvent.SdkVersionCheck -> {
                    Log.i(TAG, "Completed event from SDK: ${sdkLifecycleEvent::class.java.simpleName}...")
                }
                SdkLifeCycleEvent.SdkStartUpSuccess -> {
                    Log.i(TAG, "SDK initialization successful!!")
                    call?.resolve(
                        JSObject(
                            Json.encodeToString(
                                mapOf("event" to sdkLifecycleEvent::class.java.simpleName)
                            )
                        )
                    )
                }
                is SdkLifeCycleEvent.SdkSetOperatorInfoError,
                is SdkLifeCycleEvent.SdkStartUpError,
                is SdkLifeCycleEvent.SdkVersionCheckError -> {
                    Log.e(TAG, (sdkLifecycleEvent as SdkLifeCycleEvent.SdkLifeCycleError).message)
                    val error = Json.encodeToString(
                        mapOf(
                            "event" to sdkLifecycleEvent::class.java.simpleName,
                            "message" to sdkLifecycleEvent.message
                        )
                    )
                    call?.reject(error)
                }
            }
        }
    }

    @PluginMethod(returnType = PluginMethod.RETURN_PROMISE)
    fun setOperatorInfo(call: PluginCall) {
        val merchantId = call.getString("merchantId") ?: run {
            val error = Json.encodeToString(
                mapOf("result" to "SdkSetOperatorInfoError", "message" to "Merchant Id is missing")
            )
            call.reject(error)
            return
        }
        val operatorId = call.getString("operatorId") ?: merchantId
        val merchantName = call.getString("merchantName") ?: run {
            val error = Json.encodeToString(
                mapOf("result" to "SdkSetOperatorInfoError", "message" to "Merchant name is missing")
            )
            call.reject(error)
            return
        }
        val terminalNickName = call.getString("terminalNickName") ?: "$merchantName-$merchantId's device"
        val externalMerchant = call.getBoolean("externalMerchant", true) as Boolean

        LipaNfcSDK.setOperatorInfo(
            merchantId = merchantId,
            operatorId = operatorId,
            merchantName = merchantName,
            externalMerchant = externalMerchant,
            terminalNickname = terminalNickName
        ) {
            if (it is SdkLifeCycleEvent.SdkSetOperatorInfoError) {
                Log.e(TAG, it.message)
                val error = Json.encodeToString(
                    mapOf("result" to "SdkSetOperatorInfoError", "message" to it.message)
                )
                call.reject(error)
            }

            if (it is SdkLifeCycleEvent.SdkSetOperatorInfoSuccess) {
                Log.i(TAG, "Setting operator info was successful!!")
                val result = Json.encodeToString(mapOf("result" to "SdkSetOperatorInfoSuccess"))
                call.resolve(JSObject(result))
            }
        }
    }

    @PluginMethod(returnType = PluginMethod.RETURN_PROMISE)
    fun startTransaction(call: PluginCall) {
        val amount = call.data.getString("amount")?.toLong()
        Log.i(TAG, "Amount: $amount")
        LipaNfcSDK.startTransaction(amount) { transactionEvent ->
            when(transactionEvent) {
                SDKTransactionEvent.SDKTransactionStarted,
                is SDKTransactionEvent.SDKOnMorePaymentOptions -> Unit
                is SDKTransactionEvent.SDKOnTransactionError,
                is SDKTransactionEvent.SDKOnTransactionApproved,
                is SDKTransactionEvent.SDKOnTransactionDeclined -> {
                    val result = (transactionEvent as? SDKTransactionEvent.SDKOnTransactionApproved)?.transactionResult
                        ?: (transactionEvent as? SDKTransactionEvent.SDKOnTransactionDeclined)?.transactionResult
                        ?: (transactionEvent as SDKTransactionEvent.SDKOnTransactionError)?.transactionResult

                    call.resolve(
                        JSObject(Gson().toJson(result))
                    )
                }
                is SDKTransactionEvent.SDKTransactionInitialisationError -> {
                    call.reject("{ error: ${transactionEvent.message} }")
                }
            }
        }
    }
}
