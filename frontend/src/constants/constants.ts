export const API_URL = 'https://dev.levellog.app/api';
export const LOGIN_URL =
  'https://github.com/login/oauth/authorize?client_id=fc4c9ab6e6d189931371&redirect_uri=';

export const ROUTES_PATH = Object.freeze({
  HOME: '/',
  FEEDBACK_ROUTE: '/teams/:teamId/levellogs/:levellogId/feedbacks',
  FEEDBACK_ADD: `/teams/:teamId/levellogs/:levellogId/feedbacks/add`,
  LOGIN: '/login',
  LEVELLOG: '/levellog',
  LEVELLOG_ADD: '/levellog/add',
  LEVELLOG_ADD_ROUTE: '/levellog/add/:teamId',
  LEVELLOG_MODIFY: '/levellog/modify/teams/:teamId/levellogs/:levellogId',
  INTERVIEW_TEAMS: '/interview/teams',
  INTERVIEW_TEAMS_DETAIL: '/interview/teams/:teamId',
  NOT_FOUND: '*',
});

export const LOGIN_PATH = Object.freeze({
  LOCAL_HOST: `${LOGIN_URL}https://test.levellog.app/login`,
});

export const SERVER_PATH = Object.freeze({
  FEEDBACKS: `${API_URL}/feedbacks`,
  LEVELLOGS: `${API_URL}/levellogs`,
});
