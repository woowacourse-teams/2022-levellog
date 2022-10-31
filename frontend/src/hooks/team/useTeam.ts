import { useState, useContext, useEffect } from 'react';
import { useParams } from 'react-router-dom';

import { UserType } from 'types';

import { useMutation } from '@tanstack/react-query';

import useUser from 'hooks/useUser';
import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { requestGetTeam } from 'apis/teams';
import { TeamContext, TeamDispatchContext } from 'contexts/teamContext';

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
        errorHandler({ err, showSnackbar });
      },
    },
  );

  useEffect(() => {
    getTeam();
  }, []);

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
