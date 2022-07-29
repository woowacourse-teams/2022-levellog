import { ParticipantType } from './team';

export interface LevellogCustomHookType {
  inputValue: string;
  teamId: string;
  levellogId: string;
}

export interface LevellogApiType {
  accessToken: string | null;
  teamId: string;
  levellogId: string;
  levellogContent: LevellogFormatType;
}

export interface LevellogFormatType {
  content: string;
}

export interface LevellogParticipantType extends Pick<LevellogCustomHookType, 'teamId'> {
  participant: ParticipantType;
}
