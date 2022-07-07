import { rest } from 'msw';

export const handlers = [
  rest.post('/api/feedbacks', (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.get('/api/feedbacks', (req, res, ctx) => {
    return res(ctx.status(200));
  }),

  rest.delete('/api/feedbacks/:id', (req, res, ctx) => {
    return res(ctx.status(204));
  }),
];
