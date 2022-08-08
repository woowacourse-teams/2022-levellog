import { useState } from 'react';

import { MemberType } from 'types/member';
import { ParticipantType } from 'types/team';

const useUtil = () => {
  const [throttle, setThrottle] = useState<boolean>(false);

  const isThrottle = () => {
    if (!throttle) {
      setThrottle(true);
      setTimeout(() => {
        setThrottle(false);
      }, 100);
      return false;
    }
    return true;
  };

  // 코드 고쳐서 써야함
  const participantsToSmallType = (participants: ParticipantType[]): MemberType[] => {
    const smallParticipants = participants.map((participant) => {
      return {
        id: participant.memberId,
        nickname: participant.nickname,
        profileUrl: participant.profileUrl,
      };
    });
    return smallParticipants;
  };

  return { isThrottle, participantsToSmallType };
};

export default useUtil;
