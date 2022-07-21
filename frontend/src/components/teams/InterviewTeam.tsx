import styled from 'styled-components';
import { InterviewTeamType, ParticipantType } from 'types';

import FlexBox from '../@commons/FlexBox';
import Image from '../@commons/Image';

const InterviewTeam = ({
  id,
  teamImage,
  hostId,
  title,
  place,
  startAt,
  participants,
}: InterviewTeamType) => {
  return (
    <InterviewTeamContainer id={id}>
      <FlexBox gap={0.625}>
        <Image src={teamImage} sizes={'LARGE'} />
        <FlexBox flexFlow="column wrap" gap={0.625}>
          <InterviewTeamTitle id={id}>{title}</InterviewTeamTitle>
          <p>{'dm 주소'}</p>
        </FlexBox>
      </FlexBox>
      <FlexBox flexFlow="row">
        <InterviewInfoStyle>
          <NoticeStyle>Where</NoticeStyle>
          <NoticeContentStyle>{place}</NoticeContentStyle>
        </InterviewInfoStyle>
        <InterviewInfoStyle>
          <NoticeStyle>When</NoticeStyle>
          <NoticeContentStyle>{startAt}</NoticeContentStyle>
        </InterviewInfoStyle>
      </FlexBox>
      <FlexBox>
        {participants.map((participant: ParticipantType) => (
          <Image key={participant.id} src={participant.profileUrl} sizes={'SMALL'} />
        ))}
      </FlexBox>
    </InterviewTeamContainer>
  );
};

const InterviewTeamContainer = styled.div`
  display: flex;
  width: 324px;
  min-width: 324px;
  height: 240px;
  padding: 1.25rem 1.875rem 1.875rem 1.5rem;
  border: 1px solid ${(props) => props.theme.default.BLACK};
  flex-direction: column;
  gap: 1.5rem;
  cursor: pointer;
`;

const InterviewTeamTitle = styled.h3`
  width: 11.5rem;
  word-break: break-all;
`;

const InterviewInfoStyle = styled.div`
  display: flex;
  width: 138px;
  height: 48px;
  flex-direction: column;
  justify-content: space-between;
`;

const NoticeStyle = styled.p`
  color: ${(props) => props.theme.default.DARK_GRAY};
`;

const NoticeContentStyle = styled.p`
  font-size: 0.875rem;
`;

export default InterviewTeam;
