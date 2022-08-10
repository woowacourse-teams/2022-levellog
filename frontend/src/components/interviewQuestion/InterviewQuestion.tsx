import styled from 'styled-components';

import Input from 'components/@commons/Input';
import InterviewQuestionContent from 'components/interviewQuestion/InterviewQuestionContent';
import { InterviewQuestionApiType, interviewQuestionType } from 'types/interviewQuestion';

const InterviewQuestion = ({
  interviewQuestionsInfo,
  interviewQuestionRef,
  onClickDeleteInterviewQuestionButton,
  onClickEditInterviewQuestionButton,
  handleSubmitInterviewQuestion,
}: InterviewQuestionProps) => {
  return (
    <S.Container>
      <S.Title>Question</S.Title>
      <S.Content>
        {interviewQuestionsInfo.length > 0 &&
          interviewQuestionsInfo.map((interviewQuestionInfo) => (
            <InterviewQuestionContent
              key={interviewQuestionInfo.id}
              interviewQuestionInfo={interviewQuestionInfo}
              onClickDeleteInterviewQuestionButton={onClickDeleteInterviewQuestionButton}
              onClickEditInterviewQuestionButton={onClickEditInterviewQuestionButton}
            />
          ))}
      </S.Content>
      <S.QuestionForm onSubmit={handleSubmitInterviewQuestion}>
        <Input width={'100%'} height={'1.125rem'} inputRef={interviewQuestionRef} />
        <S.InputButton type={'submit'}>추가하기</S.InputButton>
      </S.QuestionForm>
    </S.Container>
  );
};

interface InterviewQuestionProps {
  interviewQuestionsInfo: interviewQuestionType[];
  interviewQuestionRef: React.Ref<HTMLInputElement>;
  onClickDeleteInterviewQuestionButton: ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => Promise<void>;
  onClickEditInterviewQuestionButton: ({
    interviewQuestionId,
    interviewQuestion,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId' | 'interviewQuestion'>) => Promise<void>;
  handleSubmitInterviewQuestion: (e: React.FormEvent<HTMLFormElement>) => void;
}

const S = {
  Container: styled.div`
    width: 100%;
    @media (max-width: 520px) {
      max-width: 22.875rem;
    }
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
    background-color: ${(props) => props.theme.default.WHITE};
  `,

  QuestionForm: styled.form`
    display: flex;
    flex-direction: row;
    align-items: center;
    height: '3.125rem';
    padding-right: 0.5rem;
    background-color: ${(props) => props.theme.default.WHITE};
  `,

  InputButton: styled.button`
    width: 6.625rem;
    height: 2.5rem;
    border: none;
    border-radius: 0.375rem;
    background-color: ${(props) => props.theme.default.GRAY};
  `,
};

export default InterviewQuestion;
