import { WebPlugin } from '@capacitor/core';

import { LipaNFCSdkPlugin, SetOperatorResponse, TransactionResult } from './definitions';

export class LipaNFCSdkWeb extends WebPlugin implements LipaNFCSdkPlugin {
  setOperatorInfo(
    options: { 
      merchantId: string; 
      operatorId: string; 
      merchantName: string; 
      terminalNickname: string; 
    }
  ): Promise<SetOperatorResponse> {
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
