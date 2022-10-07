import { useParams } from 'react-router-dom';

import { useQuery } from '@tanstack/react-query';

import { requestGetLevellog } from 'apis/levellog';
import { requestGetLoginUserRole } from 'apis/role';

const QUERY_KEY = {
  ROLE: 'role',
  LEVELLOG: 'levellog',
};

const useRole = () => {
  const { teamId, levellogId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { data: levellogInfo } = useQuery(
    [QUERY_KEY.LEVELLOG, accessToken, teamId, levellogId],
    () =>
      requestGetLevellog({
        accessToken,
        teamId,
        levellogId,
      }),
  );

  const author = levellogInfo?.author;

  const { data: feedbackWriterRole } = useQuery(
    [QUERY_KEY.ROLE, accessToken, teamId, author],
    () => {
      return requestGetLoginUserRole({
        accessToken,
        teamId,
        participantId: author?.id,
      });
    },
    {
      enabled: !!author,
    },
  );

  return {
    authorInfo: levellogInfo?.author,
    feedbackWriterRole: feedbackWriterRole?.myRole,
  };
};

export default useRole;
