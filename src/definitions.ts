export interface LipaNFCSdkPlugin {

  /**
   * 
   */
  initialise(
    options: {
      apiKey: string,
      tenantId: string,
      env: Env,
      getInTouchLink: string,
      getInTouchText: string,
      enableBuiltInReceiptScreen: boolean,
    }
  ): Promise<SdkLifecycleEventResponse>;

  /**
   * 
   */
  setOperatorInfo(
    options: {
      merchantId: string,
      operatorId: string,
      merchantName: string,
      terminalNickname: string,
    }
  ): Promise<SdkLifecycleEventResponse>;

  /**
   * 
   */
  startTransaction(
    options: {
      amount: number,
    }
  ): Promise<TransactionResult>;

}

// type SdkLifecycleEventCallback = (lifecycleEvent: SdkLifecycleEvent) => void;

/**
 * 
 */
export enum Env {
  DEV, TESTING, PROD
}

/**
 * 
 */
export enum SdkLifecycleEvent {
  /**
   * 
   */
  SdkConfigured,

  /**
   * 
   */
  SdkDeviceState,

  /**
   * 
   */
  SdkInitialised,

  /**
   * 
   */
  SdkSetOperatorInfoSuccess,

  /**
   * 
   */
  SdkStartUpInitialised,

  /**
   * 
   */
  SdkStartUpSuccess,

  /**
   * 
   */
  SdkVersionCheck,

  /**
   * 
   */
  SdkSetOperatorInfoError,

  /**
   * 
   */
  SdkStartUpError,

  /**
   * 
   */
  SdkVersionCheckError,
}

export interface SdkLifecycleEventResponse {
  event: SdkLifecycleEvent;
  message?: string
}

export type TransactionResult = {
  amount: string,
  currency: CurrencyCodes,
  merchantReference: String,
  transactionStatus: TransactionStatus,
  shouldDoAnotherPayment: Boolean,
  shouldShowReceiptScreen: Boolean,
  receipt: ReceiptModel,
  receiptHTML: string,
}

export type ReceiptModel = {

}

export enum TransactionStatus {
  /**
   * 
   */
  APPROVED,

  /**
   * 
   */
  DECLINED,

  /**
   * 
   */
  MORE_PAYMENT_OPTIONS,

  /**
   * 
   */
  CANCELLED,

  /**
   * 
   */
  RESTART,

  /**
   * 
   */
  TIMEOUT,

  /**
   * 
   */
  ERROR,
}

export type CurrencyCodes = {
  currencyName: string,
  currencyCode: string,
  currencySymbol: string
}