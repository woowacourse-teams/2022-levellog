import styled from 'styled-components';

import useMember from 'hooks/team/useMember';

import AddMember from 'components/teams/AddMember';
import Member from 'components/teams/Member';
import TeamFormInput from 'components/teams/TeamFormInput';
import { MemberType } from 'types/member';

const SearchMember = () => {
  const {
    watcherNicknameValue,
    participantNicknameValue,
    watchers,
    participants,
    watchersOfMembers,
    participantsOfMembers,
    addWatcher,
    addParticipant,
    removeWatcher,
    removeParticipant,
    handleChangeWatcherInput,
    handleChangeParticipantInput,
  } = useMember();

  return (
    <>
      <TeamFormInput
        label={'참관자'}
        value={watcherNicknameValue}
        onChange={handleChangeWatcherInput}
      >
        <S.ParticipantsBox>
          {watchers.length !== 0 &&
            watchers.map((watcher: MemberType) => (
              <AddMember key={watcher.id} addMember={watcher} removeEvent={removeWatcher} />
            ))}
        </S.ParticipantsBox>
      </TeamFormInput>

      <S.MembersBox>
        {watchersOfMembers?.length ? (
          watchersOfMembers.map((member: MemberType) => (
            <Member key={member.id} member={member} addEvent={addWatcher} />
          ))
        ) : (
          <p>검색되는 사용자가 없습니다.</p>
        )}
      </S.MembersBox>

      <TeamFormInput
        label={'참가자'}
        value={participantNicknameValue}
        onChange={handleChangeParticipantInput}
      >
        <S.ParticipantsBox>
          {participants.map((participant: MemberType) => (
            <AddMember
              key={participant.id}
              addMember={participant}
              removeEvent={removeParticipant}
            />
          ))}
        </S.ParticipantsBox>
      </TeamFormInput>

      <S.MembersBox>
        {participantsOfMembers?.length ? (
          participantsOfMembers.map((member: MemberType) => (
            <Member key={member.id} member={member} addEvent={addParticipant} />
          ))
        ) : (
          <p>검색되는 사용자가 없습니다.</p>
        )}
      </S.MembersBox>
    </>
  );
};

const S = {
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

  MembersBox: styled.div`
    box-sizing: border-box;
    overflow: auto;
    width: 100%;
    height: 10rem;
    padding: 1rem;
    border: 0.0625rem solid ${(props) => props.theme.default.GRAY};
    border-radius: 0.3125rem;
    background-color: ${(props) => props.theme.default.WHITE};
  `,
};

export default SearchMember;
