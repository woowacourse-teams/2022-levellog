import styled from 'styled-components';

import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/Image';
import { InterviewTeamType, ParticipantType } from 'types/team';

const InterviewTeam = ({
  id,
  teamImage,
  hostId,
  title,
  place,
  startAt,
  isClosed,
  participants,
}: InterviewTeamType) => {
  return (
    <S.Container id={id} isClosed={isClosed}>
      <FlexBox gap={0.625}>
        <Image src={teamImage} sizes={'LARGE'} />
        <FlexBox flexFlow="column wrap" gap={0.625}>
          <S.Title id={id}>{title}</S.Title>
          <p>{'dm 주소'}</p>
        </FlexBox>
      </FlexBox>
      <FlexBox flexFlow="row">
        <S.Info>
          <S.Notice>Where</S.Notice>
          <S.NoticeContent>{place}</S.NoticeContent>
        </S.Info>
        <S.Info>
          <S.Notice>When</S.Notice>
          <S.NoticeContent>{startAt}</S.NoticeContent>
        </S.Info>
      </FlexBox>
      <FlexBox>
        {participants.map((participant: ParticipantType) => (
          <Image key={participant.memberId} src={participant.profileUrl} sizes={'SMALL'} />
        ))}
      </FlexBox>
    </S.Container>
  );
};

const S = {
  Container: styled.div<{ isClosed: Boolean }>`
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
    opacity: ${(props) => (props.isClosed ? 0.2 : 1)};
    width: 20.25rem;
    min-width: 20.25rem;
    height: 15rem;
    padding: 1.25rem 1.875rem 1.875rem 1.5rem;
    border: 0.0625rem solid ${(props) => props.theme.default.BLACK};
    cursor: pointer;
  `,

  Title: styled.h3`
    width: 11.5rem;
    word-break: break-all;
  `,

  Info: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    width: 8.625rem;
    height: 3rem;
  `,

  Notice: styled.p`
    color: ${(props) => props.theme.default.DARK_GRAY};
  `,

  NoticeContent: styled.p`
    font-size: 0.875rem;
  `,
};

export default InterviewTeam;
