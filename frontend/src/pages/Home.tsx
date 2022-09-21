import useInterviewQuestionSearch from 'hooks/useInterviewQuestionSearch';

import InterviewTeams from 'pages/teams/InterviewTeams';

import InterviewQuestionSearchForm from 'components/interviewQuestion/InterviewQuestionSearchForm';
import InterviewQuestionSearchResults from 'components/interviewQuestion/InterviewQuestionSearchResults';

const Home = () => {
  const {
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
  } = useInterviewQuestionSearch();

  return (
    <>
      <InterviewQuestionSearchForm
        searchText={searchText}
        handleChangeSearchInput={handleChangeSearchInput}
        handleSubmitInterviewQuestion={handleSubmitInterviewQuestion}
      />
      {window.location.pathname === '/' && <InterviewTeams />}
      {window.location.pathname !== '/' && (
        <InterviewQuestionSearchResults
          searchResults={searchResults}
          searchSortActive={searchSortActive}
          getSearchResults={getSearchResults}
          onClickLikeButton={onClickLikeButton}
          onClickCancelLikeButton={onClickCancelLikeButton}
          handleClickSearchSortLike={handleClickSearchSortLike}
          handleClickSearchSortNew={handleClickSearchSortNew}
          handleClickSearchSortOld={handleClickSearchSortOld}
        />
      )}
    </>
  );
};

export default Home;
