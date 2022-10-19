import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useQuery, useMutation } from '@tanstack/react-query';

import useSnackbar from 'hooks//utils/useSnackbar';

import { MESSAGE, ROUTES_PATH, INTERVIEW_QUESTION_FILTER } from 'constants/constants';

import {
  requestLikeCancelQuestion,
  requestLikeQuestion,
  requestSearchedQuestion,
} from 'apis/questionSearch';
import { QuestionSort } from 'types/question';

const useSearchedQuestion = () => {
  const { showSnackbar } = useSnackbar();

  const navigate = useNavigate();

  const [searchFilterActive, setSearchFilterActive] = useState<QuestionSort>(
    INTERVIEW_QUESTION_FILTER.LATEST,
  );

  const userId = localStorage.getItem('userId');
  const accessToken = localStorage.getItem('accessToken');

  const params = new URL(window.location.href).searchParams;
  const keyword = params.get('keyword');

  const { data: searchResults, refetch: searchResultsRefetch } = useQuery(
    ['searchResults', keyword, searchFilterActive],
    () => requestSearchedQuestion({ accessToken, keyword, sort: searchFilterActive }),
  );

  const { mutate: likeQuestion } = useMutation(
    ({ QuestionId }: QuestionId) => {
      return requestLikeQuestion({ accessToken, QuestionId });
    },
    {
      onSuccess: () => {
        searchResultsRefetch();
      },
      onError: (err: unknown) => {
        if (!userId) return showSnackbar({ message: MESSAGE.NEED_LOGIN_SERVICE });

        showSnackbar({ message: 'likeQuestion.error.message' });
        navigate(ROUTES_PATH.ERROR);

        return;
      },
    },
  );

  const { mutate: likeCancelQuestion } = useMutation(
    ({ QuestionId }: QuestionId) => {
      return requestLikeCancelQuestion({ accessToken, QuestionId });
    },
    {
      onSuccess: () => {
        searchResultsRefetch();
      },
      onError: (err: unknown) => {
        if (!userId) return showSnackbar({ message: MESSAGE.NEED_LOGIN_SERVICE });

        showSnackbar({ message: 'cancelLikeQuestion.error.message' });
        navigate(ROUTES_PATH.ERROR);

        return;
      },
    },
  );

  const onClickLikeButton = async ({ QuestionId }: QuestionId) => {
    likeQuestion({ QuestionId });
  };

  const onClickCancelLikeButton = async ({ QuestionId }: QuestionId) => {
    likeCancelQuestion({ QuestionId });
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

interface QuestionId {
  QuestionId: string;
}

export default useSearchedQuestion;
