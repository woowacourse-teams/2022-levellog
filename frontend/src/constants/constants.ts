export const API_URL = 'https://dev.levellog.app/api';

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

export const SERVER_PATH = Object.freeze({
  FEEDBACKS: `${API_URL}/feedbacks`,
  LEVELLOGS: `${API_URL}/levellogs`,
});
