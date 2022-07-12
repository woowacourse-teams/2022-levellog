export const API_URL = 'http://52.79.235.135:8080/api';

export const ROUTES_PATH = Object.freeze({
  HOME: '/',

  FEEDBACK: '/feedback',
  FEEDBACK_ADD: '/feedback/add',

  LEVELLOG: '/levellog',
  LEVELLOG_ADD: '/levellog/add',
});

export const SERVER_PATH = Object.freeze({
  FEEDBACKS: `${API_URL}/feedbacks`,
  LEVELLOGS: `${API_URL}/levellogs`,
});

export const BASE_INPUTSIZE = Object.freeze({
  WIDTH: '300px',
  HEIGHT: '40px',
});
