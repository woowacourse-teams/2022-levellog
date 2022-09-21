import styled from 'styled-components';

import deleteIcon from 'assets/images/close.svg';
import searchIcon from 'assets/images/search.svg';

import Button from 'components/@commons/Button';
import Image from 'components/@commons/Image';
import Input from 'components/@commons/Input';

const InterviewQuestionSearchForm = ({
  searchText,
  handleClickRemoveSearchTextButton,
  handleChangeSearchInput,
  handleSubmitInterviewQuestion,
}: InterviewQuestionSearchFormProps) => {
  return (
    <S.Container onSubmit={handleSubmitInterviewQuestion}>
      <S.Title>인터뷰 질문 검색</S.Title>
      <S.InputBox>
        <Image src={searchIcon} sizes={'SMALL'} />
        <S.SearchInput
          placeholder={'검색어를 입력하세요'}
          value={searchText}
          onChange={handleChangeSearchInput}
        />
        {searchText && (
          <S.DeleteButton onClick={handleClickRemoveSearchTextButton}>
            <Image src={deleteIcon} sizes={'TINY'} />
          </S.DeleteButton>
        )}
      </S.InputBox>
    </S.Container>
  );
};

interface InterviewQuestionSearchFormProps {
  searchText: string;
  handleClickRemoveSearchTextButton: () => void;
  handleChangeSearchInput: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleSubmitInterviewQuestion: (e: React.FormEvent<HTMLFormElement>) => void;
}

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

  DeleteButton: styled.button`
    border: none;
    background-color: ${(props) => props.theme.new_default.WHITE};
  `,
};

export default InterviewQuestionSearchForm;
