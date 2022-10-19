import styled from 'styled-components';

import useQuestionEdit from 'hooks/question/useQuestionEdit';

import { QuestionDeleteRequestType, QuestionEditRequestType } from 'apis/question';
import Button from 'components/@commons/Button';
import { QuestionInfoType } from 'types/question';

const QuestionContent = ({
  QuestionInfo,
  onClickDeleteQuestionButton,
  onSubmitEditQuestion,
}: QuestionContentProps) => {
  const {
    isEditQuestion,
    QuestionEditRef,
    handleClickEditQuestionButton,
    handleClickDeleteQuestionButton,
    handleSubmitEditCompleteQuestion,
  } = useQuestionEdit({
    QuestionInfo,
    onClickDeleteQuestionButton,
    onSubmitEditQuestion,
  });

  return (
    <S.Container>
      {isEditQuestion ? (
        <form onSubmit={handleSubmitEditCompleteQuestion}>
          <S.Input ref={QuestionEditRef} />
        </form>
      ) : (
        <p onClick={handleClickEditQuestionButton}>{QuestionInfo.content}</p>
      )}
      <S.DeleteButton onClick={handleClickDeleteQuestionButton}>
        <p>X</p>
      </S.DeleteButton>
    </S.Container>
  );
};

interface QuestionContentProps {
  QuestionInfo: QuestionInfoType;
  onClickDeleteQuestionButton: ({
    QuestionId,
  }: Pick<QuestionDeleteRequestType, 'QuestionId'>) => Promise<void>;
  onSubmitEditQuestion: ({
    QuestionId,
    Question,
  }: Pick<QuestionEditRequestType, 'QuestionId' | 'Question'>) => Promise<void>;
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

export default QuestionContent;
