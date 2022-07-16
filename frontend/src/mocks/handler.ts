import { rest } from 'msw';

import { feedbacks, levellog, levellogGroups } from './mockData';

// 피드백 CRUD MOCKING
export const feedbackHandlers = [
  rest.post('/api/feedbacks', (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.get('/api/feedbacks', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(feedbacks));
  }),

  rest.delete('/api/feedbacks/:id', (req, res, ctx) => {
    const id = +req.params.id;
    return res(ctx.status(204));
  }),
];

// 레벨로그 CRUD MOCKING
export const levellogHandlers = [
  rest.post('/api/levellogs', (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.get(`/api/levellogs/:id`, (req, res, ctx) => {
    const id = +req.params.id;
    return res(ctx.status(200), ctx.json(levellog));
  }),
];

export const loginHandlers = [
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

export const levellogGroupHandlers = [
  rest.get('/api/groups', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(levellogGroups));
  }),

  rest.get('/api/groups/:groupId', (req, res, ctx) => {
    const { groupId } = req.params;
    const levellogGroup = levellogGroups.group.find(
      (levellogGroup) => levellogGroup.id === +groupId,
    );

    return res(ctx.status(200), ctx.json(levellogGroup));
  }),
];
