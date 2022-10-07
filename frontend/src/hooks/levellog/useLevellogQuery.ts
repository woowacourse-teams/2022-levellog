// Query 폴더로 분리하는 것이 좋다.
import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import { requestGetLevellog } from 'apis/levellog';

const QUERY_KEY = {
  LEVELLOG: 'levellog',
};

const useLevellogQuery = () => {
  const { teamId, levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const {
    refetch: levellogRefetch,
    isError: levellogError,
    isSuccess: levellogSuccess,
    data: levellogInfo,
  } = useQuery([QUERY_KEY.LEVELLOG, accessToken, teamId, levellogId], () =>
    requestGetLevellog({
      accessToken,
      teamId,
      levellogId,
    }),
  );

  return { levellogRefetch, levellogError, levellogSuccess, levellogInfo };
};

export default useLevellogQuery;
