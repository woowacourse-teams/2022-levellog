import { useState, useContext } from 'react';
import { useParams } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';
import { UserType } from 'types';

import { useMutation, useQuery } from '@tanstack/react-query';

import useUser from 'hooks/useUser';
import useSnackbar from 'hooks/utils/useSnackbar';

import { requestGetMembers } from 'apis/member';
import { requestGetTeam } from 'apis/teams';
import { WrongAccessToken } from 'apis/utils';
import { TeamContext, TeamDispatchContext } from 'contexts/teamContext';

const QUERY_KEY = {
  TEAM: 'team',
  MEMBERS: 'members',
};

const useTeam = () => {
  const { loginUserId, loginUserNickname, loginUserProfileUrl } = useUser();
  const { showSnackbar } = useSnackbar();
  const { teamId } = useParams();

  const myInfo = { id: loginUserId, nickname: loginUserNickname, profileUrl: loginUserProfileUrl };

  const [participants, setParticipants] = useState<UserType[]>([myInfo]);
  const [watchers, setWatchers] = useState<UserType[]>([]);
  const [nicknameValue, setNicknameValue] = useState('');

  const teamInfoDispatch = useContext(TeamDispatchContext);
  const team = useContext(TeamContext);

  const accessToken = localStorage.getItem('accessToken');

  useQuery([QUERY_KEY.MEMBERS], () => {
    return requestGetMembers({ accessToken, nickname: '' });
  });

  const { data: teamInfo, mutate: getTeam } = useMutation(
    () => {
      return requestGetTeam({ accessToken, teamId });
    },
    {
      onSuccess: (res) => {
        teamInfoDispatch(res);
        setParticipants(
          res.participants.map((participant) => {
            return {
              id: participant.memberId,
              nickname: participant.nickname,
              profileUrl: participant.profileUrl,
            };
          }),
        );
        setWatchers(
          res.watchers.map((watcher) => {
            return {
              id: watcher.memberId,
              nickname: watcher.nickname,
              profileUrl: watcher.profileUrl,
            };
          }),
        );
      },
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (WrongAccessToken({ message: responseBody.data.message, showSnackbar })) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      },
    },
  );

  return {
    nicknameValue,
    participants,
    watchers,
    team,
    teamInfo,
    setNicknameValue,
    setParticipants,
    setWatchers,
    getTeam,
  };
};

export default useTeam;
