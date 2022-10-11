import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { useMutation } from '@tanstack/react-query';

import useTeam from 'hooks/team/useTeam';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import { requestGetMembers } from 'apis/member';
import { requestPostTeam } from 'apis/teams';
import { MembersCustomHookType, MemberType } from 'types/member';
import { TeamCustomHookType } from 'types/team';
import {
  interviewDateValidate,
  interviewInterviewerValidate,
  interviewLocationValidate,
  interviewTimeValidate,
  interviewTitleValidate,
} from 'utils/validate';

const useTeamAdd = () => {
  const { participants, watchers, members, setParticipants, setWatchers } = useTeam();
  const { showSnackbar } = useSnackbar();
  const teamInfoRef = useRef<HTMLInputElement[]>([]);

  const [participantNicknameValue, setParticipantNicknameValue] = useState('');
  const [watcherNicknameValue, setWatcherNicknameValue] = useState('');
  const [participantMembers, setParticipantMembers] = useState<MemberType[]>([]);
  const [watcherMembers, setWatcherMembers] = useState<MemberType[]>([]);
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const convertTeamInfoFormat = () => {
    const [title, place, date, time, interviewerNumber] = teamInfoRef.current;
    const teamInfo = {
      title: title.value,
      place: place.value,
      startAt: `${date.value}T${time.value}`,
      interviewerNumber: Number(interviewerNumber.value),
      watcherIds: Object.values(watchers).map((watcher) => watcher.id),
      participantIds: Object.values(participants).map((participant) => participant.id),
    };

    return teamInfo;
  };

  const validateTeamInfo = () => {
    const [title, place, date, time, interviewerNumber] = teamInfoRef.current;
    if (
      interviewTitleValidate({ value: title.value }) &&
      interviewLocationValidate({ value: place.value }) &&
      interviewDateValidate({ value: date.value }) &&
      interviewTimeValidate({ value: time.value }) &&
      interviewInterviewerValidate({ value: +interviewerNumber.value })
      // interviewParticipantValidate({ value: participants.length })
    ) {
      return true;
    }

    return false;
  };

  const { mutate: postTeam } = useMutation(
    ({ teamInfo }: Record<'teamInfo', TeamCustomHookType>) => {
      return requestPostTeam({
        teamInfo,
        accessToken,
      });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.TEAM_CREATE });
        navigate(ROUTES_PATH.HOME);
      },
    },
  );

  const { mutate: getMembersInParticipants } = useMutation(
    ({ nicknameValue = ' ' }: MembersCustomHookType) => {
      return requestGetMembers({ accessToken, nickname: nicknameValue });
    },
    {
      onSuccess: (res) => {
        const membersFilter = res.data.members
          .filter((member) => participants.every((participant) => participant.id !== member.id))
          .filter((member) => watchers.every((watcher) => watcher.id !== member.id));
        setParticipantMembers(membersFilter);
      },
      onError: (err) => {
        if (axios.isAxiosError(err)) {
          const responseBody: AxiosResponse = err.response!;
          if (err instanceof Error) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  const { mutate: getMembersInWatchers } = useMutation(
    ({ nicknameValue = ' ' }: MembersCustomHookType) => {
      return requestGetMembers({ accessToken, nickname: nicknameValue });
    },
    {
      onSuccess: (res) => {
        const membersFilter = res.data.members
          .filter((member) => watchers.every((watcher) => watcher.id !== member.id))
          .filter((member) => participants.every((participant) => participant.id !== member.id));
        setWatcherMembers(membersFilter);
      },
      onError: (err) => {
        if (axios.isAxiosError(err)) {
          const responseBody: AxiosResponse = err.response!;
          if (err instanceof Error) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  const updateMembers = async ({ nicknameValue = ' ' }: MembersCustomHookType) => {
    // if (isDebounce()) return;
    getMembersInParticipants({ nicknameValue });
  };

  const updateWatchers = async ({ nicknameValue = ' ' }: MembersCustomHookType) => {
    // if (isDebounce()) return;
    getMembersInWatchers({ nicknameValue });
  };

  const addToWatcherParticipants = ({ id, nickname, profileUrl }: MemberType) => {
    if (watchers.every((watcher) => id !== watcher.id)) {
      setWatchers((prev) => prev.concat({ id, nickname, profileUrl }));
    }
  };

  const addToParticipants = ({ id, nickname, profileUrl }: MemberType) => {
    if (participants.every((participant) => id !== participant.id)) {
      setParticipants((prev) => prev.concat({ id, nickname, profileUrl }));
    }
  };

  const removeToParticipants = ({ id }: Pick<MemberType, 'id'>) => {
    setParticipants(participants.filter((participant) => id !== participant.id));
  };

  const remoteToWatcherParticipants = ({ id }: Pick<MemberType, 'id'>) => {
    setWatchers(watchers.filter((watcher) => id !== watcher.id));
  };

  const handleClickTeamAddButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    if (validateTeamInfo()) {
      postTeam({ teamInfo: convertTeamInfoFormat() });

      return;
    }
    showSnackbar({ message: MESSAGE.INTERVIEW_HOLE_VALUE_VALIDATE });
  };

  const handleChangeParticipantInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setParticipantNicknameValue(e.target.value);
  };

  const handleChangeWatcherInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setWatcherNicknameValue(e.target.value);
  };

  useEffect(() => {
    const membersFilter = members
      .filter((member) => watchers.every((watcher) => watcher.id !== member.id))
      .filter((member) => participants.every((participant) => participant.id !== member.id));
    setParticipantMembers(membersFilter);
    setWatcherMembers(membersFilter);
  }, [participants, watchers]);

  return {
    participantNicknameValue,
    watcherNicknameValue,
    watcherMembers,
    participantMembers,
    watchers,
    participants,
    teamInfoRef,
    setParticipantNicknameValue,
    setWatcherNicknameValue,
    updateMembers,
    updateWatchers,
    addToParticipants,
    addToWatcherParticipants,
    removeToParticipants,
    remoteToWatcherParticipants,
    handleChangeParticipantInput,
    handleChangeWatcherInput,
    handleClickTeamAddButton,
  };
};

export default useTeamAdd;
``;
