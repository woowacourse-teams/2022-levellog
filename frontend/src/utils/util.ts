import { Team } from 'types/team';
import { UriCustomHookType } from 'types/uri';

export const checkFirstWordFinalConsonant = ({ word }: CheckFirstWordFinalConsonantType) => {
  if (typeof word !== 'string') return;

  let lastLetter = word[word.length - 1];
  let uniCode = lastLetter.charCodeAt(0);

  if (uniCode < 44032 || uniCode > 55203) return;

  return (uniCode - 44032) % 28 != 0;
};

export const feedbackAddUriBuilder = ({
  teamId,
  levellogId,
}: Pick<UriCustomHookType, 'teamId' | 'levellogId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/feedbacks/add`;
};

export const feedbackEditUriBuilder = ({
  teamId,
  levellogId,
  feedbackId,
  authorId,
}: Omit<UriCustomHookType, 'preQuestionId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/feedbacks/${feedbackId}/author/${authorId}/edit`;
};

export const feedbacksGetUriBuilder = ({
  teamId,
  levellogId,
}: Pick<UriCustomHookType, 'teamId' | 'levellogId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/feedbacks`;
};

export const feedbackGetUriBuilder = ({
  teamId,
  levellogId,
  feedbackId,
}: Pick<UriCustomHookType, 'teamId' | 'levellogId' | 'feedbackId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/feedbacks/${feedbackId}`;
};

export const levellogAddUriBuilder = ({ teamId }: Pick<UriCustomHookType, 'teamId'>) => {
  return `/teams/${teamId}/levellogs/add`;
};

export const levellogEditUriBuilder = ({
  teamId,
  levellogId,
  authorId,
}: Pick<UriCustomHookType, 'teamId' | 'levellogId' | 'authorId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/author/${authorId}/edit`;
};

export const teamEditUriBuilder = ({ teamId }: Pick<UriCustomHookType, 'teamId'>) => {
  return `/teams/${teamId}/edit`;
};

export const teamGetUriBuilder = ({ teamId }: Pick<UriCustomHookType, 'teamId'>) => {
  return `/teams/${teamId}`;
};

export const preQuestionAddUriBuilder = ({
  teamId,
  levellogId,
}: Pick<UriCustomHookType, 'teamId' | 'levellogId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/pre-questions/add`;
};

export const preQuestionEditUriBuilder = ({
  teamId,
  levellogId,
  preQuestionId,
  authorId,
}: Omit<UriCustomHookType, 'feedbackId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/pre-questions/${preQuestionId}/author/${authorId}/edit`;
};

export const interviewQuestionsGetUriBuilder = ({
  teamId,
  levellogId,
}: Pick<UriCustomHookType, 'teamId' | 'levellogId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/interview-questions`;
};

export const convertDateAndTime = ({ startAt }: Pick<Team, 'startAt'>) => {
  const year = startAt.slice(0, 4);
  const month = startAt.slice(5, 7);
  const day = startAt.slice(8, 10);
  const time = `${startAt.slice(11, 13)}시 ${startAt.slice(14, 16)}분`;

  return `${year}년 ${month}월 ${day}일 ${time}`;
};

interface CheckFirstWordFinalConsonantType {
  word: string;
}
