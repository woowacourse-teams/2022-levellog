import { useState, useEffect, useContext, useRef } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useUser from 'hooks/useUser';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import {
  requestGetTeam,
  requestPostTeam,
  requestDeleteTeam,
  requestEditTeam,
  requestCloseTeamInterview,
} from 'apis/teams';
import { NotCorrectToken } from 'apis/utils';
import { TeamContext, TeamDispatchContext } from 'contexts/teamContext';
import { MemberType } from 'types/member';
import { TeamApiType, TeamCustomHookType } from 'types/team';

const useTeam = () => {
  const { loginUserId, loginUserNickname, loginUserProfileUrl } = useUser();
  const { showSnackbar } = useSnackbar();

  const { teamId } = useParams();
  const navigate = useNavigate();

  const myInfo = { id: loginUserId, nickname: loginUserNickname, profileUrl: loginUserProfileUrl };
  const [participants, setParticipants] = useState<MemberType[]>([myInfo]);
  const [watchers, setWatchers] = useState<MemberType[]>([]);
  const [nicknameValue, setNicknameValue] = useState('');

  const teamInfoDispatch = useContext(TeamDispatchContext);
  const team = useContext(TeamContext);

  const accessToken = localStorage.getItem('accessToken');

  const postTeam = async ({ teamInfo }: Record<'teamInfo', TeamCustomHookType>) => {
    try {
      await requestPostTeam({ teamInfo, accessToken });
      showSnackbar({ message: MESSAGE.TEAM_CREATE });
      navigate(ROUTES_PATH.HOME);
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
          showSnackbar({ message: responseBody.data.message });
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
        if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const editTeam = async ({ teamInfo }: Record<'teamInfo', TeamCustomHookType>) => {
    try {
      if (typeof teamId !== 'string') throw Error;
      await requestEditTeam({ teamId, teamInfo, accessToken });
      showSnackbar({ message: MESSAGE.TEAM_EDIT });
      navigate(ROUTES_PATH.HOME);
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const deleteTeam = async ({ teamId }: Pick<TeamApiType, 'teamId'>) => {
    try {
      await requestDeleteTeam({ teamId, accessToken });
      showSnackbar({ message: MESSAGE.TEAM_DELETE });
    } catch (err: unknown) {
      if (axios.isAxiosError(err) && err instanceof Error) {
        const responseBody: AxiosResponse = err.response!;
        if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
          showSnackbar({ message: responseBody.data.message });
        }
      }
    }
  };

  const closeTeamInterview = async ({ teamId }: Pick<TeamApiType, 'teamId'>) => {
    try {
      await requestCloseTeamInterview({ teamId, accessToken });
      showSnackbar({ message: MESSAGE.INTERVIEW_CLOSE });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) {
          showSnackbar({ message: responseBody.data.message });
          navigate(ROUTES_PATH.HOME);
        }
      }
    }
  };

  const handleClickDeleteTeamButton = async () => {
    if (confirm(MESSAGE.TEAM_DELETE_CONFIRM) && typeof teamId === 'string') {
      await deleteTeam({ teamId });
      navigate(ROUTES_PATH.HOME);

      return;
    }
  };

  const handleClickCloseTeamInterviewButton = async () => {
    if (confirm(MESSAGE.INTERVIEW_CLOSE_CONFIRM) && typeof teamId === 'string') {
      await closeTeamInterview({ teamId });
      navigate(ROUTES_PATH.HOME);

      return;
    }
  };

  useEffect(() => {
    getTeam();
  }, []);

  return {
    nicknameValue,
    participants,
    watchers,
    team,
    setNicknameValue,
    setParticipants,
    setWatchers,
    getTeam,
    postTeam,
    editTeam,
    handleClickDeleteTeamButton,
    handleClickCloseTeamInterviewButton,
  };
};

export default useTeam;
