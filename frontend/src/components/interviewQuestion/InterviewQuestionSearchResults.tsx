import { useEffect } from 'react';

import styled from 'styled-components';

import EmptySearchResult from 'pages/status/EmptySearchResult';

import Button from 'components/@commons/Button';
import InterviewQuestionSearchResult from 'components/interviewQuestion/InterviewQuestionSearchResult';
import {
  InterviewQuestionApiType,
  InterviewQuestionSearchResultType,
} from 'types/interviewQuestion';

const InterviewQuestionSearchResults = ({
  searchResults,
  searchSortActive,
  getSearchResults,
  onClickLikeButton,
  onClickCancelLikeButton,
  handleClickSearchSortLike,
  handleClickSearchSortNew,
  handleClickSearchSortOld,
}: InterviewQuestionSearchResultsProps) => {
  useEffect(() => {
    getSearchResults();
  }, []);

  if (!searchResults || searchResults.length === 0) {
    return <EmptySearchResult />;
  }

  return (
    <S.Container>
      <S.ButtonBox>
        <S.RangeButton
          onClick={handleClickSearchSortLike}
          isActive={searchSortActive === '좋아요순'}
        >
          좋아요순
        </S.RangeButton>
        <S.DivisionLine></S.DivisionLine>
        <S.RangeButton onClick={handleClickSearchSortNew} isActive={searchSortActive === '최신순'}>
          최신순
        </S.RangeButton>
        <S.DivisionLine></S.DivisionLine>
        <S.RangeButton
          onClick={handleClickSearchSortOld}
          isActive={searchSortActive === '오래된순'}
        >
          오래된순
        </S.RangeButton>
      </S.ButtonBox>
      <S.Content>
        {searchResults.map((searchResult) => (
          <InterviewQuestionSearchResult
            key={searchResult.id}
            interviewQuestion={searchResult}
            onClickLikeButton={onClickLikeButton}
            onClickCancelLikeButton={onClickCancelLikeButton}
          />
        ))}
      </S.Content>
    </S.Container>
  );
};

interface InterviewQuestionSearchResultsProps {
  searchResults: InterviewQuestionSearchResultType[];
  searchSortActive: string;
  getSearchResults: () => void;
  onClickLikeButton: ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => void;
  onClickCancelLikeButton: ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => void;
  handleClickSearchSortLike: () => void;
  handleClickSearchSortNew: () => void;
  handleClickSearchSortOld: () => void;
}

const S = {
  Container: styled.div`
    width: 71rem;
    height: 43.125rem;
    padding: 0 1.125rem 1.125rem 1.125rem;
    margin: auto;
    margin-bottom: 1.875rem;
    border-radius: 2.8125rem;
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
};

export default InterviewQuestionSearchResults;
