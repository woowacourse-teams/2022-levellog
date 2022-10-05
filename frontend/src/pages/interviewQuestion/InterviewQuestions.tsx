import { useEffect } from 'react';
import { useParams } from 'react-router-dom';

import styled from 'styled-components';

import useInterviewQuestion from 'hooks/interviewQuestion/useInterviewQuestion';
import useLevellog from 'hooks/useLevellog';
import useTeam from 'hooks/useTeam';
import useUser from 'hooks/useUser';

import EmptyInterviewQuestion from 'pages/status/EmptyInterviewQuestion';
import Loading from 'pages/status/Loading';

import { TEAM_STATUS } from 'constants/constants';
import { GITHUB_AVATAR_SIZE_LIST } from 'constants/constants';

import ContentHeader from 'components/@commons/ContentHeader';
import Image from 'components/@commons/Image';
import {
  InterviewQuestionsInLevellogType,
  InterviewQuestionInfoType,
} from 'types/interviewQuestion';
import { checkFirstWordFinalConsonant, feedbackAddUriBuilder } from 'utils/util';

const InterviewQuestions = () => {
  const { levellogInfo } = useLevellog();
  const { interviewQuestionInfosInLevellog } = useInterviewQuestion();
  const { loginUserId, loginUserNickname, loginUserProfileUrl } = useUser();
  const { team } = useTeam();
  const { teamId, levellogId } = useParams();

  if (typeof levellogId !== 'string' || typeof teamId !== 'string') {
    return <Loading />;
  }

  useEffect(() => {
    getLevellog({ teamId, levellogId });
  }, []);

  if (
    !loginUserId ||
    !loginUserNickname ||
    !loginUserProfileUrl ||
    Object.keys(levellogInfo).length === 0
  ) {
    return <Loading />;
  }

  if (interviewQuestionInfosInLevellog.length === 0) {
    return (
      <>
        <ContentHeader
          imageUrl={loginUserProfileUrl}
          title={`${
            checkFirstWordFinalConsonant({
              word: loginUserNickname,
            })
              ? `${loginUserNickname}이 `
              : `${loginUserNickname}가 `
          }
        받은 인터뷰 질문들`}
        ></ContentHeader>
        <EmptyInterviewQuestion
          isShow={team.status !== TEAM_STATUS.CLOSED && levellogInfo.author.id !== loginUserId}
          path={feedbackAddUriBuilder({ teamId, levellogId })}
        />
      </>
    );
  }

  return (
    <>
      <ContentHeader
        imageUrl={loginUserProfileUrl}
        title={`${
          checkFirstWordFinalConsonant({
            word: loginUserNickname,
          })
            ? `${loginUserNickname}이 `
            : `${loginUserNickname}가 `
        }
        받은 인터뷰 질문들`}
      ></ContentHeader>
      <S.Container>
        {interviewQuestionInfosInLevellog.map(
          (interviewQuestionInfoInLevellog: InterviewQuestionsInLevellogType) => (
            <S.Box key={interviewQuestionInfoInLevellog.author.id}>
              <S.AuthorBox>
                <Image
                  src={interviewQuestionInfoInLevellog.author.profileUrl}
                  sizes={'MEDIUM'}
                  githubAvatarSize={GITHUB_AVATAR_SIZE_LIST.MEDIUM}
                />
                <S.AuthorText>
                  {checkFirstWordFinalConsonant({
                    word: interviewQuestionInfoInLevellog.author.nickname,
                  })
                    ? `${interviewQuestionInfoInLevellog.author.nickname}이 `
                    : `${interviewQuestionInfoInLevellog.author.nickname}가 `}
                  기록해준 질문들
                </S.AuthorText>
              </S.AuthorBox>
              <S.Content>
                <ul>
                  {interviewQuestionInfoInLevellog.contents.length !== 0 &&
                    interviewQuestionInfoInLevellog.contents.map(
                      (interviewQuestionContent: InterviewQuestionInfoType) => (
                        <S.Text key={interviewQuestionContent.id}>
                          <p>{interviewQuestionContent.content}</p>
                        </S.Text>
                      ),
                    )}
                </ul>
              </S.Content>
            </S.Box>
          ),
        )}
      </S.Container>
    </>
  );
};

const S = {
  Container: styled.div`
    margin: auto;
    width: 100%;
    max-width: 71rem;
    min-width: 18.75rem;
    @media (max-width: 1160px) {
      width: calc(100% - 2.5rem);
    }
  `,

  Box: styled.div`
    margin-bottom: 3.125rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    border-radius: 0.5rem;
    box-shadow: 0 0 1rem ${(props) => props.theme.new_default.GRAY};
  `,

  AuthorBox: styled.div`
    display: flex;
    align-items: center;
    gap: 0.375rem;
    padding: 0.875rem;
    width: 100%;
    border-bottom: 0.125rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  `,

  AuthorText: styled.p`
    font-size: 2rem;
    font-weight: 300;
  `,

  Content: styled.div`
    margin: 0.875rem;
    padding: 0.625rem 0.625rem 0.625rem 1.25rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    line-height: 1.5rem;
    word-break: keep-all;
  `,

  Text: styled.li`
    list-style-type: circle;
  `,
};

export default InterviewQuestions;
