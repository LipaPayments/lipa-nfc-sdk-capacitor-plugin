import { WebPlugin } from '@capacitor/core';

import { LipaNFCSdkPlugin, SdkInitializationResult, SdkResponse, SetOperatorResult, TransactionResult } from './definitions';

export class LipaNFCSdkWeb extends WebPlugin implements LipaNFCSdkPlugin {
  listenToSdkInitEvents(): Promise<SdkResponse<SdkInitializationResult>> {
    throw new Error('Method not implemented.');
  }
  setOperatorInfo(
    options: { 
      merchantId: string; 
      operatorId: string; 
      merchantName: string; 
      terminalNickname: string; 
    }
  ): Promise<SdkResponse<SetOperatorResult>> {
    console.log(options)
    throw new Error('Method not implemented.');
  }
  startTransaction(
    options: { amount: number; }
  ): Promise<TransactionResult> {
    console.log(options)
    throw new Error('Method not implemented.');
  }

}
