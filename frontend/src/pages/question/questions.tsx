import { useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellogQuery from 'hooks/levellog/useLevellogQuery';
import useQuestions from 'hooks/question/useQuestions';
import useTeam from 'hooks/team/useTeam';
import useUser from 'hooks/useUser';

import EmptyQuestion from 'pages/status/EmptyQuestion';
import Loading from 'pages/status/Loading';

import { TEAM_STATUS } from 'constants/constants';
import { GITHUB_AVATAR_SIZE_LIST } from 'constants/constants';

import ContentHeader from 'components/@commons/ContentHeader';
import Image from 'components/@commons/Image';
import { QuestionsInLevellogInfoType, QuestionInfoType } from 'types/question';
import { feedbackAddUriBuilder } from 'utils/uri';
import { checkFirstWordFinalConsonant } from 'utils/util';

const Questions = () => {
  const { levellogInfo } = useLevellogQuery();
  const { Questions } = useQuestions();
  const { loginUserId, loginUserNickname, loginUserProfileUrl } = useUser();
  const { team } = useTeam();
  const { teamId, levellogId } = useParams();

  if (
    !loginUserId ||
    !loginUserNickname ||
    !loginUserProfileUrl ||
    !levellogInfo ||
    !team ||
    Object.keys(levellogInfo).length === 0
  ) {
    return <Loading />;
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
      />
      <S.Container>
        {Questions?.length === 0 && (
          <EmptyQuestion
            isShow={team.status !== TEAM_STATUS.CLOSED && levellogInfo.author.id !== loginUserId}
            path={feedbackAddUriBuilder({ teamId, levellogId })}
          />
        )}
        {Questions?.map((QuestionInLevellogInfo: QuestionsInLevellogInfoType) => (
          <S.Box key={QuestionInLevellogInfo.author.id}>
            <S.AuthorBox>
              <Image
                src={QuestionInLevellogInfo.author.profileUrl}
                sizes={'MEDIUM'}
                githubAvatarSize={GITHUB_AVATAR_SIZE_LIST.MEDIUM}
              />
              <S.AuthorText>
                {checkFirstWordFinalConsonant({
                  word: QuestionInLevellogInfo.author.nickname,
                })
                  ? `${QuestionInLevellogInfo.author.nickname}이 `
                  : `${QuestionInLevellogInfo.author.nickname}가 `}
                기록해준 질문들
              </S.AuthorText>
            </S.AuthorBox>
            <S.Content>
              <ul>
                {QuestionInLevellogInfo.contents.length !== 0 &&
                  QuestionInLevellogInfo.contents.map((QuestionContent: QuestionInfoType) => (
                    <S.Text key={QuestionContent.id}>
                      <p>{QuestionContent.content}</p>
                    </S.Text>
                  ))}
              </ul>
            </S.Content>
          </S.Box>
        ))}
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

export default Questions;
