import { LevellogCustomHookType } from 'hooks/levellog/types';

import { ParticipantType } from 'types/team';

export interface LevellogFormatType {
  content: string;
}

export interface LevellogParticipantType extends Pick<LevellogCustomHookType, 'teamId'> {
  participant: ParticipantType;
}

export interface LevellogInfoType {
  author: {
    id: string;
    nickname: string;
    profileUrl: string;
  };
  content: string;
}
