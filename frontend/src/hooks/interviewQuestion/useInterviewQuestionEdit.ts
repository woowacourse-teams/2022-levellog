import { useEffect, useRef, useState } from 'react';

import {
  InterviewQuestionDeleteRequestType,
  InterviewQuestionEditRequestType,
} from 'apis/InterviewQuestion';
import { InterviewQuestionInfoType } from 'types/interviewQuestion';

const useInterviewQuestionEdit = ({
  InterviewQuestionInfo,
  onClickDeleteInterviewQuestionButton,
  onSubmitEditInterviewQuestion,
}: useInterviewQuestionEditProps) => {
  const [isEditInterviewQuestion, setIsEditInterviewQuestion] = useState(false);
  const InterviewQuestionEditRef = useRef<HTMLInputElement>(null);

  const handleClickEditInterviewQuestionButton = () => {
    setIsEditInterviewQuestion((prev) => !prev);
  };

  const handleClickDeleteInterviewQuestionButton = () => {
    onClickDeleteInterviewQuestionButton({ InterviewQuestionId: InterviewQuestionInfo.id });
  };

  const handleSubmitEditCompleteInterviewQuestion = async (e: React.FormEvent<HTMLFormElement>) => {
    setIsEditInterviewQuestion((prev) => !prev);
    if (InterviewQuestionEditRef.current) {
      onSubmitEditInterviewQuestion({
        InterviewQuestionId: InterviewQuestionInfo.id,
        InterviewQuestion: InterviewQuestionEditRef.current.value,
      });
    }
  };

  useEffect(() => {
    if (isEditInterviewQuestion && InterviewQuestionEditRef.current) {
      InterviewQuestionEditRef.current.value = InterviewQuestionInfo.content;
      InterviewQuestionEditRef.current.focus();
    }
  }, [isEditInterviewQuestion]);

  return {
    isEditInterviewQuestion,
    InterviewQuestionEditRef,
    handleClickEditInterviewQuestionButton,
    handleClickDeleteInterviewQuestionButton,
    handleSubmitEditCompleteInterviewQuestion,
  };
};

interface useInterviewQuestionEditProps {
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

export default useInterviewQuestionEdit;
