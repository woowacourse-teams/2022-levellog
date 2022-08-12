import styled from 'styled-components';

import useInterviewQuestionEdit from 'hooks/useInterviewQuestionEdit';

import { InterviewQuestionApiType, InterviewQuestionType } from 'types/interviewQuestion';
import Button from 'components/@commons/Button';

const InterviewQuestionContent = ({
  interviewQuestionInfo,
  onClickDeleteInterviewQuestionButton,
  onSubmitEditInterviewQuestion,
}: InterviewQuestionContentProps) => {
  const {
    isEditInterviewQuestion,
    interviewQuestionEditRef,
    handleClickEditInterviewQuestionButton,
    handleClickDeleteInterviewQuestionButton,
    handleSubmitEditCompleteInterviewQuestion,
  } = useInterviewQuestionEdit({
    interviewQuestionInfo,
    onClickDeleteInterviewQuestionButton,
    onSubmitEditInterviewQuestion,
  });

  return (
    <S.Container>
      {isEditInterviewQuestion ? (
        <form onSubmit={handleSubmitEditCompleteInterviewQuestion}>
          <S.Input ref={interviewQuestionEditRef} />
        </form>
      ) : (
        <p onClick={handleClickEditInterviewQuestionButton}>
          {interviewQuestionInfo.interviewQuestion}
        </p>
      )}
      <S.DeleteButton onClick={handleClickDeleteInterviewQuestionButton}>
        <p>X</p>
      </S.DeleteButton>
      {/* <InterviewQuestionButton handleClick={handleClickDeleteInterviewQuestionButton}> */}
      {/* </InterviewQuestionButton> */}
    </S.Container>
  );
};

interface InterviewQuestionContentProps {
  interviewQuestionInfo: InterviewQuestionType;
  onClickDeleteInterviewQuestionButton: ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => Promise<void>;
  onSubmitEditInterviewQuestion: ({
    interviewQuestionId,
    interviewQuestion,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId' | 'interviewQuestion'>) => Promise<void>;
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
    margin-right: 10px;
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
