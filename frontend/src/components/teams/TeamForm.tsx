import { useEffect } from 'react';

import styled from 'styled-components';

import Member from './Member';
import Participant from './Participants';
import TeamFormInput from './TeamFormInput';
import { MemberType } from 'types/member';

const TeamForm = ({
  teamInfoRef,
  participants,
  members,
  nicknameValue,
  setNicknameValue,
  handleChangeInput,
  updateParticipants,
  getTeamOnRef,
}: TeamFormProps) => {
  useEffect(() => {
    if (!getTeamOnRef) return;
    getTeamOnRef();
  }, []);

  return (
    <S.FormContainer>
      <TeamFormInput
        label={'제목'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[0] = el)}
        minLength={'3'}
        maxLength={'14'}
        required
      />
      <TeamFormInput
        label={'장소'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[1] = el)}
        minLength={'3'}
        maxLength={'12'}
        required
      />
      <TeamFormInput
        label={'날짜'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[2] = el)}
        type={'date'}
        required
      />
      <TeamFormInput
        label={'시간'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[3] = el)}
        type={'time'}
        required
      />
      <TeamFormInput
        label={'인터뷰어의 수'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[4] = el)}
        type={'number'}
        min={'1'}
        max={'3'}
        required
      />
      <TeamFormInput label={'참가자'} value={nicknameValue} onChange={handleChangeInput}>
        <S.ParticipantsBox>
          {participants.map((participant: MemberType) => (
            <Participant
              key={participant.id}
              participant={participant}
              updateParticipants={updateParticipants}
            />
          ))}
        </S.ParticipantsBox>
      </TeamFormInput>
      <S.MembersBox isNoneMember={members.length === 0}>
        {members.map((member: MemberType) => (
          <Member
            key={member.id}
            member={member}
            setNicknameValue={setNicknameValue}
            updateParticipants={updateParticipants}
          />
        ))}
      </S.MembersBox>
    </S.FormContainer>
  );
};

interface TeamFormProps {
  teamInfoRef: React.MutableRefObject<HTMLInputElement[]>;
  participants: Array<MemberType>;
  nicknameValue: string;
  members: Array<MemberType>;
  setNicknameValue: React.Dispatch<React.SetStateAction<string>>;
  handleChangeInput: (e: React.ChangeEvent<HTMLInputElement>) => void;
  updateParticipants: ({ id, nickname, profileUrl }: MemberType) => void;
  getTeamOnRef?: () => Promise<void>;
}

const S = {
  FormContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1.5rem;
  `,

  ParticipantsBox: styled.div`
    display: flex;
    flex-wrap: wrap;
    overflow: auto;
    gap: 1rem;
    width: 42.625rem;
    @media (max-width: 1024px) {
      width: 31.25rem;
    }
    @media (max-width: 560px) {
      width: 18.75rem;
    }
  `,

  Notice: styled.p`
    color: ${(props) => props.theme.default.RED};
    font-size: 0.875rem;
    margin-bottom: 0.625rem;
  `,

  MembersBox: styled.div<{ isNoneMember: Boolean }>`
    display: ${(props) => (props.isNoneMember ? 'none' : 'block')};
    box-sizing: content-box;
    overflow: auto;
    width: 40.625rem;
    height: 10rem;
    padding: 1rem;
    border: 0.0625rem solid ${(props) => props.theme.default.GRAY};
    border-radius: 0.3125rem;
    background-color: ${(props) => props.theme.default.WHITE};
    @media (max-width: 1024px) {
      width: 31.25rem;
    }
    @media (max-width: 560px) {
      width: 18.75rem;
    }
  `,
};

export default TeamForm;
