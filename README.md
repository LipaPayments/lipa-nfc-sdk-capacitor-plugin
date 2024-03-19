# lipa-nfc-sdk

A wrapper of the native Android Lipa NFC SDK for Capacitor.

## Install

```bash
npm install lipa-nfc-sdk
npx cap sync
```

## API

<docgen-index>

* [`initialise(...)`](#initialise)
* [`setOperatorInfo(...)`](#setoperatorinfo)
* [`startTransaction(...)`](#starttransaction)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)
* [Enums](#enums)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### initialise(...)

```typescript
initialise(options: { apiKey: string; tenantId: string; env: Env; getInTouchLink: string; getInTouchText: string; enableBuiltInReceiptScreen: boolean; }) => Promise<SdkLifecycleEventResponse>
```

| Param         | Type                                                                                                                                                                 |
| ------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`options`** | <code>{ apiKey: string; tenantId: string; env: <a href="#env">Env</a>; getInTouchLink: string; getInTouchText: string; enableBuiltInReceiptScreen: boolean; }</code> |

**Returns:** <code>Promise&lt;<a href="#sdklifecycleeventresponse">SdkLifecycleEventResponse</a>&gt;</code>

--------------------


### setOperatorInfo(...)

```typescript
setOperatorInfo(options: { merchantId: string; operatorId: string; merchantName: string; terminalNickname: string; }) => Promise<SdkLifecycleEventResponse>
```

| Param         | Type                                                                                                     |
| ------------- | -------------------------------------------------------------------------------------------------------- |
| **`options`** | <code>{ merchantId: string; operatorId: string; merchantName: string; terminalNickname: string; }</code> |

**Returns:** <code>Promise&lt;<a href="#sdklifecycleeventresponse">SdkLifecycleEventResponse</a>&gt;</code>

--------------------


### startTransaction(...)

```typescript
startTransaction(options: { amount: number; }) => Promise<TransactionResult>
```

| Param         | Type                             |
| ------------- | -------------------------------- |
| **`options`** | <code>{ amount: number; }</code> |

**Returns:** <code>Promise&lt;<a href="#transactionresult">TransactionResult</a>&gt;</code>

--------------------


### Interfaces


#### SdkLifecycleEventResponse

| Prop          | Type                                                            |
| ------------- | --------------------------------------------------------------- |
| **`event`**   | <code><a href="#sdklifecycleevent">SdkLifecycleEvent</a></code> |
| **`message`** | <code>string</code>                                             |


#### String

Allows manipulation and formatting of text strings and determination and location of substrings within strings.

| Prop         | Type                | Description                                                  |
| ------------ | ------------------- | ------------------------------------------------------------ |
| **`length`** | <code>number</code> | Returns the length of a <a href="#string">String</a> object. |

| Method                | Signature                                                                                                                      | Description                                                                                                                                   |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------- |
| **toString**          | () =&gt; string                                                                                                                | Returns a string representation of a string.                                                                                                  |
| **charAt**            | (pos: number) =&gt; string                                                                                                     | Returns the character at the specified index.                                                                                                 |
| **charCodeAt**        | (index: number) =&gt; number                                                                                                   | Returns the Unicode value of the character at the specified location.                                                                         |
| **concat**            | (...strings: string[]) =&gt; string                                                                                            | Returns a string that contains the concatenation of two or more strings.                                                                      |
| **indexOf**           | (searchString: string, position?: number \| undefined) =&gt; number                                                            | Returns the position of the first occurrence of a substring.                                                                                  |
| **lastIndexOf**       | (searchString: string, position?: number \| undefined) =&gt; number                                                            | Returns the last occurrence of a substring in the string.                                                                                     |
| **localeCompare**     | (that: string) =&gt; number                                                                                                    | Determines whether two strings are equivalent in the current locale.                                                                          |
| **match**             | (regexp: string \| <a href="#regexp">RegExp</a>) =&gt; <a href="#regexpmatcharray">RegExpMatchArray</a> \| null                | Matches a string with a regular expression, and returns an array containing the results of that search.                                       |
| **replace**           | (searchValue: string \| <a href="#regexp">RegExp</a>, replaceValue: string) =&gt; string                                       | Replaces text in a string, using a regular expression or search string.                                                                       |
| **replace**           | (searchValue: string \| <a href="#regexp">RegExp</a>, replacer: (substring: string, ...args: any[]) =&gt; string) =&gt; string | Replaces text in a string, using a regular expression or search string.                                                                       |
| **search**            | (regexp: string \| <a href="#regexp">RegExp</a>) =&gt; number                                                                  | Finds the first substring match in a regular expression search.                                                                               |
| **slice**             | (start?: number \| undefined, end?: number \| undefined) =&gt; string                                                          | Returns a section of a string.                                                                                                                |
| **split**             | (separator: string \| <a href="#regexp">RegExp</a>, limit?: number \| undefined) =&gt; string[]                                | Split a string into substrings using the specified separator and return them as an array.                                                     |
| **substring**         | (start: number, end?: number \| undefined) =&gt; string                                                                        | Returns the substring at the specified location within a <a href="#string">String</a> object.                                                 |
| **toLowerCase**       | () =&gt; string                                                                                                                | Converts all the alphabetic characters in a string to lowercase.                                                                              |
| **toLocaleLowerCase** | (locales?: string \| string[] \| undefined) =&gt; string                                                                       | Converts all alphabetic characters to lowercase, taking into account the host environment's current locale.                                   |
| **toUpperCase**       | () =&gt; string                                                                                                                | Converts all the alphabetic characters in a string to uppercase.                                                                              |
| **toLocaleUpperCase** | (locales?: string \| string[] \| undefined) =&gt; string                                                                       | Returns a string where all alphabetic characters have been converted to uppercase, taking into account the host environment's current locale. |
| **trim**              | () =&gt; string                                                                                                                | Removes the leading and trailing white space and line terminator characters from a string.                                                    |
| **substr**            | (from: number, length?: number \| undefined) =&gt; string                                                                      | Gets a substring beginning at the specified location and having the specified length.                                                         |
| **valueOf**           | () =&gt; string                                                                                                                | Returns the primitive value of the specified object.                                                                                          |


#### RegExpMatchArray

| Prop        | Type                |
| ----------- | ------------------- |
| **`index`** | <code>number</code> |
| **`input`** | <code>string</code> |


#### RegExp

| Prop             | Type                 | Description                                                                                                                                                          |
| ---------------- | -------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`source`**     | <code>string</code>  | Returns a copy of the text of the regular expression pattern. Read-only. The regExp argument is a Regular expression object. It can be a variable name or a literal. |
| **`global`**     | <code>boolean</code> | Returns a <a href="#boolean">Boolean</a> value indicating the state of the global flag (g) used with a regular expression. Default is false. Read-only.              |
| **`ignoreCase`** | <code>boolean</code> | Returns a <a href="#boolean">Boolean</a> value indicating the state of the ignoreCase flag (i) used with a regular expression. Default is false. Read-only.          |
| **`multiline`**  | <code>boolean</code> | Returns a <a href="#boolean">Boolean</a> value indicating the state of the multiline flag (m) used with a regular expression. Default is false. Read-only.           |
| **`lastIndex`**  | <code>number</code>  |                                                                                                                                                                      |

| Method      | Signature                                                                     | Description                                                                                                                   |
| ----------- | ----------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| **exec**    | (string: string) =&gt; <a href="#regexpexecarray">RegExpExecArray</a> \| null | Executes a search on a string using a regular expression pattern, and returns an array containing the results of that search. |
| **test**    | (string: string) =&gt; boolean                                                | Returns a <a href="#boolean">Boolean</a> value that indicates whether or not a pattern exists in a searched string.           |
| **compile** | () =&gt; this                                                                 |                                                                                                                               |


#### RegExpExecArray

| Prop        | Type                |
| ----------- | ------------------- |
| **`index`** | <code>number</code> |
| **`input`** | <code>string</code> |


#### Boolean

| Method      | Signature        | Description                                          |
| ----------- | ---------------- | ---------------------------------------------------- |
| **valueOf** | () =&gt; boolean | Returns the primitive value of the specified object. |


### Type Aliases


#### TransactionResult

<code>{ amount: string, currency: <a href="#currencycodes">CurrencyCodes</a>, merchantReference: <a href="#string">String</a>, transactionStatus: <a href="#transactionstatus">TransactionStatus</a>, shouldDoAnotherPayment: <a href="#boolean">Boolean</a>, shouldShowReceiptScreen: <a href="#boolean">Boolean</a>, receipt: <a href="#receiptmodel">ReceiptModel</a>, receiptHTML: string, }</code>


#### CurrencyCodes

<code>{ currencyName: string, currencyCode: string, currencySymbol: string }</code>


#### ReceiptModel

<code>{ }</code>


### Enums


#### SdkLifecycleEvent

| Members                         |
| ------------------------------- |
| **`SdkConfigured`**             |
| **`SdkDeviceState`**            |
| **`SdkInitialised`**            |
| **`SdkSetOperatorInfoSuccess`** |
| **`SdkStartUpInitialised`**     |
| **`SdkStartUpSuccess`**         |
| **`SdkVersionCheck`**           |
| **`SdkSetOperatorInfoError`**   |
| **`SdkStartUpError`**           |
| **`SdkVersionCheckError`**      |


#### Env

| Members       |
| ------------- |
| **`DEV`**     |
| **`TESTING`** |
| **`PROD`**    |


#### TransactionStatus

| Members                    |
| -------------------------- |
| **`APPROVED`**             |
| **`DECLINED`**             |
| **`MORE_PAYMENT_OPTIONS`** |
| **`CANCELLED`**            |
| **`RESTART`**              |
| **`TIMEOUT`**              |
| **`ERROR`**                |

</docgen-api>
