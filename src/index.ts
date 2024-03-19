import { registerPlugin } from '@capacitor/core';

import type { LipaNFCSdkPlugin } from './definitions';

const LipaNFCSdk = registerPlugin<LipaNFCSdkPlugin>('LipaNFCSdk', {
  web: () => import('./web').then(m => new m.LipaNFCSdkWeb()),
});

export * from './definitions';
export { LipaNFCSdk };
