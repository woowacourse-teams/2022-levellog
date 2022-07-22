import { setupWorker } from 'msw';

import { feedbackHandlers, levellogHandlers, authHandlers, levellogGroupHandlers } from './handler';

export const worker = setupWorker(
  ...feedbackHandlers,
  ...levellogHandlers,
  ...authHandlers,
  ...levellogGroupHandlers,
);
