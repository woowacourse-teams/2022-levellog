import { useContext, useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';

import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import { requestEditTeam, requestGetTeam } from 'apis/teams';
import { MemberContext } from 'contexts/memberContext';
import { TeamCustomHookType } from 'types/team';
import {
  interviewDateValidate,
  interviewInterviewerValidate,
  interviewLocationValidate,
  interviewParticipantValidate,
  interviewTimeValidate,
  interviewTitleValidate,
} from 'utils/validate';

const useTeamEdit = () => {
  const { showSnackbar } = useSnackbar();
  const participantAndWatcher = useContext(MemberContext);
  const teamInfoRef = useRef<HTMLInputElement[]>([]);
  const { teamId } = useParams();
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const { mutate: getTeam } = useMutation(
    () => {
      return requestGetTeam({ accessToken, teamId });
    },
    {
      onSuccess: (res) => {
        teamInfoRef.current[0].value = res.title;
        teamInfoRef.current[1].value = res.place;
        teamInfoRef.current[2].value = res.startAt.slice(0, 10);
        teamInfoRef.current[3].value = res.startAt.slice(-8);
        teamInfoRef.current[4].value = String(res.interviewers.length);
      },
      onError: (err) => {
        errorHandler({ err, showSnackbar });
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
        errorHandler({ err, showSnackbar });
      },
    },
  );

  const validateTeamInfo = () => {
    const [title, place, date, time, interviewerNumber] = teamInfoRef.current;
    if (
      interviewTitleValidate({ value: title.value }) &&
      interviewLocationValidate({ value: place.value }) &&
      interviewDateValidate({ value: date.value }) &&
      interviewTimeValidate({ value: time.value }) &&
      interviewInterviewerValidate({ value: +interviewerNumber.value }) &&
      interviewParticipantValidate({ value: participantAndWatcher.participants.length })
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
      watcherIds: Object.values(participantAndWatcher.watchers).map((watcher) => watcher.id),
      participantIds: Object.values(participantAndWatcher.participants).map(
        (participant) => participant.id,
      ),
    };

    return teamInfo;
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
    getTeam();
  }, []);

  return {
    teamInfoRef,
    handleClickTeamEditButton,
  };
};

export default useTeamEdit;
