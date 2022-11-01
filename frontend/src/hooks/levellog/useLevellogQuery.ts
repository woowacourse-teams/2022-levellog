import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import { QUERY_KEY } from 'constants/constants';

import { requestGetLevellog } from 'apis/levellog';

const useLevellogQuery = () => {
  const { teamId, levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { isError: levellogError, data: levellogInfo } = useQuery(
    [QUERY_KEY.LEVELLOG, accessToken, teamId, levellogId],
    () =>
      requestGetLevellog({
        accessToken,
        teamId,
        levellogId,
      }),
    {
      cacheTime: 0,
    },
  );

  return { levellogError, levellogInfo };
};

export default useLevellogQuery;
