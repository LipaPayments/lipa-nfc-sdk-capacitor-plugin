import { WebPlugin } from '@capacitor/core';

import { Env, LipaNFCSdkPlugin, SdkLifecycleEventResponse, TransactionResult } from './definitions';

export class LipaNFCSdkWeb extends WebPlugin implements LipaNFCSdkPlugin {
  setOperatorInfo(
    options: { 
      merchantId: string; 
      operatorId: string; 
      merchantName: string; 
      terminalNickname: string; 
    }
  ): Promise<SdkLifecycleEventResponse> {
    console.log(options)
    throw new Error('Method not implemented.');
  }
  startTransaction(
    options: { amount: number; }
  ): Promise<TransactionResult> {
    console.log(options)
    throw new Error('Method not implemented.');
  }
  
  initialise(
    options: {
      apiKey: string, 
      tenantId: string, 
      env: Env, 
      getInTouchLink: string, 
      getInTouchText: string, 
      enableBuiltInReceiptScreen: boolean, 
    }
  ): Promise<SdkLifecycleEventResponse> {
    console.log(options.apiKey)
    console.log(options.tenantId)
    console.log(options.env)
    console.log(options.getInTouchLink)
    console.log(options.getInTouchText)
    console.log(options.enableBuiltInReceiptScreen)
    throw new Error('Method not implemented.');
  }

}
