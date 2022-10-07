import { useEffect, useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { useMutation } from '@tanstack/react-query';

import useTeam from 'hooks/team/useTeam';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import { requestGetMembers } from 'apis/member';
import { requestEditTeam } from 'apis/teams';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { MembersCustomHookType, MemberType } from 'types/member';
import { InterviewTeamDetailType, TeamCustomHookType } from 'types/team';
import {
  interviewDateValidate,
  interviewInterviewerValidate,
  interviewLocationValidate,
  interviewParticipantValidate,
  interviewTimeValidate,
  interviewTitleValidate,
} from 'utils/validate';

const useTeamEdit = () => {
  const { participants, watchers, members, teamInfo, setParticipants, setWatchers } = useTeam();
  const { showSnackbar } = useSnackbar();
  const teamInfoRef = useRef<HTMLInputElement[]>([]);

  const [participantNicknameValue, setParticipantNicknameValue] = useState('');
  const [watcherNicknameValue, setWatcherNicknameValue] = useState('');
  const [participantMembers, setParticipantMembers] = useState<MemberType[]>([]);
  const [watcherMembers, setWatcherMembers] = useState<MemberType[]>([]);
  const { teamId } = useParams();
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

  const getTeamOnRef = async () => {
    if (teamInfo && Object.keys(teamInfo).length === 0) return;
    if (teamInfoRef.current[0] === null) return;

    teamInfoRef.current[0].value = (teamInfo as unknown as InterviewTeamDetailType).title;
    teamInfoRef.current[1].value = (teamInfo as unknown as InterviewTeamDetailType).place;
    teamInfoRef.current[2].value = (teamInfo as unknown as InterviewTeamDetailType).startAt.slice(
      0,
      10,
    );
    teamInfoRef.current[3].value = (teamInfo as unknown as InterviewTeamDetailType).startAt.slice(
      -8,
    );
    teamInfoRef.current[4].value = String(
      (teamInfo as unknown as InterviewTeamDetailType).interviewers.length,
    );
  };

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

  const { mutate: editTeam } = useMutation(
    ({ teamInfo }: Record<'teamInfo', TeamCustomHookType>) => {
      return requestEditTeam({
        teamId,
        teamInfo,
        accessToken,
      });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.TEAM_EDIT });
        navigate(ROUTES_PATH.HOME);
      },
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (
            토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
          ) {
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

  const handleChangeParticipantInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setParticipantNicknameValue(e.target.value);
  };

  const handleChangeWatcherInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setWatcherNicknameValue(e.target.value);
  };

  const handleClickTeamEditButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    if (validateTeamInfo()) {
      editTeam({ teamInfo: convertTeamInfoFormat() });

      return;
    }
    showSnackbar({ message: MESSAGE.INTERVIEW_HOLE_VALUE_VALIDATE });
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
    handleClickTeamEditButton,
  };
};

export default useTeamEdit;
