import { Suspense } from 'react';

import styled from 'styled-components';

import useInterviewQuestion from 'hooks/interviewQuestion/useInterviewQuestion';

import Error from 'pages/status/Error';
import Loading from 'pages/status/Loading';

import Button from 'components/@commons/Button';
import Input from 'components/@commons/Input';
import InterviewQuestionContent from 'components/interviewQuestion/InterviewQuestionContent';

const InterviewQuestion = () => {
  const {
    interviewQuestionError,
    interviewQuestionInfos,
    interviewQuestionRef,
    interviewQuestionContentRef,
    onClickDeleteInterviewQuestionButton,
    onSubmitEditInterviewQuestion,
    handleSubmitInterviewQuestion,
  } = useInterviewQuestion();

  if (interviewQuestionError) {
    return <Error />;
  }

  return (
    <Suspense fallback={<Loading />}>
      <S.Container>
        <S.Content ref={interviewQuestionContentRef}>
          {interviewQuestionInfos!.interviewQuestions.length > 0 &&
            interviewQuestionInfos!.interviewQuestions.map((interviewQuestionInfo) => (
              <InterviewQuestionContent
                key={interviewQuestionInfo.id}
                interviewQuestionInfo={interviewQuestionInfo}
                onClickDeleteInterviewQuestionButton={onClickDeleteInterviewQuestionButton}
                onSubmitEditInterviewQuestion={onSubmitEditInterviewQuestion}
              />
            ))}
        </S.Content>
        <S.QuestionForm onSubmit={handleSubmitInterviewQuestion}>
          <Input width={'100%'} height={'1.125rem'} inputRef={interviewQuestionRef} />
          <S.InputButton type={'submit'}>추가하기</S.InputButton>
        </S.QuestionForm>
      </S.Container>
    </Suspense>
  );
};

const S = {
  Container: styled.div`
    width: 100%;
  `,

  Title: styled.h2`
    margin-bottom: 1.875rem;
    font-size: 1.875rem;
  `,

  Content: styled.ul`
    overflow: auto;
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    width: 100%;
    height: 19.5rem;
    padding: 1rem;
    margin-bottom: 0.625rem;
    box-shadow: 0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.new_default.GRAY};
    border-radius: 0.5rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    background-color: ${(props) => props.theme.default.WHITE};
  `,

  QuestionForm: styled.form`
    display: flex;
    flex-direction: row;
    align-items: center;
    height: '3.125rem';
    padding-right: 0.5rem;
    border-radius: 0.5rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    background-color: ${(props) => props.theme.default.WHITE};
  `,

  InputButton: styled(Button)`
    width: 6.625rem;
    height: 2.5rem;
    padding: 0;
    border: none;
    border-radius: 0.375rem;
  `,
};

export default InterviewQuestion;
