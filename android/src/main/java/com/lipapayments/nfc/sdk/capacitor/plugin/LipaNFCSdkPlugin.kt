package com.lipapayments.nfc.sdk.capacitor.plugin

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

    override fun load() {
        super.load()
        Log.i(TAG, "Config: $config...")
        if (config == null) {
            Log.i(
                TAG,
                "Initialization parameters are missing, please check if the config.json file is set if you wanna start the sdk when the plugin loads"
            )
            return
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

    @PluginMethod(returnType = PluginMethod.RETURN_PROMISE)
    fun initialise(call: PluginCall) {

        val apiKey = call.getString("apiKey") ?: run {
            Log.e(TAG, "")
            call.reject("SDK API Key is missing.")
            return
        }
        val tenantId = call.getString("tenantId") ?: run {
            Log.e(TAG, "")
            call.reject("tenantId is missing.")
            return
        }

        val getInTouchText = call.getString("getInTouchText", "Lipa Payments") as String
        val getInTouchLink = call.getString("getInTouchText")
        val enableBuiltInReceipt = call.getBoolean("enableBuiltInReceiptScreen", true) as Boolean

        Log.d(TAG, "Parameters seem to be present... initializing...")

        init(
            apiKey = apiKey,
            tenantId = tenantId,
            env = Env.TESTING,
            getInTouchLink = getInTouchLink,
            getInTouchText = getInTouchText,
            enableBuiltInReceipt = enableBuiltInReceipt,
            call = call
        )
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
            application = activity.application,
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
            call.reject("Merchant Id is missing")
            return
        }
        val operatorId = call.getString("operatorId") ?: merchantId
        val merchantName = call.getString("merchantName") ?: run {
            call.reject("Merchant name is missing")
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
                call.reject(it.message)
            }

            if (it is SdkLifeCycleEvent.SdkSetOperatorInfoSuccess) {
                Log.i(TAG, "Setting operator info was successful!!")
                call.resolve()
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
                is SDKTransactionEvent.SDKOnTransactionError -> {
                    call.resolve(
                        JSObject(
                            Gson().toJson(transactionEvent.transactionResult)
                        )
                    )
                }
                is SDKTransactionEvent.SDKOnTransactionApproved -> {
                    call.resolve(
                        JSObject(
                            Gson().toJson(transactionEvent.transactionResult)
                        )
                    )
                }
                is SDKTransactionEvent.SDKOnTransactionDeclined -> {
                    call.resolve(
                        JSObject(
                            Gson().toJson(transactionEvent.transactionResult)
                        )
                    )
                }
                is SDKTransactionEvent.SDKTransactionInitialisationError -> {
                    call.reject("{ error: ${transactionEvent.message} }")
                }
            }
        }
    }
}
