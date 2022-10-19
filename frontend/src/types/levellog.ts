import { LevellogCustomHookType } from 'hooks/levellog/types';

import { ParticipantType } from 'types/team';

export interface LevellogType {
  content: string;
}

export interface LevellogInfoType extends LevellogType {
  author: {
    id: string;
    nickname: string;
    profileUrl: string;
  };
}

// 얜 뭐지?
export interface LevellogParticipantType extends Pick<LevellogCustomHookType, 'teamId'> {
  participant: ParticipantType;
}
