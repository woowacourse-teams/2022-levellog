export interface FeedbackType {
  id: number;
  updatedAt: string;
  from: {
    id: number;
    nickname: string;
    profileUrl: string;
  };
  feedback: {
    study: string;
    speak: string;
    etc: string;
  };
}

export interface FeedbackPostType {
  feedback: {
    study: string;
    speak: string;
    etc: string;
  };
}

export interface LevellogType {
  content: string;
}

export interface UserInfoType {
  id: string;
  profileUrl: string;
}

export type ImageSizeType = 'HUGE' | 'LARGE' | 'MEDIUM' | 'SMALL';

export interface InterviewTeamType {
  id: string;
  teamImage: string;
  hostId: string;
  title: string;
  place: string;
  startAt: string;
  participants: ParticipantType[];
}

export interface ParticipantType {
  id: string;
  levellogId: string;
  nickname: string;
  profileUrl: string;
}
