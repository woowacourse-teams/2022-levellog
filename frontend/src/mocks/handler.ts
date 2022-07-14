import { rest } from 'msw';

export const handlers = [
  rest.post('/api/feedbacks', (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.get('/api/feedbacks', (req, res, ctx) => {
    return res(ctx.status(200));
  }),

  // rest.delete(`/api/feedbacks/${id}`, (req, res, ctx) => {
  //   return res(ctx.status(204));
  // }),
];

export const loginHandler = [
  rest.post('/api/auth/login', (req, res, ctx) => {
    if (Object.keys(req.body).includes('authorizationCode')) {
      return res(
        ctx.status(200),
        ctx.json({
          accessToken: 'fflkmdsaklfmkals32$Rmksdlfmlksdm',
          profileUrl: 'https://avatars.githubusercontent.com/u/432423423?v=4',
        }),
      );
    }

    return res(ctx.status(403));
  }),
];
