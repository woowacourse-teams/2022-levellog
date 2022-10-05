import styled from 'styled-components';

import useInterviewQuestionSearch from 'hooks/questionSearch/useInterviewQuestionSearch';

import deleteIcon from 'assets/images/close.svg';
import searchIcon from 'assets/images/search.svg';

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
    <S.Container onSubmit={handleSubmitInterviewQuestion}>
      <S.Title>인터뷰 질문 검색</S.Title>
      <S.InputBox>
        <S.Button type={'submit'}>
          <Image src={searchIcon} sizes={'SMALL'} />
        </S.Button>
        <S.SearchInput
          placeholder={'검색어를 입력하세요'}
          value={searchText}
          onChange={handleChangeSearchInput}
        />
        {searchText && (
          <S.Button onClick={handleClickRemoveSearchTextButton}>
            <Image src={deleteIcon} sizes={'TINY'} />
          </S.Button>
        )}
      </S.InputBox>
    </S.Container>
  );
};

const S = {
  Container: styled.form`
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    padding: 3.125rem 0;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY}; ;
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
    border-radius: 16px;
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

  Button: styled.button`
    border: none;
    background-color: ${(props) => props.theme.new_default.WHITE};
  `,
};

export default InterviewQuestionSearchForm;
