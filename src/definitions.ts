export interface LipaNFCSdkPlugin {

  /**
   *  Registers SDK initialization events handlers for the initialization terminal states and returns the first terminal event to be emitted by the native layer.
   *  Since this function registers event handlers for terminal states, it means that this will return the result that indicates whether the SDK initialized
   *  successfully or not.
   *  @since 0.1.3
   * 
   */
  listenToSdkInitEvents(): Promise<SdkResponse<SdkInitializationResult>>;

  /**
   *  Links an operator to the terminal/device.
   *  @since 0.0.1
   * 
   */
  setOperatorInfo(options: SetOperatorOptions): Promise<SdkResponse<SetOperatorResult>>;

  /**
   *  Starts a transaction with the specied options.
   *  @since 0.0.1
   */
  startTransaction(options: StartTransactionOptions): Promise<TransactionResult>;

}

export interface SdkResponse<T> {
  /**
   *  The result of calling `setOperatorInfo(...)` .
   *  
   *  @since 0.0.1
   */
  result: T;
  /**
   *  The message that specifies why an error has occured.
   *  
   *  @since 0.0.1
   */
  message?: string
}

export enum SdkInitializationResult {
  /**
   * Indicates that SDK initialization was successful.
   * 
   * @since 0.1.3
   */
  SdkStartUpSuccess,

  /**
   * Indicates that an error occured during the SDK initialization start-up process.
   * 
   * @since 0.1.3
   */
  SdkStartUpError,

  /**
   * Indicates that an error occured during the SDK initialization version-check process.
   * 
   * @since 0.1.3
   */
  SdkVersionCheckError,
}

export interface SetOperatorOptions {
  /**
   *  The merchant id to be used for processing transactions.
   *  
   *  @since 0.0.1
   */
  merchantId: string

  /**
   *  The name of the merchant to be used in for processing a transaction.
   *  This is the name used in the SDK screens and the receipt.
   *  
   *  @since 0.0.1
   */
  merchantName: string

  /**
   *  The id of the operator to be linked to the terminal.
   *  This is the user that is signed in on the app, more specifically the user that will be tied to the transactions processed.
   *  If this is not specified, the `merchantId` will be used.
   *  
   *  @since 0.0.1.
   */
  operatorId?: string


  /**
   *  The friendly name of the terminal.
   *  
   *  @since 0.0.1
   */
  terminalNickname?: string
}

export interface StartTransactionOptions {
  /**
   *  The amount to be processed for the transaction.
   *  
   *  @since 0.0.1
   */
  amount: number
}

export enum SetOperatorResult {
  /**
   * Indicates that an error occured while setting the operator information.
   * 
   * @since 0.0.1
   */
  SdkSetOperatorInfoError,

  /**
   * Indicates that setting the operator information was successful.
   * 
   * @since 0.0.1
   */
  SdkSetOperatorInfoSuccess,
}


export interface TransactionResult {
  /**
   *  The amount for which the transaction was processed.
   *  
   *  @since 0.0.1
   */
  amount: string

  /**
   *  The currency used to process the transaction.
   *  
   *  @since 0.0.1
   */
  currency: CurrencyCodes

  /**
   *  The merchant id used to process the transaction.
   *  
   *  @since 0.0.1
   */
  merchantReference: string

  /**
   *  The status of the outcome of starting the transaction.
   *  
   *  @since 0.0.1
   */
  transactionStatus: TransactionStatus

  /**
   *  Indicates whether another transaction should be started or not.
   *  
   *  @since 0.0.1
   */
  shouldDoAnotherPayment: Boolean

  /**
   *  Indicated whether a receipt should be shown or not.
   *  
   *  @since 0.0.1
   */
  shouldShowReceiptScreen: Boolean

  /**
   *  The receipt data of the processed transaction.
   *  
   *  @since 0.0.1
   */
  receipt: ReceiptModel

  /**
   *  The html version of the receipt that the SDK used to show the receipt.
   *  
   *  @since 0.0.1
   */
  receiptHTML: string
}

export interface ReceiptModel {
  /**
   * This is the result of the transaction.
   * 
   * @since 0.0.1
   */
  result: string

  /**
   * The amount for which the transaction was processed.
   * 
   * @since 0.0.1
   */
  amount: string

  /**
   * The currency used to process the transaction.
   * 
   * @since 0.0.1
   */
  currencySymbol: string

  /**
   * The name of the merchant for which the transaction was processed.
   * 
   * @since 0.0.1
   */
  merchantName: string

  /**
   * The merchant id used to process the transaction.
   * 
   * @since 0.0.1
   */
  merchantId: string

  /**
   * The id of the terminal/device used to process the transaction.
   * 
   * @since 0.0.1
   */
  terminalId: string

  /**
   * This is the last 4 digits of the PAN of the card that was used in the transaction.
   * 
   * @since 0.0.1
   */
  cardPan: string

  /**
   * This is the expiry date of the card that was used in the transaction.
   * 
   * @since 0.0.1
   */
  expirationDate: string
  
  /**
   * This is the type which the transaction was processed as.
   * 
   * @since 0.0.1
   */
  transactionType: string

  /**
   * This is the transaction authorization number
   * 
   * @since 0.0.1
   */
  authorizationNumber: string

  /**
   * This indicates the type of the card that was used in the transaction.
   * 
   * @since 0.0.1
   */
  cardLabel: string

  /**
   * This is the application id as per EMV TLVTags.
   * 
   * @since 0.0.1
   */
  aid: string

  /**
   * This is the transaction response code as per EMV TVLTags.
   * 
   * @since 0.0.1
   */
  responseCode: string

  /**
   * This is the terminal verification result.
   * 
   * @since 0.0.1
   */
  tvr: string
}

export enum TransactionStatus {
  /**
   * Indicates that the transaction was approved.
   * 
   *  @since 0.0.1
   */
  APPROVED,

  /**
   * Indicates that the transaction was declined.
   * 
   *  @since 0.0.1
   */
  DECLINED,

  /**
   * Indicates that the more payment options link was clicked on the SDK tap screen.
   * When this is returned, an alternative payment method needs to be provided the SDK will not process that payment. 
   * 
   *  @since 0.0.1
   */
  MORE_PAYMENT_OPTIONS,

  /**
   * Indicates that the transaction was cancelled.
   * 
   *  @since 0.0.1
   */
  CANCELLED,

  /**
   * Indicates that `startTransaction(...)` with the same parameters needs to be called again.
   * Essentially indicates that the transaction should be restarted.
   * 
   *  @since 0.0.1
   */
  RESTART,

  /**
   * Indicates that processing the transaction took longer than the internal SDK configured timeout of 2 minutes.
   * Usually indicates that the internet connection needs to be checked before trying to process the transaction again.
   * 
   *  @since 0.0.1
   */
  TIMEOUT,

  /**
   * Indicates that the was an error while processing the transaction.
   * 
   *  @since 0.0.1
   */
  ERROR,
}

export interface CurrencyCodes {
  /**
   *  The name of the currency used in for processing the transaction.
   *  
   *  @since 0.0.1
   */
  currencyName: string,

  /**
   * The currency code as per ISO 4217 standard.
   *  
   *  @since 0.0.1
   */
  currencyCode: string,

  /**
   *  The currency symbol or currency sign used to denote a currency unit.
   *  
   *  @since 0.0.1
   */
  currencySymbol: string
}