import styled from 'styled-components';

import useInterviewQuestionEdit from 'hooks/interviewQuestion/useInterviewQuestionEdit';

import {
  InterviewQuestionDeleteRequestType,
  InterviewQuestionEditRequestType,
} from 'apis/InterviewQuestion';
import Button from 'components/@commons/Button';
import { InterviewQuestionInfoType } from 'types/interviewQuestion';

const InterviewQuestionContent = ({
  InterviewQuestionInfo,
  onClickDeleteInterviewQuestionButton,
  onSubmitEditInterviewQuestion,
}: InterviewQuestionContentProps) => {
  const {
    isEditInterviewQuestion,
    InterviewQuestionEditRef,
    handleClickEditInterviewQuestionButton,
    handleClickDeleteInterviewQuestionButton,
    handleSubmitEditCompleteInterviewQuestion,
  } = useInterviewQuestionEdit({
    InterviewQuestionInfo,
    onClickDeleteInterviewQuestionButton,
    onSubmitEditInterviewQuestion,
  });

  return (
    <S.Container>
      {isEditInterviewQuestion ? (
        <form onSubmit={handleSubmitEditCompleteInterviewQuestion}>
          <S.Input ref={InterviewQuestionEditRef} />
        </form>
      ) : (
        <p onClick={handleClickEditInterviewQuestionButton}>{InterviewQuestionInfo.content}</p>
      )}
      <S.DeleteButton onClick={handleClickDeleteInterviewQuestionButton}>
        <p>X</p>
      </S.DeleteButton>
    </S.Container>
  );
};

interface InterviewQuestionContentProps {
  InterviewQuestionInfo: InterviewQuestionInfoType;
  onClickDeleteInterviewQuestionButton: ({
    InterviewQuestionId,
  }: Pick<InterviewQuestionDeleteRequestType, 'InterviewQuestionId'>) => Promise<void>;
  onSubmitEditInterviewQuestion: ({
    InterviewQuestionId,
    InterviewQuestion,
  }: Pick<
    InterviewQuestionEditRequestType,
    'InterviewQuestionId' | 'InterviewQuestion'
  >) => Promise<void>;
}

const S = {
  Container: styled.li`
    display: grid;
    grid-template-columns: auto 1.875rem;
    align-items: center;
    column-gap: 0.625rem;
  `,

  Input: styled.input`
    width: 100%;
    height: 1.875rem;
    margin-right: 0.625rem;
    border: none;
    border-bottom: 0.0625rem solid ${(props) => props.theme.default.BLACK};
    font-size: 1rem;
  `,

  DeleteButton: styled(Button)`
    width: 1.875rem;
    height: 1.875rem;
    padding: 0;
    border: none;
    border-radius: 0.9375rem;
    background-color: ${(props) => props.theme.default.GRAY};
  `,
};

export default InterviewQuestionContent;
