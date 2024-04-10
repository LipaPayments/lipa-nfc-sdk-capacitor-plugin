# lipa-nfc-sdk

A wrapper of the native Android Lipa NFC SDK for Capacitor.

## Install

```bash
npm install @lipa-plugins/lipa-nfc-sdk-android-capacitor-plugin
```

## Setup
After installing the plugin onto your project there is some configuration needed before you are able to import the payment SDK into you project. 

#### Gradle Config
The plugin under the hood uses the Lipa NFC SDK which is a native android sdk and is hosted in remote maven repositories hence the need for gradle configuraion to pull the dependency onto your project. 

To pull the project into your project, the plugin requires a `gradle-local.properties` to created in the `/android/` folder of your project.
The file should contain the following values assigned:
```properties
# maven repo credentials
lipaMavenArtifactsUrl=
lipaNFCSDKMavenArtifactsFeedName=
lipaMavenArtifactsUserName=
lipaCustomMavenArtifactsToken=
```
#### NOTE: THIS FILE SHOULD INLCUDED IN YOUR PROJECT'S `.gitignore` FILE.

For the plugin to read this configuration, it needs the following gradle plugin to be applied.
Here is an example of how to apply it:

Groovy Gradle DSL:\
Legacy plugin application: 
```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "net.saliman:gradle-properties-plugin:1.4.6"
  }
}

apply plugin: "net.saliman.properties"
```

For more information and latest integration guide, [here](https://github.com/stevesaliman/gradle-properties-plugin).

#### SDK initialization
For the plugin to initialise the underlying Lipa NFC SDK it requires a `config.json` to created in the `android/app/src/main/assets/` folder of your project.
The json file should contain the following: 
```json
{
  "env": "DEV",
  "enableBuiltInReceipt": "true",
  "getInTouchText": "Example Text",
  "getInTouchLink": "https://www.example.com/contact-us",
  "paymentLauncherActivityName": "com.example.plugin.MainActivity",
  "tenantId": "XXXX",
  "apiKey": "XXXXX"
}
```
#### NOTE: THIS FILE SHOULD INLCUDED IN YOUR PROJECT'S `.gitignore` FILE.

## Usage

#### Listening to NFC SDK initialization terminal events:
```js
LipaNFCSdk.listenToSdkInitEvents().then((sdkInitEvent) => {
    console.log(sdkInitEvent);
    alert("Sdk inititialization complete.")
}).catch((error) => {
    console.log("Failed to alert. error: ", error);
    const inititializationErrorMessage = JSON.parse(error?.message);
    alert(inititializationErrorMessage?.message)
})
```
_***Note: This function should be called immediately after the JS layer of the app has been loaded and is the UI is showing to the user.**_

#### Linking terminal:
```ts
const linkingResult = await LipaNFCSdk.setOperatorInfo({
    merchantId: "merchant-id",
    terminalNickname: "terminal name",
    operatorId: "operator-id",
    merchantName: "merchant name",
});

if (linkingResult.result == SetOperatorResult.SdkSetOperatorInfoSuccess) {
    // start transaction
} else {
    console.error(linkingResult.message);
}
```

#### Starting a transaction: 
```ts
const transactionResult = await LipaNFCSdk.startTransaction({ amount: 10000 });
```
_***Note: The expected amount type is a Long. Since JS/TS only offer the type number, do ensure that the amount is multiplied by 100 to account for decimals. With that said, the snippet above is for a R 100.00 transaction.**_

## API

<docgen-index>

* [`listenToSdkInitEvents()`](#listentosdkinitevents)
* [`setOperatorInfo(...)`](#setoperatorinfo)
* [`startTransaction(...)`](#starttransaction)
* [Interfaces](#interfaces)
* [Enums](#enums)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### listenToSdkInitEvents()

```typescript
listenToSdkInitEvents() => Promise<SdkResponse<SdkInitializationResult>>
```

Registers SDK initialization events handlers for the initialization terminal states and returns the first terminal event to be emitted by the native layer.
Since this function registers event handlers for terminal states, it means that this will return the result that indicates whether the SDK initialized
successfully or not.

**Returns:** <code>Promise&lt;<a href="#sdkresponse">SdkResponse</a>&lt;<a href="#sdkinitializationresult">SdkInitializationResult</a>&gt;&gt;</code>

**Since:** 0.1.3

--------------------


### setOperatorInfo(...)

```typescript
setOperatorInfo(options: SetOperatorOptions) => Promise<SdkResponse<SetOperatorResult>>
```

Links an operator to the terminal/device.

| Param         | Type                                                              |
| ------------- | ----------------------------------------------------------------- |
| **`options`** | <code><a href="#setoperatoroptions">SetOperatorOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#sdkresponse">SdkResponse</a>&lt;<a href="#setoperatorresult">SetOperatorResult</a>&gt;&gt;</code>

**Since:** 0.0.1

--------------------


### startTransaction(...)

```typescript
startTransaction(options: StartTransactionOptions) => Promise<TransactionResult>
```

Starts a transaction with the specied options.

| Param         | Type                                                                        |
| ------------- | --------------------------------------------------------------------------- |
| **`options`** | <code><a href="#starttransactionoptions">StartTransactionOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#transactionresult">TransactionResult</a>&gt;</code>

**Since:** 0.0.1

--------------------


### Interfaces


#### SdkResponse

| Prop          | Type                | Description                                          | Since |
| ------------- | ------------------- | ---------------------------------------------------- | ----- |
| **`result`**  | <code>T</code>      | The result of calling `setOperatorInfo(...)` .       | 0.0.1 |
| **`message`** | <code>string</code> | The message that specifies why an error has occured. | 0.0.1 |


#### SetOperatorOptions

| Prop                   | Type                | Description                                                                                                                                                                                                                              | Since  |
| ---------------------- | ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------ |
| **`merchantId`**       | <code>string</code> | The merchant id to be used for processing transactions.                                                                                                                                                                                  | 0.0.1  |
| **`merchantName`**     | <code>string</code> | The name of the merchant to be used in for processing a transaction. This is the name used in the SDK screens and the receipt.                                                                                                           | 0.0.1  |
| **`operatorId`**       | <code>string</code> | The id of the operator to be linked to the terminal. This is the user that is signed in on the app, more specifically the user that will be tied to the transactions processed. If this is not specified, the `merchantId` will be used. | 0.0.1. |
| **`terminalNickname`** | <code>string</code> | The friendly name of the terminal.                                                                                                                                                                                                       | 0.0.1  |


#### TransactionResult

| Prop                          | Type                                                            | Description                                                            | Since |
| ----------------------------- | --------------------------------------------------------------- | ---------------------------------------------------------------------- | ----- |
| **`amount`**                  | <code>string</code>                                             | The amount for which the transaction was processed.                    | 0.0.1 |
| **`currency`**                | <code><a href="#currencycodes">CurrencyCodes</a></code>         | The currency used to process the transaction.                          | 0.0.1 |
| **`merchantReference`**       | <code>string</code>                                             | The merchant id used to process the transaction.                       | 0.0.1 |
| **`transactionStatus`**       | <code><a href="#transactionstatus">TransactionStatus</a></code> | The status of the outcome of starting the transaction.                 | 0.0.1 |
| **`shouldDoAnotherPayment`**  | <code><a href="#boolean">Boolean</a></code>                     | Indicates whether another transaction should be started or not.        | 0.0.1 |
| **`shouldShowReceiptScreen`** | <code><a href="#boolean">Boolean</a></code>                     | Indicated whether a receipt should be shown or not.                    | 0.0.1 |
| **`receipt`**                 | <code><a href="#receiptmodel">ReceiptModel</a></code>           | The receipt data of the processed transaction.                         | 0.0.1 |
| **`receiptHTML`**             | <code>string</code>                                             | The html version of the receipt that the SDK used to show the receipt. | 0.0.1 |


#### CurrencyCodes

| Prop                 | Type                | Description                                                          | Since |
| -------------------- | ------------------- | -------------------------------------------------------------------- | ----- |
| **`currencyName`**   | <code>string</code> | The name of the currency used in for processing the transaction.     | 0.0.1 |
| **`currencyCode`**   | <code>string</code> | The currency code as per ISO 4217 standard.                          | 0.0.1 |
| **`currencySymbol`** | <code>string</code> | The currency symbol or currency sign used to denote a currency unit. | 0.0.1 |


#### Boolean

| Method      | Signature        | Description                                          |
| ----------- | ---------------- | ---------------------------------------------------- |
| **valueOf** | () =&gt; boolean | Returns the primitive value of the specified object. |


#### ReceiptModel

| Prop                      | Type                | Description                                                                        | Since |
| ------------------------- | ------------------- | ---------------------------------------------------------------------------------- | ----- |
| **`result`**              | <code>string</code> | This is the result of the transaction.                                             | 0.0.1 |
| **`amount`**              | <code>string</code> | The amount for which the transaction was processed.                                | 0.0.1 |
| **`currencySymbol`**      | <code>string</code> | The currency used to process the transaction.                                      | 0.0.1 |
| **`merchantName`**        | <code>string</code> | The name of the merchant for which the transaction was processed.                  | 0.0.1 |
| **`merchantId`**          | <code>string</code> | The merchant id used to process the transaction.                                   | 0.0.1 |
| **`terminalId`**          | <code>string</code> | The id of the terminal/device used to process the transaction.                     | 0.0.1 |
| **`cardPan`**             | <code>string</code> | This is the last 4 digits of the PAN of the card that was used in the transaction. | 0.0.1 |
| **`expirationDate`**      | <code>string</code> | This is the expiry date of the card that was used in the transaction.              | 0.0.1 |
| **`transactionType`**     | <code>string</code> | This is the type which the transaction was processed as.                           | 0.0.1 |
| **`authorizationNumber`** | <code>string</code> | This is the transaction authorization number                                       | 0.0.1 |
| **`cardLabel`**           | <code>string</code> | This indicates the type of the card that was used in the transaction.              | 0.0.1 |
| **`aid`**                 | <code>string</code> | This is the application id as per EMV TLVTags.                                     | 0.0.1 |
| **`responseCode`**        | <code>string</code> | This is the transaction response code as per EMV TVLTags.                          | 0.0.1 |
| **`tvr`**                 | <code>string</code> | This is the terminal verification result.                                          | 0.0.1 |


#### StartTransactionOptions

| Prop         | Type                | Description                                     | Since |
| ------------ | ------------------- | ----------------------------------------------- | ----- |
| **`amount`** | <code>number</code> | The amount to be processed for the transaction. | 0.0.1 |


### Enums


#### SdkInitializationResult

| Members                    | Description                                                                          | Since |
| -------------------------- | ------------------------------------------------------------------------------------ | ----- |
| **`SdkStartUpSuccess`**    | Indicates that SDK initialization was successful.                                    | 0.1.3 |
| **`SdkStartUpError`**      | Indicates that an error occured during the SDK initialization start-up process.      | 0.1.3 |
| **`SdkVersionCheckError`** | Indicates that an error occured during the SDK initialization version-check process. | 0.1.3 |


#### SetOperatorResult

| Members                         | Description                                                             | Since |
| ------------------------------- | ----------------------------------------------------------------------- | ----- |
| **`SdkSetOperatorInfoError`**   | Indicates that an error occured while setting the operator information. | 0.0.1 |
| **`SdkSetOperatorInfoSuccess`** | Indicates that setting the operator information was successful.         | 0.0.1 |


#### TransactionStatus

| Members                    | Description                                                                                                                                                                                                                     | Since |
| -------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----- |
| **`APPROVED`**             | Indicates that the transaction was approved.                                                                                                                                                                                    | 0.0.1 |
| **`DECLINED`**             | Indicates that the transaction was declined.                                                                                                                                                                                    | 0.0.1 |
| **`MORE_PAYMENT_OPTIONS`** | Indicates that the more payment options link was clicked on the SDK tap screen. When this is returned, an alternative payment method needs to be provided the SDK will not process that payment.                                | 0.0.1 |
| **`CANCELLED`**            | Indicates that the transaction was cancelled.                                                                                                                                                                                   | 0.0.1 |
| **`RESTART`**              | Indicates that `startTransaction(...)` with the same parameters needs to be called again. Essentially indicates that the transaction should be restarted.                                                                       | 0.0.1 |
| **`TIMEOUT`**              | Indicates that processing the transaction took longer than the internal SDK configured timeout of 2 minutes. Usually indicates that the internet connection needs to be checked before trying to process the transaction again. | 0.0.1 |
| **`ERROR`**                | Indicates that the was an error while processing the transaction.                                                                                                                                                               | 0.0.1 |

</docgen-api>
