import { useState } from 'react';

import { useQuery, useMutation } from '@tanstack/react-query';

import useSnackbar from 'hooks//utils/useSnackbar';
import errorHandler from 'hooks/utils/errorHandler';

import { MESSAGE, INTERVIEW_QUESTION_FILTER, QUERY_KEY } from 'constants/constants';

import {
  requestLikeCancelInterviewQuestion,
  requestLikeInterviewQuestion,
  requestSearchedInterviewQuestion,
} from 'apis/interviewQuestionSearch';
import { InterviewQuestionSort } from 'types/interviewQuestion';

const useSearchedInterviewQuestion = () => {
  const { showSnackbar } = useSnackbar();
  const [searchFilterActive, setSearchFilterActive] = useState<InterviewQuestionSort>(
    INTERVIEW_QUESTION_FILTER.LATEST,
  );

  const userId = localStorage.getItem('userId');
  const accessToken = localStorage.getItem('accessToken');

  const params = new URL(window.location.href).searchParams;
  const keyword = params.get('keyword');

  const { data: searchResults, refetch: searchResultsRefetch } = useQuery(
    [QUERY_KEY.SEARCH_RESULTS, keyword, searchFilterActive],
    () => requestSearchedInterviewQuestion({ accessToken, keyword, sort: searchFilterActive }),
    {
      staleTime: 600,
    },
  );

  const { mutate: likeInterviewQuestion } = useMutation(
    ({ interviewQuestionId }: InterviewQuestionId) => {
      return requestLikeInterviewQuestion({ accessToken, interviewQuestionId });
    },
    {
      onSuccess: () => {
        searchResultsRefetch();
      },
      onError: (err) => {
        if (!userId) return showSnackbar({ message: MESSAGE.NEED_LOGIN_SERVICE });
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const { mutate: likeCancelInterviewQuestion } = useMutation(
    ({ interviewQuestionId }: InterviewQuestionId) => {
      return requestLikeCancelInterviewQuestion({ accessToken, interviewQuestionId });
    },
    {
      onSuccess: () => {
        searchResultsRefetch();
      },
      onError: (err) => {
        if (!userId) return showSnackbar({ message: MESSAGE.NEED_LOGIN_SERVICE });
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const onClickLikeButton = async ({ interviewQuestionId }: InterviewQuestionId) => {
    likeInterviewQuestion({ interviewQuestionId });
  };

  const onClickCancelLikeButton = async ({ interviewQuestionId }: InterviewQuestionId) => {
    likeCancelInterviewQuestion({ interviewQuestionId });
  };

  const handleClickFilterButton = async (e: React.MouseEvent<HTMLElement>) => {
    const eventTarget = e.target as HTMLElement;

    switch (eventTarget.innerText) {
      case '최신순':
        setSearchFilterActive(INTERVIEW_QUESTION_FILTER.LATEST);
        searchResultsRefetch();

        break;
      case '좋아요순':
        setSearchFilterActive(INTERVIEW_QUESTION_FILTER.LIKES);
        searchResultsRefetch();

        break;
    }
  };

  return {
    searchResults,
    searchFilterActive,
    onClickLikeButton,
    onClickCancelLikeButton,
    handleClickFilterButton,
  };
};

interface InterviewQuestionId {
  interviewQuestionId: string;
}

export default useSearchedInterviewQuestion;
