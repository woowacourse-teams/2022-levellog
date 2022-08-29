import { useEffect, useRef, useState } from 'react';

import axios, { AxiosResponse } from 'axios';

import useTeam from 'hooks/useTeam';
import useUtil from 'hooks/useUtil';

import { MESSAGE } from 'constants/constants';
import {
  interviewDateValidate,
  interviewInterviewerValidate,
  interviewLocationValidate,
  interviewParticipantValidate,
  interviewTimeValidate,
  interviewTitleValidate,
} from 'constants/validate';

import { requestGetMembers } from 'apis/member';
import { MembersCustomHookType, MemberType } from 'types/member';
import { InterviewTeamType } from 'types/team';

const useTeamPage = () => {
  const { isDebounce } = useUtil();
  const { participants, watchers, setParticipants, setWatchers, getTeam, postTeam, editTeam } =
    useTeam();
  const [participantNicknameValue, setParticipantNicknameValue] = useState('');
  const [watcherNicknameValue, setWatcherNicknameValue] = useState('');
  const [participantMembers, setParticipantMembers] = useState<MemberType[]>([]);
  const [watcherMembers, setWatcherMembers] = useState<MemberType[]>([]);
  const [members, setMembers] = useState<MemberType[]>([]);
  const teamInfoRef = useRef<HTMLInputElement[]>([]);

  const accessToken = localStorage.getItem('accessToken');

  const updateMembers = async ({ nicknameValue = ' ' }: MembersCustomHookType) => {
    try {
      if (isDebounce()) return;

      const res = await requestGetMembers({ accessToken, nickname: nicknameValue });
      const membersFilter = res.data.members
        .filter((member) => participants.every((participant) => participant.id !== member.id))
        .filter((member) => watchers.every((watcher) => watcher.id !== member.id));
      setParticipantMembers(membersFilter);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
      }
    }
  };

  const updateWatchers = async ({ nicknameValue = ' ' }: MembersCustomHookType) => {
    try {
      if (isDebounce()) return;

      const res = await requestGetMembers({ accessToken, nickname: nicknameValue });
      const membersFilter = res.data.members
        .filter((member) => watchers.every((watcher) => watcher.id !== member.id))
        .filter((member) => participants.every((participant) => participant.id !== member.id));
      setWatcherMembers(membersFilter);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
      }
    }
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

  const getTeamOnRef = async () => {
    const team = await getTeam();
    if (team && Object.keys(team).length === 0) return;
    if (teamInfoRef.current[0] === null) return;

    teamInfoRef.current[0].value = (team as unknown as InterviewTeamType).title;
    teamInfoRef.current[1].value = (team as unknown as InterviewTeamType).place;
    teamInfoRef.current[2].value = (team as unknown as InterviewTeamType).startAt.slice(0, 10);
    teamInfoRef.current[3].value = (team as unknown as InterviewTeamType).startAt.slice(-8);
    teamInfoRef.current[4].value = String(
      (team as unknown as InterviewTeamType).interviewers.length,
    );
  };

  const validateTeamInfo = () => {
    const [title, place, date, time, interviewerNumber] = teamInfoRef.current;
    if (
      interviewTitleValidate({ value: title.value }) &&
      interviewLocationValidate({ value: place.value }) &&
      interviewDateValidate({ value: date.value }) &&
      interviewTimeValidate({ value: time.value }) &&
      interviewInterviewerValidate({ value: +interviewerNumber.value }) &&
      interviewParticipantValidate({ value: participants.length })
    ) {
      return true;
    }

    return false;
  };

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

  const handleChangeParticipantInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setParticipantNicknameValue(e.target.value);
  };

  const handleChangeWatcherInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setWatcherNicknameValue(e.target.value);
  };

  const handleClickTeamAddButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    if (validateTeamInfo()) {
      postTeam({ teamInfo: convertTeamInfoFormat() });

      return;
    }
    alert(MESSAGE.INTERVIEW_HOLE_VALUE_VALIDATE);
  };

  const handleClickTeamEditButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    if (validateTeamInfo()) {
      editTeam({ teamInfo: convertTeamInfoFormat() });

      return;
    }
    alert(MESSAGE.INTERVIEW_HOLE_VALUE_VALIDATE);
  };

  useEffect(() => {
    const membersFilter = members
      .filter((member) => watchers.every((watcher) => watcher.id !== member.id))
      .filter((member) => participants.every((participant) => participant.id !== member.id));
    setParticipantMembers(membersFilter);
    setWatcherMembers(membersFilter);
  }, [participants, watchers]);

  const init = async () => {
    const res = await requestGetMembers({ accessToken, nickname: '' });
    setMembers(res.data.members);
  };

  useEffect(() => {
    init();
  }, []);

  return {
    state: {
      participantNicknameValue,
      watcherNicknameValue,
      watcherMembers,
      participantMembers,
      watchers,
      participants,
    },
    ref: {
      teamInfoRef,
    },
    handle: {
      setParticipantNicknameValue,
      setWatcherNicknameValue,
      getTeamOnRef,
      updateMembers,
      updateWatchers,
      addToParticipants,
      addToWatcherParticipants,
      removeToParticipants,
      remoteToWatcherParticipants,
      handleChangeParticipantInput,
      handleChangeWatcherInput,
      handleClickTeamAddButton,
      handleClickTeamEditButton,
    },
  };
};

export default useTeamPage;
