import { useEffect, useRef, useState } from 'react';

import { QuestionDeleteRequestType, QuestionEditRequestType } from './../../apis/question';
import { QuestionInfoType } from 'types/question';

const useQuestionEdit = ({
  QuestionInfo,
  onClickDeleteQuestionButton,
  onSubmitEditQuestion,
}: useQuestionEditProps) => {
  const [isEditQuestion, setIsEditQuestion] = useState(false);
  const QuestionEditRef = useRef<HTMLInputElement>(null);

  const handleClickEditQuestionButton = () => {
    setIsEditQuestion((prev) => !prev);
  };

  const handleClickDeleteQuestionButton = () => {
    onClickDeleteQuestionButton({ QuestionId: QuestionInfo.id });
  };

  const handleSubmitEditCompleteQuestion = async (e: React.FormEvent<HTMLFormElement>) => {
    setIsEditQuestion((prev) => !prev);
    if (QuestionEditRef.current) {
      onSubmitEditQuestion({
        QuestionId: QuestionInfo.id,
        Question: QuestionEditRef.current.value,
      });
    }
  };

  useEffect(() => {
    if (isEditQuestion && QuestionEditRef.current) {
      QuestionEditRef.current.value = QuestionInfo.content;
      QuestionEditRef.current.focus();
    }
  }, [isEditQuestion]);

  return {
    isEditQuestion,
    QuestionEditRef,
    handleClickEditQuestionButton,
    handleClickDeleteQuestionButton,
    handleSubmitEditCompleteQuestion,
  };
};

interface useQuestionEditProps {
  QuestionInfo: QuestionInfoType;
  onClickDeleteQuestionButton: ({
    QuestionId,
  }: Pick<QuestionDeleteRequestType, 'QuestionId'>) => Promise<void>;
  onSubmitEditQuestion: ({
    QuestionId,
    Question,
  }: Pick<QuestionEditRequestType, 'QuestionId' | 'Question'>) => Promise<void>;
}

export default useQuestionEdit;
