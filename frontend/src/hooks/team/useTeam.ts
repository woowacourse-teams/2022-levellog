import { useState, useEffect, useContext, useRef } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { useMutation, useQuery } from '@tanstack/react-query';

import useUser from 'hooks/useUser';
import useSnackbar from 'hooks/utils/useSnackbar';

import { requestGetMembers } from 'apis/member';
import { requestGetTeam } from 'apis/teams';
import { NotCorrectToken } from 'apis/utils';
import { TeamContext, TeamDispatchContext } from 'contexts/teamContext';
import { MemberType } from 'types/member';

const QUERY_KEY = {
  TEAM: 'team',
  MEMBERS: 'members',
};

const useTeam = () => {
  const { loginUserId, loginUserNickname, loginUserProfileUrl } = useUser();
  const { showSnackbar } = useSnackbar();
  const { teamId } = useParams();
  const navigate = useNavigate();

  const myInfo = { id: loginUserId, nickname: loginUserNickname, profileUrl: loginUserProfileUrl };

  const [participants, setParticipants] = useState<MemberType[]>([myInfo]);
  const [watchers, setWatchers] = useState<MemberType[]>([]);
  const [nicknameValue, setNicknameValue] = useState('');
  const [members, setMembers] = useState<MemberType[]>([]);

  const teamInfoDispatch = useContext(TeamDispatchContext);
  const team = useContext(TeamContext);

  const accessToken = localStorage.getItem('accessToken');

  useQuery(
    [QUERY_KEY.MEMBERS],
    () => {
      return requestGetMembers({ accessToken, nickname: '' });
    },
    {
      onSuccess: (res) => {
        // setMembers(res.data.members);
      },
    },
  );

  const { data: teamInfo, mutate: getTeam } = useMutation(
    () => {
      return requestGetTeam({ accessToken, teamId });
    },
    {
      onSuccess: (res) => {
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
      },
      onError: (err) => {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (NotCorrectToken({ message: responseBody.data.message, showSnackbar })) {
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
    members,
    setNicknameValue,
    setParticipants,
    setWatchers,
    getTeam,
  };
};

export default useTeam;
