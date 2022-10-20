import styled from 'styled-components';

import useSearchedInterviewQuestion from 'hooks/questionSearch/useSearchedInterviewQuestion';

import EmptySearchResult from 'pages/status/EmptySearchResult';

import { INTERVIEW_QUESTION_FILTER } from 'constants/constants';

import Button from 'components/@commons/Button';
import InterviewQuestionSearchResult from 'components/interviewQuestion/InterviewQuestionSearchResult';

const InterviewQuestionSearchResults = () => {
  const {
    searchResults,
    searchFilterActive,
    onClickLikeButton,
    onClickCancelLikeButton,
    handleClickFilterButton,
  } = useSearchedInterviewQuestion();

  if (searchResults?.results.length === 0) {
    return (
      <>
        <S.SearchResultAriaText role={'status'}>검색 결과가 없습니다.</S.SearchResultAriaText>
        <EmptySearchResult />
      </>
    );
  }

  return (
    <S.Container>
      <S.SearchResultAriaText
        role={'status'}
      >{`총 ${searchResults?.results.length}개의 검색 결과가 있습니다.`}</S.SearchResultAriaText>
      <S.ButtonBox onClick={handleClickFilterButton}>
        <S.RangeButton
          isActive={searchFilterActive === INTERVIEW_QUESTION_FILTER.LATEST}
          aria-label={'인터뷰 질문 검색 최신순 정렬'}
        >
          최신순
        </S.RangeButton>
        <S.DivisionLine></S.DivisionLine>
        <S.RangeButton
          isActive={searchFilterActive === INTERVIEW_QUESTION_FILTER.LIKES}
          aria-label={'인터뷰 질문 검색 좋아요순 정렬'}
        >
          좋아요순
        </S.RangeButton>
      </S.ButtonBox>
      <S.Content role={'list'}>
        {searchResults?.results.map((result) => (
          <InterviewQuestionSearchResult
            key={result.id}
            interviewQuestion={result}
            onClickLikeButton={onClickLikeButton}
            onClickCancelLikeButton={onClickCancelLikeButton}
          />
        ))}
      </S.Content>
    </S.Container>
  );
};

const S = {
  Container: styled.div`
    width: 71rem;
    height: 43.125rem;
    padding: 0 1.125rem 1.125rem 1.125rem;
    margin: auto;
    margin-top: 1.875rem;
    margin-bottom: 1.875rem;
    border-radius: 1.875rem;
    background-color: ${(props) => props.theme.new_default.WHITE};
    box-shadow: 0.0625rem 0.25rem 0.625rem ${(props) => props.theme.new_default.GRAY};
    @media (max-width: 1180px) {
      width: calc(100% - 2.5rem);
    }
    @media (max-width: 520px) {
      border-radius: 1rem;
    }
  `,

  ButtonBox: styled.div`
    display: flex;
    justify-content: flex-end;
    align-items: center;
    width: 100%;
    height: 3.625rem;
  `,

  RangeButton: styled(Button)`
    padding: 0;
    background-color: ${(props) => props.theme.new_default.WHITE};
    color: ${(props) =>
      props.isActive ? props.theme.new_default.BLUE : props.theme.new_default.BLACK};
  `,

  DivisionLine: styled.div`
    height: 1.25rem;
    margin: 0 0.625rem;
    border-left: 0.125rem solid ${(props) => props.theme.new_default.BLACK};
  `,

  Content: styled.ul`
    overflow: auto;
    width: 100%;
    height: 38.625rem;
  `,

  ResultBox: styled.li`
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    padding: 0.4375rem 0.4375rem 0.4375rem 0.75rem;
    margin-bottom: 1rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  `,

  ResultText: styled.p`
    font-size: 1.25rem;
  `,

  GoodButton: styled.button`
    border: none;
    background-color: ${(props) => props.theme.new_default.WHITE};
  `,

  SearchResultAriaText: styled.p`
    font-size: 0;
  `,
};

export default InterviewQuestionSearchResults;
