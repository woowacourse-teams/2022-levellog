import { rest } from 'msw';

import { feedbacks, levellogs, interviewTeams } from './mockData';

export const feedbackHandlers = [
  rest.post('/api/levellogs/:levellogId/feedbacks', (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.get('/api/levellogs/:levellogId/feedbacks', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(feedbacks));
  }),

  rest.delete('/api/levellogs/:levellogId/feedbacks/:feedbackId', (req, res, ctx) => {
    return res(ctx.status(204));
  }),
];

export const levellogHandlers = [
  rest.post('/api/teams/:teamId/levellogs', (req, res, ctx) => {
    const teamId = +req.params.teamId;
    const contents = req.body as any;
    const randomId = Math.random();
    const levellog = {
      teamId: teamId,
      levellogId: randomId,
      content: contents.content,
    };
    // levellogs.push(levellog);
    // levellogTeams.teams.find(
    //   (team) => team.id === teamId && (team.participants[0].levellogId = randomId),
    // );

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
    const contents = req.body as any;

    levellogs.find(
      (levellog) =>
        levellog.teamId === teamId &&
        levellog.levellogId === levellogId &&
        (levellog.content = contents.content),
    );

    return res(ctx.status(204));
  }),

  rest.delete(`/api/teams/:teamId/levellogs/:levellogId`, (req, res, ctx) => {
    const teamId = +req.params.teamId;
    const levellogId = +req.params.levellogId;
    const index = levellogs.findIndex(
      (levellog) => levellog.teamId === teamId && levellog.levellogId === levellogId,
    );
    levellogs.splice(index, 1);

    interviewTeams.teams.find((team) => {
      if (team.id === teamId) {
        team.participants.find(
          (participant) => participant.levellogId === levellogId && participant.memberId === 300,
          // (participant.levellogId = null),
        );
      }
    });

    return res(ctx.status(204));
  }),
];

export const authHandlers = [
  rest.post('/api/auth/login', (req, res, ctx) => {
    // if (Object.keys(req.body).includes('authorizationCode')) {
    return res(
      ctx.status(200),
      ctx.json({
        id: 300,
        accessToken: 'fflkmdsaklfmkals32$Rmksdlfmlksdm',
        profileUrl: 'https://avatars.githubusercontent.com/u/432423423?v=4',
      }),
    );
    // }

    // return res(ctx.status(403));
  }),

  rest.get('/api/myInfo', (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        id: 300,
        nickname: '페퍼',
        accessToken: 'fflkmdsaklfmkals32$Rmksdlfmlksdm',
        profileUrl: 'https://avatars.githubusercontent.com/u/432423423?v=4',
      }),
    );
    // }

    // return res(ctx.status(403));
  }),
];

export const teamHandlers = [
  rest.get('/api/teams', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(interviewTeams));
  }),

  rest.get('/api/teams/:teamId', (req, res, ctx) => {
    const { teamId } = req.params;
    const chooseTeam = interviewTeams.teams.find((team) => +team.id === +teamId);

    return res(ctx.status(200), ctx.json(chooseTeam));
  }),
];
