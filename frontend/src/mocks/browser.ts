import { setupWorker } from 'msw';

import { feedbackHandlers, levellogHandlers, authHandlers, teamHandlers } from './handler';

export const worker = setupWorker(
  ...feedbackHandlers,
  ...levellogHandlers,
  ...authHandlers,
  ...teamHandlers,
);
