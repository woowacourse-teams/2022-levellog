import { useEffect, useState, useContext } from 'react';

import { useMutation } from '@tanstack/react-query';

import { requestGetMembers } from 'apis/member';
import { MemberDispatchContext } from 'contexts/memberContext';
import { MembersCustomHookType, MemberType } from 'types/member';
import { debounce } from 'utils/util';

const useMember = () => {
  const [watchers, setWatchers] = useState<MemberType[]>([]);
  const [participants, setParticipants] = useState<MemberType[]>([]);

  const [watchersOfMembers, setWatchersOfMembers] = useState<MemberType[]>([]);
  const [participantsOfMembers, setParticipantsOfMembers] = useState<MemberType[]>([]);

  const [watcherNicknameValue, setWatcherNicknameValue] = useState('');
  const [participantNicknameValue, setParticipantNicknameValue] = useState('');

  const participantAndWatcherDispatch = useContext(MemberDispatchContext);

  const accessToken = localStorage.getItem('accessToken');

  const memberFilter = (args: Record<'members', MemberType[]>) => {
    return args?.members
      .filter((member) => participants.every((participant) => participant.id !== member.id))
      .filter((member) => watchers.every((watcher) => watcher.id !== member.id));
  };

  const { data: members, mutate: getMembers } = useMutation(
    () => {
      return requestGetMembers({ accessToken, nickname: '' });
    },
    {
      onSuccess: (res) => {
        setWatchersOfMembers(res.members);
        setParticipantsOfMembers(res.members);
      },
    },
  );

  const { mutate: getMembersForWatchers } = useMutation(
    ({ nicknameValue = '' }: MembersCustomHookType) => {
      return requestGetMembers({ accessToken, nickname: nicknameValue });
    },
    {
      onSuccess: (res) => {
        setWatchersOfMembers(memberFilter(res));
      },
    },
  );

  const { mutate: getMembersForParticipants } = useMutation(
    ({ nicknameValue = '' }: MembersCustomHookType) => {
      return requestGetMembers({ accessToken, nickname: nicknameValue });
    },
    {
      onSuccess: (res) => {
        setParticipantsOfMembers(memberFilter(res));
      },
    },
  );

  const addWatcher = ({ id, nickname, profileUrl }: MemberType) => {
    setWatchers((prev) => prev.concat({ id, nickname, profileUrl }));
    setWatcherNicknameValue('');
  };

  const addParticipant = ({ id, nickname, profileUrl }: MemberType) => {
    setParticipants((prev) => prev.concat({ id, nickname, profileUrl }));
    setParticipantNicknameValue('');
  };

  const removeWatcher = ({ id }: Pick<MemberType, 'id'>) => {
    setWatchers(watchers.filter((watcher) => id !== watcher.id));
  };

  const removeParticipant = ({ id }: Pick<MemberType, 'id'>) => {
    setParticipants(participants.filter((participant) => id !== participant.id));
  };

  const handleChangeWatcherInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setWatcherNicknameValue(e.target.value);
    debounce.action({
      func: getMembersForWatchers,
      args: { nicknameValue: e.target.value },
      timer: 200,
    });
  };

  const handleChangeParticipantInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setParticipantNicknameValue(e.target.value);
    debounce.action({
      func: getMembersForParticipants,
      args: { nicknameValue: e.target.value },
      timer: 200,
    });
  };

  useEffect(() => {
    const membersFilter = memberFilter(members!);
    setWatchersOfMembers(membersFilter!);
    setParticipantsOfMembers(membersFilter!);
    participantAndWatcherDispatch({ participants, watchers });
  }, [participants, watchers]);

  useEffect(() => {
    getMembers();
  }, []);

  return {
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
  };
};

export default useMember;
