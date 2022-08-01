import styled from 'styled-components';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import Input from 'components/@commons/Input';

const InterviewTeamAdd = () => {
  const handleSubmitTeamAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault;
  };

  return (
    <S.Container onSubmit={handleSubmitTeamAddForm}>
      <ContentHeader title={'인터뷰 팀 생성하기'}>
        <Button type={'submit'}>만들기</Button>
      </ContentHeader>
      <S.FormContainer>
        <S.InputContainer>
          <S.Label>제목</S.Label>
          <S.Input />
        </S.InputContainer>
        <S.InputContainer>
          <S.Label>장소</S.Label>
          <S.Input />
        </S.InputContainer>
        <S.InputContainer>
          <S.Label>날짜</S.Label>
          <S.Input type={'date'} />
        </S.InputContainer>
        <S.InputContainer>
          <S.Label>시간</S.Label>
          <S.Input type={'time'} />
        </S.InputContainer>
        <S.InputContainer>
          <S.Label>참가자</S.Label>
          <S.Input />
        </S.InputContainer>
      </S.FormContainer>
    </S.Container>
  );
};

const S = {
  Container: styled.form``,

  FormContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1.5rem;
  `,

  InputContainer: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
  `,

  Input: styled(Input)`
    width: 40.625rem;
    height: 1.125rem;
  `,

  Label: styled.label`
    font-size: 1rem;
    margin-bottom: 1rem;
  `,
};

export default InterviewTeamAdd;
