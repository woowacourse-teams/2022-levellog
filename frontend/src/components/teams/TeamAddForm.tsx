import styled from 'styled-components';

import Member from './Member';
import Participant from './Participants';
import TeamAddInput from './TeamAddInput';
import { MemberType } from 'types/member';

const TeamAddForm = ({
  teamInfoRef,
  participants,
  members,
  nickname,
  setNickname,
  handleChangeInput,
  updateParticipants,
}: TeamAddFormProps) => {
  return (
    <S.FormContainer>
      <TeamAddInput
        label={'제목'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[0] = el)}
        minLength={'5'}
        maxLength={'10'}
        required
      />
      <TeamAddInput
        label={'장소'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[1] = el)}
        minLength={'5'}
        maxLength={'10'}
        required
      />
      <TeamAddInput
        label={'날짜'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[2] = el)}
        type={'date'}
        required
      />
      <TeamAddInput
        label={'시간'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[3] = el)}
        type={'time'}
        required
      />
      <TeamAddInput
        label={'인터뷰어의 수'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[4] = el)}
        type={'number'}
        required
      />
      <TeamAddInput label={'참가자'} value={nickname} onChange={handleChangeInput}>
        <>
          {participants.length > 0 ? (
            <S.ParticipantsBox>
              {participants.map((participant: MemberType) => (
                <Participant
                  key={participant.id}
                  participant={participant}
                  updateParticipants={updateParticipants}
                />
              ))}
            </S.ParticipantsBox>
          ) : (
            <S.Notice>참가자를 추가해주세요!</S.Notice>
          )}
        </>
      </TeamAddInput>
      {members.length > 0 && (
        <S.MembersBox>
          {members.map((member: MemberType) => (
            <Member
              key={member.id}
              member={member}
              setNickname={setNickname}
              updateParticipants={updateParticipants}
            />
          ))}
        </S.MembersBox>
      )}
    </S.FormContainer>
  );
};

interface TeamAddFormProps {
  teamInfoRef: React.MutableRefObject<HTMLInputElement[]>;
  participants: Array<MemberType>;
  nickname: string;
  members: Array<MemberType>;
  setNickname: React.Dispatch<React.SetStateAction<string>>;
  handleChangeInput: (e: React.ChangeEvent<HTMLInputElement>) => void;
  updateParticipants: ({ id, nickname, profileUrl }: MemberType) => void;
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
    gap: 1rem;
  `,

  Notice: styled.p`
    color: ${(props) => props.theme.default.RED};
    font-size: 14px;
    margin-bottom: 10px;
  `,

  MembersBox: styled.div`
    box-sizing: content-box;
    width: 40.625rem;
    height: fit-content;
    padding: 1rem;
    border: 0.0625rem solid ${(props) => props.theme.default.GRAY};
    border-radius: 0.3125rem;
    background-color: ${(props) => props.theme.default.WHITE};
  `,
};

export default TeamAddForm;
