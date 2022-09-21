import styled from 'styled-components';

import deleteIcon from 'assets/images/close.svg';
import searchIcon from 'assets/images/search.svg';

import Image from 'components/@commons/Image';
import Input from 'components/@commons/Input';

const InterviewQuestionSearchForm = ({
  searchText,
  handleChangeSearchInput,
  handleSubmitInterviewQuestion,
}: InterviewQuestionSearchFormProps) => {
  return (
    <S.Container onSubmit={handleSubmitInterviewQuestion}>
      <S.Title>인터뷰 질문 검색</S.Title>
      <S.InputBox>
        <S.IconBox>
          <Image src={searchIcon} sizes={'MEDIUM'} />
        </S.IconBox>
        <S.SearchInput
          placeholder={'검색어를 입력하세요'}
          value={searchText}
          onChange={handleChangeSearchInput}
        />
        <Image src={deleteIcon} sizes={'SMALL'} />
      </S.InputBox>
    </S.Container>
  );
};

interface InterviewQuestionSearchFormProps {
  searchText: string;
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
  `,

  Title: styled.h1`
    width: 47.5rem;
    font-size: 2rem;
    @media (max-width: 850px) {
      width: calc(100% - 2.5rem);
      font-size: 1.5rem;
    }
  `,

  InputBox: styled.div`
    display: flex;
    align-items: center;
    width: 47.5rem;
    margin-top: 1rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    @media (max-width: 850px) {
      width: calc(100% - 2.5rem);
      min-width: 20rem;
    }
  `,

  IconBox: styled.div`
    margin: 0 0.6875rem;
  `,

  SearchInput: styled(Input)`
    width: 47.5rem;
    height: 2.125rem;
    border-radius: 0.3125rem;
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
};

export default InterviewQuestionSearchForm;
