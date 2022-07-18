export const API_URL = 'http://http://3.36.75.80:8080/api';

export const ROUTES_PATH = Object.freeze({
  HOME: '/',
  FEEDBACK: '/feedback',
  FEEDBACK_ADD: '/feedback/add',
  LOGIN: '/login',
  LEVELLOG: '/levellog',
  LEVELLOG_ADD: '/levellog/add',
  INTERVIEW_TEAMS: '/interview/teams',
  INTERVIEW_TEAMS_DETAIL: '/interview/teams/:teamId',
});

export const SERVER_PATH = Object.freeze({
  FEEDBACKS: `${API_URL}/feedbacks`,
  LEVELLOGS: `${API_URL}/levellogs`,
});
