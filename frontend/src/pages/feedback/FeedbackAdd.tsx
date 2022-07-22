import React, { useEffect, useRef, useState } from 'react';
import { useLocation, useParams } from 'react-router-dom';

import styled from 'styled-components';
import { FeedbackPostType } from 'types';

import useFeedback from 'hooks/useFeedback';
//////
import useLevellog from 'hooks/useLevellog';
/////
import useTeams from 'hooks/useTeams';
/////

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import FeedbackForm from 'components/feedbacks/FeedbackForm';
import LevellogReport from 'components/levellogs/LevellogReport';

const FeedbackAdd = () => {
  const { levellogId, teamId } = useParams();
  const { feedbackAdd } = useFeedback();
/////
  const { levellogLookup } = useLevellog();
  const [levellog, setLevellog] = useState();
/////
  const { teamRequestAndDispatch } = useTeams();
//////
  const feedbackRef = useRef([]);

  const handleSubmitFeedbackForm = async (e: any) => {
    e.preventDefault();
    const feedbackResult: FeedbackPostType = {
      feedback: {
        study: feedbackRef.current[1].value,
        speak: feedbackRef.current[2].value,
        etc: feedbackRef.current[3].value,
      },
    };

    await feedbackAdd({ feedbackResult, levellogId });
    // 혹시 몰라서 호출하지만, TeamDetail 페이지로 라우팅했을 때 오류 안 날 경우 제거.
    teamRequestAndDispatch();
  };

  const handleClickFeedbackForm = (e: any) => {
    const [study, speak, etc] = feedbackRef.current;
    const feedbackResult: FeedbackPostType = {
      feedback: {
        study: study.value,
        speak: speak.value,
        etc: etc.value,
      },
    };

    feedbackAdd({ feedbackResult, levellogId });
  };

  const requestLevellog = async () => {
    const res = await levellogLookup(teamId, levellogId);
    setLevellog(res.content);
  };

  useEffect(() => {
    requestLevellog();
  }, []);

  return (
    <FlexBox gap={1.875}>
      <ContentHeader title={'레벨로그 피드백'}>
        <Button onClick={handleClickFeedbackForm}>등록하기</Button>
      </ContentHeader>
      <FeedbackAddContainer>
        <LevellogReport levellog={levellog} />
        <FeedbackForm
          feedbackRef={feedbackRef}
          handleSubmitFeedbackForm={handleSubmitFeedbackForm}
        />
      </FeedbackAddContainer>
    </FlexBox>
  );
};

const FeedbackAddContainer = styled.div`
  display: flex;
  overflow: auto;
  width: 100%;
  gap: 4.875rem;
  @media (max-width: 1024px) {
    gap: 1.875rem;
  }
  @media (max-width: 520px) {
    flex-direction: column;
  }
`;

export default FeedbackAdd;
