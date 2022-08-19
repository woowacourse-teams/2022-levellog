import { useParams } from 'react-router-dom';

import { PATH_TYPE, ROUTES_PATH } from 'constants/constants';

import { UriPathType } from 'types/uri';

const useRouteUri = () => {
  const { teamId, levellogId, feedbackId, preQuestionId, authorId } = useParams();

  const feedbackUri = ({ pathType }: UriPathType) => {
    if (pathType === PATH_TYPE.ADD) {
      return `/teams/${teamId}/levellogs/${levellogId}/feedbacks/add`;
    }
    if (pathType === PATH_TYPE.EDIT) {
      return `/teams/${teamId}/levellogs/${levellogId}/feedbacks/${feedbackId}/author/${authorId}/edit`;
    }
    if (pathType === PATH_TYPE.GETS) {
      return `/teams/${teamId}/levellogs/${levellogId}/feedbacks`;
    }
    if (pathType === PATH_TYPE.GET) {
      return `/teams/${teamId}/levellogs/${levellogId}/feedbacks/${feedbackId}`;
    }

    return ROUTES_PATH.ERROR;
  };

  const levellogUri = ({ pathType }: UriPathType) => {
    if (pathType === PATH_TYPE.ADD) {
      return `/teams/${teamId}/levellogs/add`;
    }
    if (pathType === PATH_TYPE.EDIT) {
      return `/teams/${teamId}/levellogs/${levellogId}/author/${authorId}/edit`;
    }

    return ROUTES_PATH.ERROR;
  };

  const teamUri = ({ pathType }: UriPathType) => {
    if (pathType === PATH_TYPE.ADD) {
      return `/teams/add`;
    }
    if (pathType === PATH_TYPE.EDIT) {
      return `/teams/${teamId}/edit`;
    }
    if (pathType === PATH_TYPE.GETS) {
      return `/teams`;
    }
    if (pathType === PATH_TYPE.GET) {
      return `/teams/${teamId}`;
    }

    return ROUTES_PATH.ERROR;
  };

  const preQuestionUri = ({ pathType }: UriPathType) => {
    if (pathType === PATH_TYPE.ADD) {
      return `teams/${teamId}/levellogs/${levellogId}/preQuestions/add`;
    }
    if (pathType === PATH_TYPE.EDIT) {
      return `teams/${teamId}/levellogs/${levellogId}/preQuestions/${preQuestionId}/author/${authorId}/edit`;
    }

    return ROUTES_PATH.ERROR;
  };

  const interviewQuestionUri = ({ pathType }: UriPathType) => {
    if (pathType === PATH_TYPE.GETS) {
      return `teams/${teamId}/levellogs/${levellogId}/interviewQuestions`;
    }

    return ROUTES_PATH.ERROR;
  };

  return { feedbackUri, levellogUri, teamUri, preQuestionUri, interviewQuestionUri };
};

export default useRouteUri;
