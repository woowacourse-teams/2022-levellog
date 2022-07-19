import { rest } from 'msw';

import { feedbacks, levellogs, levellogTeams } from './mockData';

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

export const levellogHandlers = [
  rest.post('/api/teams/:teamId/levellogs', (req, res, ctx) => {
    const teamId = +req.params.teamId;
    const contents = req.body;

    console.log(contents);

    return res(ctx.status(201));
  }),

  rest.get(`/api/teams/:teamId/levellogs/:id`, (req, res, ctx) => {
    const teamId = +req.params.teamId;
    const levellogId = +req.params.id;
    const levellog = levellogs.find(
      (levellog) => levellog.teamId === teamId && levellog.levellogId === levellogId,
    );

    return res(ctx.status(200), ctx.json(levellog));
  }),

  rest.put(`/api/teams/:teamId/levellogs/:id`, (req, res, ctx) => {
    const teamId = +req.params.teamId;
    const levellogId = +req.params.id;
    const contents = req.body;
    console.log(contents);

    return res(ctx.status(204));
  }),

  rest.delete(`/api/teams/:teamId/levellogs/:id`, (req, res, ctx) => {
    const teamId = +req.params.teamId;
    const levellogId = +req.params.id;

    return res(ctx.status(204));
  }),
];

export const loginHandlers = [
  rest.post('/api/auth/login', (req, res, ctx) => {
    if (Object.keys(req.body).includes('authorizationCode')) {
      return res(
        ctx.status(200),
        ctx.json({
          id: 300,
          accessToken: 'fflkmdsaklfmkals32$Rmksdlfmlksdm',
          profileUrl: 'https://avatars.githubusercontent.com/u/432423423?v=4',
        }),
      );
    }

    return res(ctx.status(403));
  }),
];

export const levellogGroupHandlers = [
  rest.get('/api/teams', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(levellogTeams));
  }),

  rest.get('/api/teams/:teamId', (req, res, ctx) => {
    const { teamId } = req.params;
    const chooselevellogTeam = levellogTeams.teams.find(
      (levellogTeam) => +levellogTeam.id === +teamId,
    );

    return res(ctx.status(200), ctx.json(chooselevellogTeam));
  }),
];
