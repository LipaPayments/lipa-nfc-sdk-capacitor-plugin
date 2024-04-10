import { SplashScreen } from '@capacitor/splash-screen';
import config from '../assets/config/terminal-config.json';
import { LipaNFCSdk, SetOperatorResult } from '@lipa-plugins/lipa-nfc-sdk-android-capacitor-plugin';

window.customElements.define(
  'capacitor-welcome',
  class extends HTMLElement {
    constructor() {
      super();

      LipaNFCSdk.listenToSdkInitEvents().then((sdkInitEvent) => {
        console.log(sdkInitEvent);
        alert("Sdk inititialization complete.")
      }).catch((error) => {
        console.log("Failed to alert. error: ", error);
        const inititializationErrorMessage = JSON.parse(error?.message);
        alert(inititializationErrorMessage?.message)
      })

      SplashScreen.hide();

      const root = this.attachShadow({ mode: 'open' });

      root.innerHTML = `
    <style>
      :host {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
        display: block;
        width: 100%;
        height: 100%;
      }
      h1, h2, h3, h4, h5 {
        text-transform: uppercase;
      }
      .button {
        display: inline-block;
        padding: 10px;
        background-color: #73B5F6;
        color: #fff;
        font-size: 0.9em;
        border: 0;
        border-radius: 3px;
        text-decoration: none;
        cursor: pointer;
      }
      main {
        padding: 15px;
      }
      main hr { height: 1px; background-color: #eee; border: 0; }
      main h1 {
        font-size: 1.4em;
        text-transform: uppercase;
        letter-spacing: 1px;
      }
      main h2 {
        font-size: 1.1em;
      }
      main h3 {
        font-size: 0.9em;
      }
      main p {
        color: #333;
      }
      main pre {
        white-space: pre-line;
      }
      .loader {
        border: 16px solid #f3f3f3; /* Light grey */
        border-top: 16px solid #3498db; /* Blue */
        border-radius: 50%;
        width: 120px;
        height: 120px;
        animation: spin 2s linear infinite;
      }
      
      @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
      }
    </style>
    <div>
      <capacitor-welcome-titlebar>
        <h1>Lipa NFC SDK Capacitor Plugin showcase</h1>
      </capacitor-welcome-titlebar>
      <main>
        <p>
          This demo shows how to use the Lipa NFC SDK Capacitor Plugin.  
        </p>
        <h2>Getting Started</h2>
        <p>
          This button confifures the terminal.<br/><strong>ONLY Press after opening the app for the FIRST TIME</strong>
        </p>
        <p>
          <button class="button" id="set-operator">Set Operator</button>
        </p>
        <p>
          This button shows how the transaction is processed by the Lipa NFC SDK. Thanks for donating...
        </p>
        <p>
          <button class="button" id="start-transaction">Donate</button>
        </p>
        <p>
          <img id="image" style="max-width: 100%">
        </p>
      </main>
    </div>
    `;
    }

    connectedCallback() {
      const self = this;

      self.shadowRoot.querySelector('#set-operator').addEventListener('click', async function (e) {
        try {
          const linkingResult = await LipaNFCSdk.setOperatorInfo({
            merchantId: config.merchantId,
            terminalNickname: config.terminalNickName,
            operatorId: config.operatorId,
            merchantName: config.merchantName,
          });
          if (linkingResult.result == SetOperatorResult.SdkSetOperatorInfoSuccess){
            alert("Set operator successful.\nHappy transacting!")
          } else {
            alert(linkingResult.message);
          }
        } catch (e) {
          console.warn('User cancelled', e);
        }
      });

      self.shadowRoot.querySelector('#start-transaction').addEventListener('click', async function (e) {
        try {
          const transactionResult = await LipaNFCSdk.startTransaction({
            amount: 10000
          });
          console.log("Transaction done", transactionResult);
        } catch (e) {
          console.warn('User cancelled', e);
        }
      });
    }
  }
);

window.customElements.define(
  'capacitor-welcome-titlebar',
  class extends HTMLElement {
    constructor() {
      super();
      const root = this.attachShadow({ mode: 'open' });
      root.innerHTML = `
    <style>
      :host {
        position: relative;
        display: block;
        padding: 15px 15px 15px 15px;
        text-align: center;
        background-color: #73B5F6;
      }
      ::slotted(h1) {
        margin: 0;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
        font-size: 0.9em;
        font-weight: 600;
        color: #fff;
      }
    </style>
    <slot></slot>
    `;
    }
  }
);
