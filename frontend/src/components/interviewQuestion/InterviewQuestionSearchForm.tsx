import styled from 'styled-components';

import useInterviewQuestionSearch from 'hooks/questionSearch/useInterviewQuestionSearch';

import deleteIcon from 'assets/images/close.svg';
import searchIcon from 'assets/images/search.svg';

import Button from 'components/@commons/Button';
import Image from 'components/@commons/Image';
import Input from 'components/@commons/Input';

const InterviewQuestionSearchForm = () => {
  const {
    searchText,
    handleClickRemoveSearchTextButton,
    handleChangeSearchInput,
    handleSubmitInterviewQuestion,
  } = useInterviewQuestionSearch();

  return (
    <S.Container>
      <S.Title>인터뷰 질문 검색</S.Title>
      <S.Content>
        <S.SearchForm onSubmit={handleSubmitInterviewQuestion}>
          <S.InputBox>
            <Image
              src={searchIcon}
              alt={'인터뷰 질문 검색하기'}
              sizes={'SMALL'}
              aria-hidden={true}
            />
            <S.SearchInput
              placeholder={'검색어를 입력하세요'}
              value={searchText}
              onChange={handleChangeSearchInput}
            />
          </S.InputBox>
        </S.SearchForm>
        {searchText && (
          <S.Button onClick={handleClickRemoveSearchTextButton}>
            <Image src={deleteIcon} alt={'인터뷰 질문 검색 텍스트 삭제'} sizes={'TINY'} />
          </S.Button>
        )}
      </S.Content>
    </S.Container>
  );
};

const S = {
  Container: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    padding: 3.125rem 0;
    border-bottom: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  `,

  Content: styled.div`
    position: relative;
    display: flex;
    width: 47.5rem;
    @media (max-width: 850px) {
      width: calc(100% - 2.5rem);
      min-width: 20rem;
    }
  `,

  SearchForm: styled.form`
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
  `,

  Title: styled.h1`
    width: 47.5rem;
    font-size: 24px;
    @media (max-width: 850px) {
      width: calc(100% - 2.5rem);
      font-size: 1rem;
    }
  `,

  InputBox: styled.div`
    display: flex;
    align-items: center;
    width: 47.5rem;
    margin-top: 1rem;
    padding: 0 0.625rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    border-radius: 1rem;
    @media (max-width: 850px) {
      width: calc(100% - 2.5rem);
      min-width: 20rem;
    }
  `,

  SearchInput: styled(Input)`
    width: 47.5rem;
    height: 2.125rem;
    font-size: 1.5rem;
    ::placeholder {
      color: ${(props) => props.theme.new_default.GRAY};
    }
    @media (max-width: 850px) {
      width: calc(100% - 2.5rem);
      ::placeholder {
        font-size: 1.25rem;
      }
    }
  `,

  Button: styled(Button)`
    position: absolute;
    top: 2.5rem;
    right: 0.625rem;
    padding: 0;
    border: none;
    background-color: ${(props) => props.theme.new_default.WHITE};
    @media (max-width: 850px) {
      right: 1.875rem;
    }
    @media (max-width: 400px) {
      right: 1.25rem;
    }
  `,
};

export default InterviewQuestionSearchForm;
