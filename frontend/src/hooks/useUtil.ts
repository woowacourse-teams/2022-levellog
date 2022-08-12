import { useState } from 'react';

import { MemberType } from 'types/member';
import { ParticipantType } from 'types/team';

const useUtil = () => {
  const [debounce, setDebounce] = useState<boolean>(false);

  const isDebounce = () => {
    if (debounce) {
      setDebounce(false);
      setTimeout(() => {
        setDebounce(true);
      }, 100);
      return true;
    }
    return false;
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

  return { isDebounce, participantsToSmallType };
};

export default useUtil;
