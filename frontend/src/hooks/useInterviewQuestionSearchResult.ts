import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import useSnackbar from 'hooks/useSnackbar';

import { MESSAGE } from 'constants/constants';

import {
  requestInterviewQuestionSearch,
  requestInterviewQuestionSearchCancelGood,
  requestInterviewQuestionSearchGood,
  requestInterviewQuestionSearchSort,
} from 'apis/interviewQuestionSearch';
import {
  InterviewQuestionApiType,
  InterviewQuestionSearchApiType,
  InterviewQuestionSearchResultType,
} from 'types/interviewQuestion';
import { tryCatch } from 'utils/util';

const useInterviewQuestionSearchResult = () => {
  const { showSnackbar } = useSnackbar();
  const [searchResults, setSearchResults] = useState<InterviewQuestionSearchResultType[]>([]);
  const [searchSortActive, setSearchSortActive] = useState('최신순');
  const [searchText, setSearchText] = useState('');
  const location = useLocation();

  const userId = localStorage.getItem('userId');
  const accessToken = localStorage.getItem('accessToken');

  const getSearchResults = async () => {
    const params = new URL(window.location.href).searchParams;
    const keyword = params.get('keyword');
    const res = await tryCatch<InterviewQuestionSearchApiType>({
      func: requestInterviewQuestionSearch,
      args: { accessToken, keyword },
      snackbar: showSnackbar,
    });
    if (typeof res !== 'boolean' && keyword) {
      setSearchText(keyword);
      setSearchResults(res.results);
    }
  };

  const onClickLikeButton = async ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => {
    if (!userId) {
      return showSnackbar({ message: MESSAGE.NEED_LOGIN_SERVICE });
    }
    setSearchResults(
      searchResults.map((result) => {
        return {
          id: result.id,
          content: result.content,
          like: Number(result.id) === Number(interviewQuestionId) ? !result.like : result.like,
          likeCount:
            Number(result.id) === Number(interviewQuestionId)
              ? (result.likeCount += 1)
              : result.likeCount,
        };
      }),
    );
    await tryCatch<void>({
      func: requestInterviewQuestionSearchGood,
      args: { interviewQuestionId, accessToken },
      snackbar: showSnackbar,
    });
  };

  const onClickCancelLikeButton = async ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => {
    if (!userId) {
      return showSnackbar({ message: MESSAGE.NEED_LOGIN_SERVICE });
    }
    setSearchResults(
      searchResults.map((result) => {
        return {
          id: result.id,
          content: result.content,
          like: Number(result.id) === Number(interviewQuestionId) ? !result.like : result.like,
          likeCount:
            Number(result.id) === Number(interviewQuestionId)
              ? (result.likeCount -= 1)
              : result.likeCount,
        };
      }),
    );
    await tryCatch<void>({
      func: requestInterviewQuestionSearchCancelGood,
      args: { interviewQuestionId, accessToken },
      snackbar: showSnackbar,
    });
  };

  const handleClickSearchSortNew = async () => {
    const res = await tryCatch<InterviewQuestionSearchApiType>({
      func: requestInterviewQuestionSearchSort,
      args: { accessToken, keyword: searchText, sort: 'latest' },
      snackbar: showSnackbar,
    });
    if (typeof res !== 'boolean') {
      setSearchResults(res.results);
    }
    setSearchSortActive('최신순');
  };

  const handleClickSearchSortLike = async () => {
    const res = await tryCatch<InterviewQuestionSearchApiType>({
      func: requestInterviewQuestionSearchSort,
      args: { accessToken, keyword: searchText, sort: 'likes' },
      snackbar: showSnackbar,
    });
    if (typeof res !== 'boolean') {
      setSearchResults(res.results);
    }
    setSearchSortActive('좋아요순');
  };

  useEffect(() => {
    getSearchResults();
  }, [location.search]);

  return {
    searchResults,
    searchSortActive,
    getSearchResults,
    onClickLikeButton,
    onClickCancelLikeButton,
    handleClickSearchSortLike,
    handleClickSearchSortNew,
    // handleClickSearchSortOld,
  };
};

export default useInterviewQuestionSearchResult;
