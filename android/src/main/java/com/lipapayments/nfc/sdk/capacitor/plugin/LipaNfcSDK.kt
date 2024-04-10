package com.lipapayments.nfc.sdk.capacitor.plugin

import android.app.Activity
import android.app.Application
import android.util.Log
import com.lipa.tap.attestation.domain.models.SDKTransactionEvent
import com.lipa.tap.attestation.domain.models.SDKTransactionEvent.*
import com.lipa.tap.sdkManagement.adapter.out.DeviceStateUIAdapter
import com.lipa.tap.sdkManagement.application.port.`in`.VersionCheckCallback
import com.lipa.tap.sdkManagement.utils.IDeviceStateUIListener
import com.lipa.tap.transaction.domain.SdkLifeCycleEvent
import com.lipa.tap.transaction.domain.SdkLifeCycleEvent.*
import com.lipa.tap.transaction.domain.enums.NFCTransactionStartResult.NFC_NOT_ENABLED
import com.lipa.tap.transaction.domain.enums.NFCTransactionStartResult.STARTED
import com.lipa.tap.transaction.domain.enums.TransactionResult.*
import com.lipa.tap.transaction.domain.models.MorePaymentOptionsData
import com.lipa.tap.transaction.domain.models.TransactionStatus
import com.lipa.tap.utils.*
import com.lipa.tap.utils.listeners.ITransactionListener
import com.lipa.tap.utils.startup.StartupManager
import com.lipapayments.nfc.sdk.capacitor.plugin.EventMap.map
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import org.jetbrains.annotations.ApiStatus.Experimental
import kotlin.reflect.KClass

object EventMap {
    internal val map: MutableMap<KClass<out SdkLifeCycleEvent>, List<(SdkLifeCycleEvent) -> Unit>> = mutableMapOf()
    private val TAG = "EventMap"

    infix fun <T : SdkLifeCycleEvent> map(eventMapper: Pair<KClass<T>, (T) -> Unit>) {
        val (event, onEvent) = eventMapper
        Log.d(TAG, "Mapping $event")
        @Suppress("UNCHECKED_CAST")
        map[event] = listOf(onEvent as ((SdkLifeCycleEvent) -> Unit))
    }

    /**
     * Not allowing adding of multiple events because removing listeners will require some id of
     * the onEvent to remove. Not sure if I want the API to be that annoying
     */
    fun <T : SdkLifeCycleEvent> remove(event: KClass<T>) {
        map[event] = listOf()
    }

    fun <T : SdkLifeCycleEvent> getListener(event: KClass<T>) = map[event]?.firstOrNull()

    fun onEvent(triggeredEvent: SdkLifeCycleEvent) {
        map.forEach { (registeredEvent, callbacks) ->

            if (triggeredEvent isInstanceOf registeredEvent) {
                callbacks.forEach { callback ->
                    try {
                        Log.i(TAG, "Running callback for $triggeredEvent")
                        callback(triggeredEvent)
                    } catch (e: Throwable) {
                        Log.e(TAG, "Failed to run additional integrator callback", e)
                    }
                }
            }
        }

    }

    private inline infix fun <reified T> T.isInstanceOf(base: KClass<*>) =
        base.java.isInstance(this) || T::class == base
}

@Experimental
object LipaNfcSDK {
    private val SDKScope = CoroutineScope(Dispatchers.Main.immediate + CoroutineName("LipaNFCSDK"))
    private val SDKTransactionScope = CoroutineScope(
        Dispatchers.Main.immediate + CoroutineName("LipaNFCSDKTransaction")
    )
    // SDK State
    private lateinit var startupManager: StartupManager
    private var terminalNickname: String? = null
    private var initialised = false
    private const val TAG = "LipaNfcSDK"
    // End SDK State

    fun initialise(
        application: Application,
        apiKey: String,
        tenantId: String,
        env: Env = Env.PROD,
        getInTouchText: String = "Lipa Payments",
        getInTouchLink: String? = "https://www.lipapayments.com/contact-us",
        enableBuiltInReceiptScreen: Boolean = true,
        paymentLauncherActivity: KClass<out Activity>,
        onEvent: (SdkLifeCycleEvent) -> Unit,
    ) {
        SDKScope.launch {

            channelFlow {
                init(
                    apiKey,
                    tenantId,
                    application,
                    env,
                    getInTouchText,
                    getInTouchLink,
                    enableBuiltInReceiptScreen,
                    paymentLauncherActivity,
                ) {
                    runBlocking { send(it) }
                }

                awaitClose()
            }.collect {
                try {
                    onEvent(it)
                    EventMap.onEvent(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun init(
        apiKey: String,
        tenantId: String,
        application: Application,
        env: Env,
        getInTouchText: String = "Lipa Payments",
        getInTouchLink: String?,
        enableBuiltInReceiptScreen: Boolean = true,
        paymentLauncherActivity: KClass<out Activity>,
        onEvent: (SdkLifeCycleEvent) -> Unit,
    ) {
        if (!initialised) {
            LipaTapSDK.initialize(
                context = application,
                getInTouchText = getInTouchText,
                getInTouchLink = getInTouchLink,
                enableBuiltInReceiptScreen = enableBuiltInReceiptScreen,
                paymentLauncherActivity = paymentLauncherActivity
            )
            initialised = true
        }
        onEvent(SdkInitialised)
        NFCSdkConfiguration.configure(
            /*baseUrl =*/env.url,
            /*apiKey =*/ apiKey,
            /*tenantId =*/ tenantId,
        )
        onEvent(SdkConfigured)
        startupManager = StartupManager(object : IStartupListener {
            override fun onSuccess() {
                onEvent(SdkStartUpSuccess)
                DeviceStateUIAdapter.setDeviceStateUIListener(object : IDeviceStateUIListener {
                    override fun onDeviceStateChange(activated: Boolean) {
                        if (!activated) {
                            onEvent(
                                SdkDeviceState(
                                    DeviceState.DEACTIVATED,
                                    "Device state changed to: Deactivated."
                                )
                            )
                        } else {
                            onEvent(SdkDeviceState(DeviceState.ACTIVATED, "Device state changed to: Activated."))
                        }

                    }

                    override fun onError(errorMessage: String) {
                        onEvent(SdkDeviceState(DeviceState.ERRORED, "Error checking device state. $errorMessage"))
                    }
                })
            }

            override fun onError(error: String) {
                onEvent(SdkStartUpError("Startup Error: $error"))
            }
        }, listOf(), object : VersionCheckCallback {
            override fun onResult(isValid: Boolean, isError: Boolean) {
                if (isValid) {
                    onEvent(SdkVersionCheck(true, "Valid Version"))
                } else {
                    if (isError) {
                        onEvent(SdkVersionCheckError("Startup Error: Error validating version"))
                    } else {
                        onEvent(SdkVersionCheckError("Startup Error: Invalid Version,\n Please update."))
                    }
                }
            }
        })
        startupManager.initializeNfcSDK()
        onEvent(SdkStartUpInitialised)

    }

    fun setOperatorInfo(
        merchantName: String,
        merchantId: String,
        operatorId: String = merchantId,
        terminalNickname: String = LipaNfcSDK.terminalNickname ?: "",
        externalMerchant: Boolean = true,
        onEvent: (SdkLifeCycleEvent) -> Unit,
    ) {

        LipaNfcSDK.terminalNickname = terminalNickname
        SDKScope.launch {
            channelFlow {
                try {
                    if (terminalNickname.isBlank()) {
                        send(
                            SdkSetOperatorInfoError(
                                "Failed to set operator info. 'terminalNickname' is required"
                            )
                        )
                        return@channelFlow
                    }
                    val res = startupManager.setOperatorInfo(
                        merchantName = merchantName,
                        merchantId = merchantId,
                        operatorId = operatorId,
                        terminalNickname = terminalNickname,
                        externalMerchant = externalMerchant,
                    )
                    if (res.operatorUpdated)
                        send(SdkSetOperatorInfoSuccess)
                    else
                        send(SdkSetOperatorInfoError("Failed to set operator info. ${res.message}"))
                } catch (e: Exception) {
                    send(SdkSetOperatorInfoError("Failed to set operator info"))
                }
                awaitClose { /* KEEP ME RUNNING */ }
            }.collect {
                onEvent(it)
            }
        }
    }

    fun <T : SdkLifeCycleEvent> on(event: KClass<T>, block: (T) -> Unit) {
        map(event to block)
    }

    fun startTransaction(
        amount: Long? = null,
        onEvent: (SDKTransactionEvent) -> Unit,
    ) {
        SDKTransactionScope.launch {
            channelFlow {
                transact(amount) { runBlocking { send(it) } }
                awaitClose { /* KEEP ME RUNNING */ }
            }.collect {
                onEvent(it)
            }
        }
    }

    private fun transact(
        amount: Long?,
        onEvent: (SDKTransactionEvent) -> Unit,
    ) {
        val res = TransactionManager.startTransaction(
            /* transactionListener = */
            object : ITransactionListener {
                override fun onMorePaymentOptionsClicked(
                    morePaymentOptionsData: MorePaymentOptionsData,
                ) {
                    Log.d(TAG, "onMorePaymentOptionsClicked with $morePaymentOptionsData")
                    onEvent(SDKOnMorePaymentOptions(morePaymentOptionsData.amount))
                }

                override fun onTransactionFinished(transactionStatus: TransactionStatus) {
                    Log.d(TAG, "Transaction Finished with status = $transactionStatus")
                    val event = when (transactionStatus.transactionResult) {
                        APPROVED -> SDKOnTransactionApproved(transactionResult = transactionStatus)
                        DECLINED -> SDKOnTransactionDeclined(transactionResult = transactionStatus)
                        MORE_PAYMENT_OPTIONS -> SDKOnMorePaymentOptions(
                            amount = transactionStatus.amount?.toLongOrNull()
                        )

                        CANCELLED, RESTART, TIMEOUT, ERROR -> SDKOnTransactionError(
                            transactionResult = transactionStatus
                        )
                    }
                    onEvent(event)

                }
            },
            /* amount = */ amount,
        )
        val event = when (res) {
            STARTED -> SDKTransactionStarted
            NFC_NOT_ENABLED -> SDKTransactionInitialisationError("NFC_NOT_ENABLED")
        }
        onEvent(event)
    }
}
