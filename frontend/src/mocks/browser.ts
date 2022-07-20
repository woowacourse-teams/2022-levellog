import { setupWorker } from 'msw';

import {
  feedbackHandlers,
  levellogHandlers,
  loginHandlers,
  levellogGroupHandlers,
} from './handler';

export const worker = setupWorker(
  ...feedbackHandlers,
  ...levellogHandlers,
  ...loginHandlers,
  ...levellogGroupHandlers,
);
