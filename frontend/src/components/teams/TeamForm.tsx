import { useEffect } from 'react';

import styled from 'styled-components';

import Member from './Member';
import Participant from './Participants';
import TeamFormInput from './TeamFormInput';
import Button from 'components/@commons/Button';
import { MemberType } from 'types/member';

const TeamForm = ({
  purpose,
  handleSubmitTeamForm,
  teamInfoRef,
  participants,
  members,
  nicknameValue,
  setNicknameValue,
  handleChangeInput,
  addToParticipants,
  removeToParticipants,
  getTeamOnRef,
}: TeamFormProps) => {
  useEffect(() => {
    if (!getTeamOnRef) return;
    getTeamOnRef();
  }, []);

  return (
    <S.FormContainer onSubmit={handleSubmitTeamForm}>
      <S.Title>인터뷰 팀 {purpose}</S.Title>
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
              removeToParticipants={removeToParticipants}
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
            addToParticipants={addToParticipants}
          />
        ))}
      </S.MembersBox>
      <S.SubmitButton>{purpose}</S.SubmitButton>
    </S.FormContainer>
  );
};

interface TeamFormProps {
  purpose: '생성하기' | '수정하기';
  handleSubmitTeamForm: (e: React.FormEvent<HTMLFormElement>) => void;
  teamInfoRef: React.MutableRefObject<HTMLInputElement[]>;
  participants: Array<MemberType>;
  nicknameValue: string;
  members: Array<MemberType>;
  setNicknameValue: React.Dispatch<React.SetStateAction<string>>;
  handleChangeInput: (e: React.ChangeEvent<HTMLInputElement>) => void;
  addToParticipants: ({ id, nickname, profileUrl }: MemberType) => void;
  removeToParticipants: ({ id, nickname, profileUrl }: MemberType) => void;
  getTeamOnRef?: () => Promise<void>;
}

const S = {
  FormContainer: styled.form`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1.5rem;
    width: 722px;
    height: max-content;
    margin: 20px auto 0 auto;
    padding-bottom: 30px;
    border-radius: 20px;
    box-shadow: 0.0625rem 0.25rem 0.625rem ${(props) => props.theme.new_default.GRAY};
    @media (min-width: 560px) and (max-width: 800px) {
    }
    @media (max-width: 560px) {
      justify-content: center;
      width: 362.5px;
      margin: 20px 0 0 0;
    }
  `,

  Title: styled.h1`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100px;
    font-weight: 600;
  `,

  ParticipantsBox: styled.div`
    display: flex;
    flex-wrap: wrap;
    overflow: auto;
    gap: 0.5rem;
    width: 42.625rem;
    padding-bottom: 16px;
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

  SubmitButton: styled(Button)`
    width: 42.625rem;
    height: 3.125rem;
    @media (max-width: 1024px) {
      width: 31.25rem;
    }
    @media (max-width: 560px) {
      width: 18.75rem;
    }
  `,
};

export default TeamForm;
