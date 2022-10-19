export interface UserType {
  id: string;
  nickname: string;
  profileUrl: string;
}

export interface ParticipantType {
  memberId: string;
  levellogId: string;
  preQuestionId: string;
  nickname: string;
  profileUrl: string;
}

export type ImageSizeType =
  | 'EXCEPTION'
  | 'EXTRA_HUGE'
  | 'HUGE'
  | 'LARGE'
  | 'MEDIUM'
  | 'SMALL'
  | 'TINY';
