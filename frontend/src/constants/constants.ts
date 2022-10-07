export const GITHUB_LOGIN_URL = `https://github.com/login/oauth/authorize?client_id=${process.env.CLIENT_ID}`;

export const ROUTES_PATH = Object.freeze({
  HOME: '/',
  LOGIN: '/login',
  NOT_FOUND: '*',
  ERROR: '/error',
  COPYRIGHT: '/copyright',

  FEEDBACKS: '/teams/:teamId/levellogs/:levellogId/feedbacks',
  FEEDBACK_ADD: `/teams/:teamId/levellogs/:levellogId/feedback/add`,
  FEEDBACK_EDIT: `/teams/:teamId/levellogs/:levellogId/feedback/:feedbackId/author/:authorId/edit`,

  LEVELLOG_ADD: '/teams/:teamId/levellogs/add',
  LEVELLOG_EDIT: '/teams/:teamId/levellogs/:levellogId/author/:authorId/edit',

  INTERVIEW_TEAMS_DETAIL: `/teams/:teamId`,
  INTERVIEW_TEAMS_ADD: `/teams/add`,
  INTERVIEW_TEAMS_EDIT: `/teams/:teamId/edit`,

  PREQUESTION_ADD: '/teams/:teamId/levellogs/:levellogId/pre-questions/add',
  PREQUESTION_EDIT:
    '/teams/:teamId/levellogs/:levellogId/pre-questions/:preQuestionId/author/:authorId/edit',

  INTERVIEW_QUESTION: '/teams/:teamId/levellogs/:levellogId/interview-questions',
  INTERVIEW_QUESTION_SEARCH: '/search/*',
});

export const MESSAGE = Object.freeze({
  WRONG_ACCESS: '잘못된 접근입니다.',
  NEED_RE_LOGIN: '재로그인이 필요합니다.',
  NEED_LOGIN: '로그인이 필요한 페이지입니다.',
  NEED_LOGIN_SERVICE: '로그인이 필요한 서비스입니다.',
  NEED_IN_TEAM: '해당 팀에 소속된 유저만 접근할 수 있습니다!',
  NEED_HOST: '호스트만 접근할 수 있습니다!',
  NEED_NOT_ME: '본인이 본인에 대한 질문,피드백을 작성할 수 없습니다.',
  NEED_AUTHOR: '작성자만 글을 수정할 수 있습니다.',
  WRITE_MORE: '3글자 이상 작성해주세요.',
  CAN_EDIT: '수정하고 싶은 질문을 클릭해서 수정할 수 있어요!',
  WRONG_TOKEN: '유효하지 않은 토큰입니다.',
  ESCAPE_NOW_PAGE: '현재 페이지에서 나가시겠습니까?',
  CAN_NOT_EXPECT_ERROR: '예상치 못한 에러가 발생하였습니다.',

  TEAM_CREATE: '인터뷰팀 생성을 완료했습니다!',
  TEAM_DELETE_CONFIRM: '정말로 팀을 삭제하시겠습니까?',
  TEAM_DELETE: '팀 삭제가 완료되었습니다',
  TEAM_EDIT: '팀 수정이 완료되었습니다.',

  FEEDBACK_CREATE: '피드백 작성이 완료되었습니다.',
  FEEDBACK_EDIT: '피드백 수정이 완료되었습니다.',
  FEEDBACK_SAVE: '피드백 저장이 완료되었습니다.',

  LEVELLOG_ADD: '레벨로그 작성이 완료되었습니다.',
  LEVELLOG_EDIT: '레벨로그 수정이 완료되었습니다.',

  PREQUESTION_ADD: '사전질문 등록이 완료되었습니다.',
  PREQUESTION_DELETE: '사전질문 삭제가 완료되었습니다.',
  PREQUESTION_EDIT: '사전질문 수정이 완료되었습니다.',

  INTERVIEW_CLOSE_CONFIRM: '정말로 인터뷰를 종료하시겠습니까?',
  INTERVIEW_STATUS_NOT_READY: '인터뷰가 시작 전이 아닙니다!',
  INTERVIEW_STATUS_NOT_IN_PROGRESS: '인터뷰가 진행 중이 아닙니다!',
  INTERVIEW_CLOSE: '인터뷰가 종료되었습니다.',

  INTERVIEW_TITLE_VALIDATE_FAIL: '3글자 이상 14자 이하로 작성해주세요.',
  INTERVIEW_LOCATION_VALIDATE_FAIL: '3글자 이상 12자 이하로 작성해주세요.',
  INTERVIEW_DATE_VALIDATE_FAIL: '올바른 인터뷰 날짜를 입력해주세요.',
  INTERVIEW_TIME_VALIDATE_FAIL: '올바른 인터뷰 시간를 입력해주세요.',
  INTERVIEW_INTERVIEWEE_VALIDATE_FAIL: '인터뷰이는 1명 이상 3명 이하로 작성해주세요.',
  INTERVIEW_HOLE_VALUE_VALIDATE:
    '필수값 모두 입력해주세요. (필수값: 제목, 장소, 날짜, 시간, 인터뷰어 수, 참여자)',
});

export const TEAM_STATUS = Object.freeze({
  READY: 'READY',
  IN_PROGRESS: 'IN_PROGRESS',
  CLOSED: 'CLOSED',
});

export const TEAMS_CONDITION = Object.freeze({
  OPEN: 'open',
  CLOSE: 'close',
  MY: 'my',
});

export const REQUIRE_AUTH = Object.freeze({
  LOGIN: 'login',
  IN_TEAM: 'inTeam',
  HOST: 'host',
  ME: 'me',
  NOT_ME: 'notMe',
  AUTHOR: 'author',
});

export const GITHUB_AVATAR_SIZE_LIST = {
  HUGE: 120,
  LARGE: 60,
  MEDIUM: 44,
  SMALL: 30,
};

export const NOT_YET_HTTP_STATUS = 0;

export const INTERVIEW_QUESTION_FILTER = Object.freeze({
  LATEST: 'latest',
  OLDEST: 'oldest',
  LIKES: 'likes',
});
