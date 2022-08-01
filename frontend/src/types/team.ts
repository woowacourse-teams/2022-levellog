export interface TeamApiType {
  teamId: string;
}

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
  memberId: string;
  levellogId: string;
  nickname: string;
  profileUrl: string;
}
