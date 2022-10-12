import { useRef, useContext } from 'react';
import { useNavigate } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';

import errorHandler from 'hooks/utils/errorHandler';
import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import { requestPostTeam } from 'apis/teams';
import { MemberContext } from 'contexts/memberContext';
import { TeamCustomHookType } from 'types/team';
import {
  interviewDateValidate,
  interviewInterviewerValidate,
  interviewLocationValidate,
  interviewTimeValidate,
  interviewTitleValidate,
  interviewParticipantValidate,
} from 'utils/validate';

const useTeamAdd = () => {
  const { showSnackbar } = useSnackbar();
  const participantAndWatcher = useContext(MemberContext);
  const teamInfoRef = useRef<HTMLInputElement[]>([]);
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const { mutate: postTeam } = useMutation(
    ({ teamInfo }: Record<'teamInfo', TeamCustomHookType>) => {
      return requestPostTeam({
        teamInfo,
        accessToken,
      });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.TEAM_CREATE });
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

  const handleClickTeamAddButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    if (validateTeamInfo()) {
      postTeam({ teamInfo: convertTeamInfoFormat() });

      return;
    }
    showSnackbar({ message: MESSAGE.INTERVIEW_HOLE_VALUE_VALIDATE });
  };

  return {
    teamInfoRef,
    handleClickTeamAddButton,
  };
};

export default useTeamAdd;
``;
