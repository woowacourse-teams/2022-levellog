import { ParticipantType } from 'types/team';

export interface LevellogCustomHookType {
  inputValue: string;
  teamId: string;
  levellogId: string;
}

export interface LevellogApiType {
  accessToken: string | null;
  teamId: string | undefined;
  levellogId: string | undefined;
  levellogContent: LevellogFormatType;
}

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
