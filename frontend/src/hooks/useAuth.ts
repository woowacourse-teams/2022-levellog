import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import { MESSAGE, REQUIRE_AUTH } from 'constants/constants';

import useTeam from './useTeam';
import useUser from './useUser';
import { requestGetUserAuthority } from 'apis/login';

const useAuth = ({ requireAuth }: AuthCustomHookProps) => {
  const { getTeam } = useTeam();
  const { levellogId } = useParams();
  const [isLoad, setIsLoad] = useState(true);
  const [isError, setIsError] = useState(false);
  const accessToken = localStorage.getItem('accessToken');

  const checkAuth = async () => {
    const team = await getTeam();

    if (!accessToken) {
      alert(MESSAGE.NEED_LOGIN);
      return;
    }

    if (!team) {
      await setIsLoad(false);
      await setIsError(true);
      alert(MESSAGE.WRONG_ACCESS);

      return;
    }

    const res = await requestGetUserAuthority({ accessToken });
    const loginUserId = res.data.id;
    const idsAndLevellogIds = Object.values(team.participants).map((participant) => [
      participant.memberId,
      participant.levellogId,
    ]);
    console.log('진입: checkAuth');

    if (requireAuth === (REQUIRE_AUTH.IN_TEAM || REQUIRE_AUTH.ME || REQUIRE_AUTH.NOT_ME)) {
      console.log('진입: checkInTeam');
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
      console.log('진입: checkHost');
      if (String(team.hostId) !== String(loginUserId)) {
        await setIsLoad(false);
        await setIsError(true);
        alert(MESSAGE.NEED_HOST);

        return;
      }
    }

    if (requireAuth === REQUIRE_AUTH.ME) {
      console.log('진입: checkMe');
      if (
        !idsAndLevellogIds.some(
          (idAndLevellogId) =>
            String(levellogId) === String(idAndLevellogId[1]) &&
            String(loginUserId) === String(idAndLevellogId[0]),
        )
      ) {
        await setIsLoad(false);
        await setIsError(true);
        // alert(MESSAGE.WRONG_ACCESS);
        alert('본인이 작성하지 않은 글을 수정할 수 없습니다');
        return;
      }
    }

    if (requireAuth === REQUIRE_AUTH.NOT_ME) {
      console.log('진입: checkNotMe');
      if (
        idsAndLevellogIds.some(
          (idAndLevellogId) =>
            String(levellogId) === String(idAndLevellogId[1]) &&
            String(loginUserId) === String(idAndLevellogId[0]),
        )
      ) {
        await setIsLoad(false);
        await setIsError(true);
        // alert(MESSAGE.WRONG_ACCESS);
        alert('본인이 본인에 대한 질문,피드백을 작성,수정할 수 없습니다.');
        return;
      }
    }
    console.log('에러 없이 탈출');
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
