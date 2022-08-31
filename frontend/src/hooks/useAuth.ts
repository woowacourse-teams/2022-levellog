import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import useSnackbar from 'hooks/useSnackbar';
import useTeam from 'hooks/useTeam';

import { MESSAGE, REQUIRE_AUTH } from 'constants/constants';

import { requestGetUserAuthority } from 'apis/login';

const useAuth = ({ requireAuth }: AuthCustomHookProps) => {
  const { getTeam } = useTeam();
  const { showSnackbar } = useSnackbar();
  const { levellogId, authorId } = useParams();
  const [isLoad, setIsLoad] = useState(true);
  const [isError, setIsError] = useState(false);

  const accessToken = localStorage.getItem('accessToken');

  const checkAuth = async () => {
    const team = await getTeam();

    if (!accessToken) {
      showSnackbar({ message: MESSAGE.NEED_LOGIN });
      return;
    }

    if (!team) {
      setIsLoad(false);
      setIsError(true);
      showSnackbar({ message: MESSAGE.WRONG_ACCESS });

      return;
    }

    const res = await requestGetUserAuthority({ accessToken });
    const loginUserId = res.data.id;
    const idsAndLevellogIds = Object.values(team.participants).map((participant) => [
      participant.memberId,
      participant.levellogId,
    ]);

    if (requireAuth === (REQUIRE_AUTH.IN_TEAM || REQUIRE_AUTH.NOT_ME)) {
      if (
        team &&
        Object.values(team.participants)
          .map((participant) => participant.memberId)
          .every((memberId) => {
            return String(memberId) !== String(loginUserId);
          }) &&
        Object.values(team.watchers)
          .map((watcher) => watcher.memberId)
          .every((memberId) => {
            return String(memberId) !== String(loginUserId);
          })
      ) {
        setIsLoad(false);
        setIsError(true);
        showSnackbar({ message: MESSAGE.NEED_IN_TEAM });

        return;
      }
    }

    if (requireAuth === REQUIRE_AUTH.HOST) {
      if (String(team.hostId) !== String(loginUserId)) {
        setIsLoad(false);
        setIsError(true);
        showSnackbar({ message: MESSAGE.NEED_HOST });

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
        setIsLoad(false);
        setIsError(true);
        showSnackbar({ message: MESSAGE.NEED_NOT_ME });

        return;
      }
    }

    if (requireAuth === REQUIRE_AUTH.AUTHOR) {
      if (String(loginUserId) !== authorId) {
        setIsLoad(false);
        setIsError(true);
        showSnackbar({ message: MESSAGE.NEED_AUTHOR });
      }
    }
    setIsLoad(false);
  };

  useEffect(() => {
    checkAuth();
  }, []);

  return { isLoad, isError };
};

interface AuthCustomHookProps {
  requireAuth: string;
}

export default useAuth;
