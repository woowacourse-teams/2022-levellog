import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import useContentTag from 'hooks/useContentTag';
import useFeedback from 'hooks/useFeedback';
import useInterviewQuestion from 'hooks/useInterviewQuestion';
import useLevellog from 'hooks/useLevellog';
import usePreQuestion from 'hooks/usePreQuestion';
import useRole from 'hooks/useRole';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

const useFeedbackAddPage = () => {
  const { levellogInfo, getLevellog } = useLevellog();
  const { feedbackRef, onClickFeedbackAddButton } = useFeedback();
  const { feedbackWriterRole, getWriterInfo } = useRole();
  const { preQuestion, getPreQuestion } = usePreQuestion();
  const {
    interviewQuestionInfos,
    interviewQuestionRef,
    interviewQuestionContentRef,
    getInterviewQuestion,
    onClickDeleteInterviewQuestionButton,
    onSubmitEditInterviewQuestion,
    handleSubmitInterviewQuestion,
  } = useInterviewQuestion();
  const { whichContentShow, handleClickLevellogTag, handleClickPreQuestionTag } = useContentTag();
  const { teamId, levellogId } = useParams();
  const navigate = useNavigate();

  const handleClickFeedbackAddButton = () => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      onClickFeedbackAddButton({ teamId, levellogId });

      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
  };

  const init = async () => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      const res = await getLevellog({ teamId, levellogId });
      await getWriterInfo({ teamId, participantId: res!.author.id });
      getInterviewQuestion();
      getPreQuestion({ levellogId });

      return;
    }

    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.ERROR);
  };

  useEffect(() => {
    init();
  }, []);

  return {
    state: {
      levellogInfo,
      feedbackWriterRole,
      interviewQuestionInfos,
      preQuestion,
      whichContentShow,
    },
    ref: { feedbackRef, interviewQuestionRef, interviewQuestionContentRef },
    handler: {
      onClickDeleteInterviewQuestionButton,
      onSubmitEditInterviewQuestion,
      handleClickFeedbackAddButton,
      handleSubmitInterviewQuestion,
      handleClickLevellogTag,
      handleClickPreQuestionTag,
    },
  };
};

export default useFeedbackAddPage;
