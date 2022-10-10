import { useEffect } from 'react';

import styled from 'styled-components';

import { MESSAGE } from 'constants/constants';

import Button from 'components/@commons/Button';
import Member from 'components/teams/Member';
import Participant from 'components/teams/Participants';
import TeamFormInput from 'components/teams/TeamFormInput';
import { MemberType } from 'types/member';
import {
  interviewDateValidate,
  interviewInterviewerValidate,
  interviewLocationValidate,
  interviewTimeValidate,
  interviewTitleValidate,
} from 'utils/validate';

const TeamForm = ({
  purpose,
  participantNicknameValue,
  watcherNicknameValue,
  participantMembers,
  watcherMembers,
  participants,
  watchers,
  teamInfoRef,
  setParticipantNicknameValue,
  setWatcherNicknameValue,
  addToParticipants,
  addToWatcherParticipants,
  removeToParticipants,
  removeToWatcherParticipants,
  handleChangeParticipantInput,
  handleChangeWatcherInput,
  handleClickTeamButton,
}: TeamFormProps) => {
  //   useEffect(() => {
  //     if (!getTeamOnRef) return;
  //
  //     getTeamOnRef();
  //   }, []);

  return (
    <S.Container>
      <S.Title>인터뷰 팀 {purpose}</S.Title>
      <TeamFormInput
        label={'제목'}
        errorText={MESSAGE.INTERVIEW_TITLE_VALIDATE_FAIL}
        validate={interviewTitleValidate}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[0] = el)}
      />
      <TeamFormInput
        label={'장소'}
        errorText={MESSAGE.INTERVIEW_LOCATION_VALIDATE_FAIL}
        validate={interviewLocationValidate}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[1] = el)}
      />
      <TeamFormInput
        label={'날짜'}
        type={'date'}
        errorText={MESSAGE.INTERVIEW_DATE_VALIDATE_FAIL}
        validate={interviewDateValidate}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[2] = el)}
      />
      <TeamFormInput
        label={'시간'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[3] = el)}
        type={'time'}
        errorText={MESSAGE.INTERVIEW_TIME_VALIDATE_FAIL}
        validate={interviewTimeValidate}
      />
      <TeamFormInput
        label={'인터뷰어의 수'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[4] = el)}
        type={'number'}
        errorText={MESSAGE.INTERVIEW_INTERVIEWEE_VALIDATE_FAIL}
        validate={interviewInterviewerValidate}
      />
      <TeamFormInput
        label={'참관자'}
        value={watcherNicknameValue}
        onChange={handleChangeWatcherInput}
      >
        <S.ParticipantsBox>
          {watchers.length !== 0 &&
            watchers.map((watcher: MemberType) => (
              <Participant
                key={watcher.id}
                participant={watcher}
                removeToParticipants={removeToWatcherParticipants}
              />
            ))}
        </S.ParticipantsBox>
      </TeamFormInput>
      <S.MembersBox isNoneMember={watcherMembers.length === 0}>
        {watcherMembers.map((watcherMember: MemberType) => (
          <Member
            key={watcherMember.id}
            member={watcherMember}
            setNicknameValue={setWatcherNicknameValue}
            addToParticipants={addToWatcherParticipants}
          />
        ))}
      </S.MembersBox>
      <TeamFormInput
        label={'참가자'}
        value={participantNicknameValue}
        onChange={handleChangeParticipantInput}
      >
        <S.ParticipantsBox>
          {participants.map((participant: MemberType) => (
            <Participant
              key={participant.id}
              participant={participant}
              removeToParticipants={removeToParticipants}
            />
          ))}
        </S.ParticipantsBox>
      </TeamFormInput>
      <S.MembersBox isNoneMember={participantMembers.length === 0}>
        {participantMembers.map((participantMember: MemberType) => (
          <Member
            key={participantMember.id}
            member={participantMember}
            setNicknameValue={setParticipantNicknameValue}
            addToParticipants={addToParticipants}
          />
        ))}
      </S.MembersBox>
      <S.SubmitButton onClick={handleClickTeamButton}>{purpose}</S.SubmitButton>
    </S.Container>
  );
};

interface TeamFormProps {
  purpose: '생성하기' | '수정하기';
  participantNicknameValue: string;
  watcherNicknameValue: string;
  participantMembers: Array<MemberType>;
  watcherMembers: Array<MemberType>;
  participants: Array<MemberType>;
  watchers: Array<MemberType>;
  teamInfoRef: React.MutableRefObject<HTMLInputElement[]>;
  setParticipantNicknameValue: React.Dispatch<React.SetStateAction<string>>;
  setWatcherNicknameValue: React.Dispatch<React.SetStateAction<string>>;
  // getTeamOnRef?: () => Promise<void>;
  addToParticipants: ({ id, nickname, profileUrl }: MemberType) => void;
  addToWatcherParticipants: ({ id, nickname, profileUrl }: MemberType) => void;
  removeToParticipants: ({ id, nickname, profileUrl }: MemberType) => void;
  removeToWatcherParticipants: ({ id, nickname, profileUrl }: MemberType) => void;
  handleChangeParticipantInput: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleChangeWatcherInput: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleClickTeamButton: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const S = {
  Container: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1.5rem;
    width: 45.125rem;
    height: max-content;
    margin: 1.25rem auto 1.25rem auto;
    padding: 0 1.875rem 1.875rem 1.875rem;
    border-radius: 1.25rem;
    box-shadow: 0.0625rem 0.25rem 0.625rem ${(props) => props.theme.new_default.GRAY};
    @media (min-width: 560px) and (max-width: 760px) {
      width: 32.5rem;
    }
    @media (max-width: 560px) {
      justify-content: center;
      width: calc(100vw - 2.5rem);
      box-shadow: none;
    }
  `,

  Title: styled.h1`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 6.25rem;
    font-weight: 600;
  `,

  ErrorBox: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    width: 100%;
  `,

  ErrorText: styled.p``,

  ParticipantsBox: styled.div`
    display: flex;
    flex-wrap: wrap;
    overflow: auto;
    gap: 0.5rem;
    width: 100%;
    padding-bottom: 1rem;
    @media (min-width: 560px) and (max-width: 760px) {
      width: 27.5rem;
    }
    @media (max-width: 560px) {
      width: calc(100vw - 5rem);
    }
  `,

  Notice: styled.p`
    color: ${(props) => props.theme.default.RED};
    font-size: 0.875rem;
    margin-bottom: 0.625rem;
  `,

  MembersBox: styled.div<{ isNoneMember: Boolean }>`
    display: ${(props) => (props.isNoneMember ? 'none' : 'block')};
    box-sizing: border-box;
    overflow: auto;
    width: 100%;
    height: 10rem;
    padding: 1rem;
    border: 0.0625rem solid ${(props) => props.theme.default.GRAY};
    border-radius: 0.3125rem;
    background-color: ${(props) => props.theme.default.WHITE};
  `,

  SubmitButton: styled(Button)`
    width: 100%;
    height: 3.125rem;
  `,
};

export default TeamForm;
