import { rest } from 'msw';
import { SERVER_PATH } from '../constants/constants';
import { feedbacks, levellog } from './mockData';

export const handlers = [
  // 피드백 CRUD MOCKING
  rest.post(SERVER_PATH.FEEDBACKS, (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.get(SERVER_PATH.FEEDBACKS, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(feedbacks));
  }),

  rest.delete(`${SERVER_PATH.FEEDBACKS}/:id`, (req, res, ctx) => {
    const id = +req.params.id;
    return res(ctx.status(204));
  }),

  // 레벨로그 CRUD MOCKING
  rest.post(SERVER_PATH.LEVELLOGS, (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.get(`${SERVER_PATH.LEVELLOGS}/:id`, (req, res, ctx) => {
    const id = +req.params.id;
    return res(ctx.status(200), ctx.json(levellog));
  }),
];
