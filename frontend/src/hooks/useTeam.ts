import { useState, useEffect, useRef, useContext } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useUser from 'hooks/useUser';
import useUtil from 'hooks/useUtil';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import { requestGetMembers } from 'apis/member';
import {
  requestGetTeam,
  requestPostTeam,
  requestDeleteTeam,
  requestEditTeam,
  requestCloseTeamInterview,
} from 'apis/teams';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { TeamContext, TeamDispatchContext } from 'contexts/teamContext';
import { MembersCustomHookType, MemberType } from 'types/member';
import { InterviewTeamType, TeamApiType, TeamCustomHookType, TeamEditApiType } from 'types/team';

const useTeam = () => {
  const { loginUserId, loginUserNickname, loginUserProfileUrl } = useUser();
  const { isDebounce } = useUtil();
  const [members, setMembers] = useState<MemberType[]>([]);
  const [nicknameValue, setNicknameValue] = useState('');
  const team = useContext(TeamContext);
  const teamInfoDispatch = useContext(TeamDispatchContext);
  const location = useLocation() as { state: InterviewTeamType };
  const teamInfoRef = useRef<HTMLInputElement[]>([]);
  const navigate = useNavigate();
  const { teamId } = useParams();
  const teamLocationState: InterviewTeamType | undefined = location.state;
  const accessToken = localStorage.getItem('accessToken');
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
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
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
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
      }
    }
  };

  const editTeam = async ({ teamInfo }: Pick<TeamEditApiType, 'teamInfo'>) => {
    try {
      if (typeof teamId !== 'string') throw Error;
      await requestEditTeam({ teamId, teamInfo, accessToken });
      navigate(ROUTES_PATH.HOME);
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
      }
    }
  };

  const deleteTeam = async ({ teamId }: Pick<TeamApiType, 'teamId'>) => {
    try {
      await requestDeleteTeam({ teamId, accessToken });
      navigate(ROUTES_PATH.HOME);
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message })) {
          alert(responseBody.data.message);
        }
      }
    }
  };

  const closeTeamInterview = async ({ teamId }: Pick<TeamApiType, 'teamId'>) => {
    try {
      await requestCloseTeamInterview({ teamId, accessToken });
      navigate(ROUTES_PATH.HOME);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
  };

  const onSubmitTeamAddForm = ({ participants }: Record<'participants', MemberType[]>) => {
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
    postTeam({ teamInfo });
  };

  const handleSubmitTeamEditForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const [title, place, date, time, interviewerNumber] = teamInfoRef.current;
    const teamInfo = {
      title: title.value,
      place: place.value,
      startAt: `${date.value}T${time.value}`,
      interviewerNumber: interviewerNumber.value,
      participants: {
        ids: Object.values(participants)
          .filter((participants) => participants.id !== loginUserId)
          .map((participants) => participants.id),
      },
    };
    editTeam({ teamInfo });
  };

  const onClickDeleteTeamButton = ({ teamId }: Pick<TeamApiType, 'teamId'>) => {
    if (confirm(MESSAGE.TEAM_DELETE_CONFIRM)) {
      deleteTeam({ teamId });
      navigate(ROUTES_PATH.HOME);

      return;
    }
  };

  const onClickCloseTeamInterviewButton = ({ teamId }: Pick<TeamApiType, 'teamId'>) => {
    if (confirm(MESSAGE.INTERVIEW_CLOSE_CONFIRM)) {
      closeTeamInterview({ teamId });
      navigate(ROUTES_PATH.HOME);

      return;
    }
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

  const updateMembers = async ({ nicknameValue = '' }: MembersCustomHookType) => {
    try {
      if (isDebounce()) return;

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
