import { setupWorker } from 'msw';
import { handlers, loginHandler } from './handler';

export const worker = setupWorker(...handlers, ...loginHandler);
