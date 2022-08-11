import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import { MESSAGE, REQUIRE_AUTH } from 'constants/constants';

import useTeam from './useTeam';
import useUser from './useUser';

const useAuth = ({ requireAuth }: AuthCustomHookProps) => {
  const { loginUserId } = useUser();
  const { team, getTeam } = useTeam();
  const { levellogId } = useParams();
  const [isLoad, setIsLoad] = useState(true);
  const [isError, setIsError] = useState(false);

  const checkAuth = async () => {
    const idsAndLevellogIds = Object.values(team.participants).map((participant) => [
      participant.memberId,
      participant.levellogId,
    ]);
    await getTeam();

    if (requireAuth === (REQUIRE_AUTH.IN_TEAM || REQUIRE_AUTH.ME || REQUIRE_AUTH.NOT_ME)) {
      if (
        team &&
        Object.values(team.participants)
          .map((participant) => participant.memberId)
          .every((memberId) => {
            return String(memberId) !== String(loginUserId);
          })
      ) {
        await setIsLoad(false);
        await setIsError(true);
        alert(MESSAGE.NEED_IN_TEAM);

        return;
      }
    }

    if (requireAuth === REQUIRE_AUTH.HOST) {
      if (String(team.hostId) !== String(loginUserId)) {
        await setIsLoad(false);
        await setIsError(true);
        alert(MESSAGE.NEED_HOST);

        return;
      }
    }

    if (requireAuth === REQUIRE_AUTH.ME) {
      if (
        !idsAndLevellogIds.some(
          (idAndLevellogId) =>
            String(levellogId) === String(idAndLevellogId[1]) &&
            String(loginUserId) === String(idAndLevellogId[0]),
        )
      ) {
        await setIsLoad(false);
        await setIsError(true);
        alert(MESSAGE.WRONG_ACCESS);

        return;
      }
    }

    if (requireAuth === REQUIRE_AUTH.NOT_ME) {
      if (
        idsAndLevellogIds.some(
          (idAndLevellogId) =>
            String(levellogId) === String(idAndLevellogId[1]) &&
            String(loginUserId) === String(idAndLevellogId[0]),
        )
      ) {
        await setIsLoad(false);
        await setIsError(true);
        alert(MESSAGE.WRONG_ACCESS);

        return;
      }
    }
    await setIsLoad(false);
  };

  useEffect(() => {
    checkAuth();
  }, []);

  return [isLoad, isError];
};

interface AuthCustomHookProps {
  requireAuth: string;
}

export default useAuth;
