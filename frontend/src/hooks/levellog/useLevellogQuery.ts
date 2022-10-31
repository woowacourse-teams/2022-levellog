import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from 'constants/constants';

import { requestGetLevellog } from 'apis/levellog';

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
