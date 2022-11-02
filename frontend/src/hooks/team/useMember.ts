import { useEffect, useState, useContext } from 'react';
import { useParams } from 'react-router-dom';

import { UserType } from 'types';

import { useMutation } from '@tanstack/react-query';

import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { requestGetMembers } from 'apis/member';
import { requestGetTeam } from 'apis/teams';
import { MemberDispatchContext } from 'contexts/memberContext';
import { debounce } from 'utils/util';

const useMember = () => {
  const { showSnackbar } = useSnackbar();
  const [watchers, setWatchers] = useState<UserType[]>([]);
  const [participants, setParticipants] = useState<UserType[]>([]);

  const [watchersOfMembers, setWatchersOfMembers] = useState<UserType[]>([]);
  const [participantsOfMembers, setParticipantsOfMembers] = useState<UserType[]>([]);

  const [watcherNicknameValue, setWatcherNicknameValue] = useState('');
  const [participantNicknameValue, setParticipantNicknameValue] = useState('');

  const participantAndWatcherDispatch = useContext(MemberDispatchContext);
  const { teamId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const memberFilter = (args: Record<'members', UserType[]>) => {
    return args?.members
      .filter((member) => participants.every((participant) => participant.id !== member.id))
      .filter((member) => watchers.every((watcher) => watcher.id !== member.id));
  };

  const { mutateAsync: getTeam } = useMutation(
    () => {
      return requestGetTeam({ accessToken, teamId });
    },
    {
      onSuccess: (res) => {
        const participants = res.participants.map((participant) => {
          return {
            id: participant.memberId,
            nickname: participant.nickname,
            profileUrl: participant.profileUrl,
          };
        });
        const watchers = res.watchers.map((watcher) => {
          return {
            id: watcher.memberId,
            nickname: watcher.nickname,
            profileUrl: watcher.profileUrl,
          };
        });
        setWatchers(watchers);
        setParticipants(participants);
      },
      onError: (err) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const { data: members, mutateAsync: getMembers } = useMutation(
    () => {
      return requestGetMembers({ accessToken, nickname: '' });
    },
    {
      onSuccess: (res) => {
        setWatchersOfMembers(res.members);
        setParticipantsOfMembers(res.members);
      },
      onError: (err) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const { mutate: getMembersForWatchers } = useMutation(
    ({ nickname }: Pick<UserType, 'nickname'>) => {
      return requestGetMembers({ accessToken, nickname });
    },
    {
      onSuccess: (res) => {
        setWatchersOfMembers(memberFilter(res));
      },
      onError: (err) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const { mutate: getMembersForParticipants } = useMutation(
    ({ nickname }: Pick<UserType, 'nickname'>) => {
      return requestGetMembers({ accessToken, nickname });
    },
    {
      onSuccess: (res) => {
        setParticipantsOfMembers(memberFilter(res));
      },
      onError: (err) => {
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const addWatcher = ({ id, nickname, profileUrl }: UserType) => {
    setWatchers((prev) => prev.concat({ id, nickname, profileUrl }));
    setWatcherNicknameValue('');
  };

  const addParticipant = ({ id, nickname, profileUrl }: UserType) => {
    setParticipants((prev) => prev.concat({ id, nickname, profileUrl }));
    setParticipantNicknameValue('');
  };

  const removeWatcher = ({ id }: Pick<UserType, 'id'>) => {
    setWatchers(watchers.filter((watcher) => id !== watcher.id));
  };

  const removeParticipant = ({ id }: Pick<UserType, 'id'>) => {
    setParticipants(participants.filter((participant) => id !== participant.id));
  };

  const handleChangeWatcherInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setWatcherNicknameValue(e.target.value);
    debounce.action<Pick<UserType, 'nickname'>>({
      func: getMembersForWatchers,
      args: { nickname: e.target.value },
      timer: 200,
    });
  };

  const handleChangeParticipantInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setParticipantNicknameValue(e.target.value);
    debounce.action<Pick<UserType, 'nickname'>>({
      func: getMembersForParticipants,
      args: { nickname: e.target.value },
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
    if (teamId) {
      getTeam();
    }
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
