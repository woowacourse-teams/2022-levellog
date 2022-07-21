export const API_URL = 'https://levellog.app/api';

export const ROUTES_PATH = Object.freeze({
  HOME: '/',
  FEEDBACK: '/feedback',
  FEEDBACK_ADD: '/feedback/add',
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
