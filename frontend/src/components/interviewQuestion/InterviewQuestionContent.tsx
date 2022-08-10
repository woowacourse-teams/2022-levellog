import styled from 'styled-components';

import useInterviewQuestionEdit from 'hooks/useInterviewQuestionEdit';

import InterviewQuestionButton from 'components/interviewQuestion/interviewQuestionButton';
import { InterviewQuestionApiType, interviewQuestionType } from 'types/interviewQuestion';

const InterviewQuestionContent = ({
  interviewQuestionInfo,
  onClickDeleteInterviewQuestionButton,
  onClickEditInterviewQuestionButton,
}: InterviewQuestionContentProps) => {
  const {
    isEditInterviewQuestion,
    interviewQuestionEditRef,
    handleToggleEditInterviewQuestion,
    handleClickDeleteInterviewQuestionButton,
    handleClickEditCompleteInterviewQuestionButton,
  } = useInterviewQuestionEdit({
    interviewQuestionInfo,
    onClickDeleteInterviewQuestionButton,
    onClickEditInterviewQuestionButton,
  });

  return (
    <S.Container>
      {isEditInterviewQuestion ? (
        <>
          <S.Input ref={interviewQuestionEditRef} />
          <InterviewQuestionButton handleClick={handleClickEditCompleteInterviewQuestionButton}>
            <p>수정완료</p>
          </InterviewQuestionButton>
        </>
      ) : (
        <>
          <p onClick={handleToggleEditInterviewQuestion}>
            {interviewQuestionInfo.interviewQuestion}
          </p>
          <InterviewQuestionButton handleClick={handleToggleEditInterviewQuestion}>
            <p>수정</p>
          </InterviewQuestionButton>
        </>
      )}
      <InterviewQuestionButton handleClick={handleClickDeleteInterviewQuestionButton}>
        <p>삭제</p>
      </InterviewQuestionButton>
    </S.Container>
  );
};

interface InterviewQuestionContentProps {
  interviewQuestionInfo: interviewQuestionType;
  onClickDeleteInterviewQuestionButton: ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => Promise<void>;
  onClickEditInterviewQuestionButton: ({
    interviewQuestionId,
    interviewQuestion,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId' | 'interviewQuestion'>) => Promise<void>;
}

const S = {
  Container: styled.li`
    display: grid;
    grid-template-columns: auto 3.75rem 3.75rem;
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
};

export default InterviewQuestionContent;
