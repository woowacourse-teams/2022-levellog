import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const useInterviewQuestionSearch = () => {
  const [searchText, setSearchText] = useState('');
  const navigate = useNavigate();
  const location = useLocation();

  const handleClickRemoveSearchTextButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    setSearchText('');
  };

  const handleChangeSearchInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchText(e.target.value);
  };

  const handleSubmitInterviewQuestion = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (searchText.trim().length > 0) {
      navigate(`/search?keyword=${searchText}`);
    }
  };

  useEffect(() => {
    const params = new URL(window.location.href).searchParams;
    const keyword = params.get('keyword');
    if (keyword) {
      setSearchText(keyword);
    }
  }, [location.search]);

  return {
    searchText,
    handleClickRemoveSearchTextButton,
    handleChangeSearchInput,
    handleSubmitInterviewQuestion,
  };
};

export default useInterviewQuestionSearch;
