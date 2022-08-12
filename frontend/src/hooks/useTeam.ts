import React, { useState, useEffect, useRef, useContext } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import useUser from './useUser';
import useUtil from './useUtil';
import { requestGetMembers } from 'apis/member';
import {
  requestGetTeam,
  requestPostTeam,
  requestDeleteTeam,
  requestEditTeam,
  requestCloseTeamInterview,
} from 'apis/teams';
import Member from 'components/teams/Member';
import { TeamContext, TeamDispatchContext } from 'contexts/teamContext';
import { MembersCustomHookType, MemberType } from 'types/member';
import { InterviewTeamType, TeamApiType, TeamCustomHookType, TeamEditApiType } from 'types/team';

const useTeam = () => {
  const { loginUserId, loginUserNickname, loginUserProfileUrl } = useUser();
  const [members, setMembers] = useState<MemberType[]>([]);
  const [nicknameValue, setNicknameValue] = useState('');
  const team = useContext(TeamContext);
  const teamInfoDispatch = useContext(TeamDispatchContext);

  const location = useLocation() as { state: InterviewTeamType };
  const teamInfoRef = useRef<HTMLInputElement[]>([]);
  const { teamId } = useParams();
  const navigate = useNavigate();
  const { isThrottle } = useUtil();
  const teamLocationState: InterviewTeamType | undefined = location.state;
  const accessToken = localStorage.getItem('accessToken');
  // participants는 인터뷰 생성 폼, 인터뷰 수정 폼에서만 사용해야함!
  const [participants, setParticipants] = useState<MemberType[]>([
    { id: loginUserId, nickname: loginUserNickname, profileUrl: loginUserProfileUrl },
  ]);

  const postTeam = async ({ teamInfo }: Record<'teamInfo', TeamCustomHookType>) => {
    try {
      teamInfo.participants.ids = teamInfo.participants.ids.filter((id) => id !== loginUserId);
      await requestPostTeam({ teamInfo, accessToken });
      alert(MESSAGE.TEAM_CREATE);
      navigate(ROUTES_PATH.HOME);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
      }
    }
  };

  const getTeam = async () => {
    try {
      if (typeof teamId === 'string') {
        const res = await requestGetTeam({ teamId, accessToken });
        teamInfoDispatch(res.data);
        setParticipants(
          res.data.participants.map((participant) => {
            return {
              id: participant.memberId,
              nickname: participant.nickname,
              profileUrl: participant.profileUrl,
            };
          }),
        );
        return res.data;
      }
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const editTeam = async ({ teamInfo }: Pick<TeamEditApiType, 'teamInfo'>) => {
    try {
      if (typeof teamId !== 'string') throw Error;
      await requestEditTeam({ teamId, teamInfo, accessToken });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
      }
    }
  };

  const deleteTeam = async ({ teamId }: Pick<TeamApiType, 'teamId'>) => {
    try {
      await requestDeleteTeam({ teamId, accessToken });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const closeTeamInterview = async ({ teamId }: Pick<TeamApiType, 'teamId'>) => {
    try {
      await requestCloseTeamInterview({ teamId, accessToken });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const onSubmitTeamAddForm = async ({ participants }: Record<'participants', MemberType[]>) => {
    const [title, place, date, time, interviewerNumber] = teamInfoRef.current;
    const teamInfo = {
      title: title.value,
      place: place.value,
      startAt: `${date.value}T${time.value}`,
      interviewerNumber: interviewerNumber.value,
      participants: {
        ids: Object.values(participants).map((participants) => participants.id),
      },
    };
    await postTeam({ teamInfo });
  };

  const handleSubmitTeamEditForm = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const [title, place, date, time, interviewerNumber] = teamInfoRef.current;
    const teamInfo = {
      title: title.value,
      place: place.value,
      startAt: `${date.value}T${time.value}`,
      interviewerNumber: interviewerNumber.value,
      participants: {
        ids: Object.values(participants).map((participants) => participants.id),
      },
    };
    await editTeam({ teamInfo });
    navigate(ROUTES_PATH.HOME);
  };

  const onClickDeleteTeamButton = async ({ teamId }: Pick<TeamApiType, 'teamId'>) => {
    if (confirm(MESSAGE.TEAM_DELETE_CONFIRM)) {
      await deleteTeam({ teamId });
      navigate(ROUTES_PATH.HOME);

      return;
    }
  };

  const onClickCloseTeamInterviewButton = async ({ teamId }: Pick<TeamApiType, 'teamId'>) => {
    if (confirm(MESSAGE.INTERVIEW_CLOSE_CONFIRM)) {
      await closeTeamInterview({ teamId });
      navigate(ROUTES_PATH.HOME);

      return;
    }
  };

  const getTeamOnRef = async () => {
    const team = await getTeam();
    if (team && Object.keys(team).length === 0) {
      return;
    }
    if (teamInfoRef.current[0] === null) {
      return;
    }
    teamInfoRef.current[0].value = (team as unknown as InterviewTeamType).title;
    teamInfoRef.current[1].value = (team as unknown as InterviewTeamType).place;
    teamInfoRef.current[2].value = (team as unknown as InterviewTeamType).startAt.slice(0, 10);
    teamInfoRef.current[3].value = (team as unknown as InterviewTeamType).startAt.slice(-8);
    teamInfoRef.current[4].value = (team as unknown as InterviewTeamType).interviewerNumber;
  };

  const updateMembers = async ({ nicknameValue = '' }: MembersCustomHookType) => {
    try {
      if (isThrottle()) return;
      const res = await requestGetMembers({ accessToken, nickname: nicknameValue });
      const members = res.data.members.filter((member) =>
        participants.every((participant) => participant.id !== member.id),
      );

      setMembers(members);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
      }
    }
  };

  const addToParticipants = ({ id, nickname, profileUrl }: MemberType) => {
    // 비동기로 불러오는 동안 두 번 클릭하면 두 번 들어가는 버그 때문에 일단 분기로 체크해야함
    if (participants.every((participant) => id !== participant.id)) {
      setParticipants((prev) => prev.concat({ id, nickname, profileUrl }));
    }
  };

  const removeToParticipants = ({ id }: Pick<MemberType, 'id'>) => {
    setParticipants(participants.filter((participant) => id !== participant.id));
  };

  useEffect(() => {
    if (teamLocationState && (teamLocationState as InterviewTeamType).id !== undefined) {
      teamInfoDispatch(teamLocationState);
    }
  }, []);

  return {
    team,
    teamLocationState,
    getTeam,
    members,
    participants,
    nicknameValue,
    setNicknameValue,
    teamInfoRef,
    getTeamOnRef,
    updateMembers,
    addToParticipants,
    removeToParticipants,
    onSubmitTeamAddForm,
    handleSubmitTeamEditForm,
    onClickDeleteTeamButton,
    onClickCloseTeamInterviewButton,
  };
};

export default useTeam;

// request나, 반환이 없는 await 제거하고 테스트
