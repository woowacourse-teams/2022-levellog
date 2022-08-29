import { useState, useEffect, useContext } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import {
  requestGetTeam,
  requestPostTeam,
  requestDeleteTeam,
  requestEditTeam,
  requestCloseTeamInterview,
} from 'apis/teams';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';
import { TeamContext, TeamDispatchContext } from 'contexts/teamContext';
import { MemberType } from 'types/member';
import { InterviewTeamType, TeamApiType, TeamCustomHookType } from 'types/team';

const useTeam = () => {
  const team = useContext(TeamContext);
  const teamInfoDispatch = useContext(TeamDispatchContext);
  const [participants, setParticipants] = useState<MemberType[]>([]);
  const [watchers, setWatchers] = useState<MemberType[]>([]);
  const location = useLocation() as { state: InterviewTeamType };
  const navigate = useNavigate();
  const { teamId } = useParams();

  const teamLocationState: InterviewTeamType | undefined = location.state;
  const accessToken = localStorage.getItem('accessToken');

  const postTeam = async ({ teamInfo }: Record<'teamInfo', TeamCustomHookType>) => {
    try {
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
        setWatchers(
          res.data.watchers.map((watcher) => {
            return {
              id: watcher.memberId,
              nickname: watcher.nickname,
              profileUrl: watcher.profileUrl,
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

  const editTeam = async ({ teamInfo }: Record<'teamInfo', TeamCustomHookType>) => {
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
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
        navigate(ROUTES_PATH.HOME);
      }
    }
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

  useEffect(() => {
    if (teamLocationState && (teamLocationState as InterviewTeamType).id !== undefined) {
      teamInfoDispatch(teamLocationState);
    }
  }, []);

  return {
    participants,
    watchers,
    team,
    teamLocationState,
    setParticipants,
    setWatchers,
    getTeam,
    postTeam,
    editTeam,
    onClickDeleteTeamButton,
    onClickCloseTeamInterviewButton,
  };
};

export default useTeam;
