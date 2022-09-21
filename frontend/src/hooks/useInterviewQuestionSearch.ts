import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

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
  InterviewQuestionSearchResultType,
  InterviewQuestionSearchType,
} from 'types/interviewQuestion';
import { debounce, tryCatch } from 'utils/util';

const useInterviewQuestionSearch = () => {
  const { showSnackbar } = useSnackbar();
  const [searchResults, setSearchResults] = useState<InterviewQuestionSearchResultType[]>([]);
  const [searchText, setSearchText] = useState('');
  const [searchSortActive, setSearchSortActive] = useState('좋아요순');
  const navigate = useNavigate();

  const userId = localStorage.getItem('userId');
  const accessToken = localStorage.getItem('accessToken');

  const getSearchResults = async () => {
    const params = new URL(window.location.href).searchParams;
    const keyword = params.get('keyword');

    if (keyword === searchText) return;

    const res = await tryCatch({
      func: requestInterviewQuestionSearch,
      args: { accessToken, keyword },
      snackbar: showSnackbar,
    });
    if (res && keyword) {
      setSearchResults(res.results);
      setSearchText(keyword);
      navigate(`/search?keyword=${keyword}`);
    }
  };

  const onClickLikeButton = async ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => {
    if (!userId) {
      return showSnackbar({ message: MESSAGE.NEED_LOGIN_SERVICE });
    }
    await tryCatch({
      func: requestInterviewQuestionSearchGood,
      args: { interviewQuestionId, accessToken },
      snackbar: showSnackbar,
    });
    const res = await tryCatch({
      func: requestInterviewQuestionSearch,
      args: { accessToken, keyword: searchText },
      snackbar: showSnackbar,
    });
    if (res) {
      setSearchResults(res.results);
    }
  };

  const onClickCancelLikeButton = async ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => {
    if (!userId) {
      return showSnackbar({ message: MESSAGE.NEED_LOGIN_SERVICE });
    }
    await tryCatch({
      func: requestInterviewQuestionSearchCancelGood,
      args: { interviewQuestionId, accessToken },
      snackbar: showSnackbar,
    });
    const res = await tryCatch({
      func: requestInterviewQuestionSearch,
      args: { accessToken, keyword: searchText },
      snackbar: showSnackbar,
    });
    if (res) {
      setSearchResults(res.results);
    }
  };

  const handleClickSearchSortLike = async () => {
    const res = await tryCatch({
      func: requestInterviewQuestionSearchSort,
      args: { accessToken, keyword: searchText, sort: 'likes' },
      snackbar: showSnackbar,
    });
    if (res) {
      setSearchResults(res.results);
    }
    setSearchSortActive('좋아요순');
  };

  const handleClickSearchSortNew = async () => {
    const res = await tryCatch({
      func: requestInterviewQuestionSearchSort,
      args: { accessToken, keyword: searchText, sort: 'latest' },
      snackbar: showSnackbar,
    });
    if (res) {
      setSearchResults(res.results);
    }
    setSearchSortActive('최신순');
  };

  const handleClickSearchSortOld = async () => {
    const res = await tryCatch({
      func: requestInterviewQuestionSearchSort,
      args: { accessToken, keyword: searchText, sort: 'oldest' },
      snackbar: showSnackbar,
    });
    if (res) {
      setSearchResults(res.results);
    }
    setSearchSortActive('오래된순');
  };

  const handleChangeSearchInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchText(e.target.value);
  };

  const handleSubmitInterviewQuestion = async (e: React.FormEvent<HTMLFormElement>) => {
    if (searchText.trim().length > 0) {
      e.preventDefault();
      const res = await tryCatch({
        func: requestInterviewQuestionSearch,
        args: { accessToken, keyword: searchText },
        snackbar: showSnackbar,
      });
      if (res) {
        setSearchResults(res.results);
        navigate(`/search?keyword=${searchText}`);
      }
    }
  };

  return {
    searchText,
    searchResults,
    searchSortActive,
    getSearchResults,
    onClickLikeButton,
    onClickCancelLikeButton,
    handleClickSearchSortLike,
    handleClickSearchSortNew,
    handleClickSearchSortOld,
    handleChangeSearchInput,
    handleSubmitInterviewQuestion,
  };
};

export default useInterviewQuestionSearch;
