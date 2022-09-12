import { Team } from 'types/team';

export const checkFirstWordFinalConsonant = ({ word }: CheckFirstWordFinalConsonantType) => {
  if (typeof word !== 'string') return;

  let lastLetter = word[word.length - 1];
  let uniCode = lastLetter.charCodeAt(0);

  if (uniCode < 44032 || uniCode > 55203) return;

  return (uniCode - 44032) % 28 != 0;
};

export const createParam = (object: CreateParamType) => {
  return Object.entries(object)
    .flat(1)
    .map((v) => v && `/${v}`)
    .join('');
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

interface CreateParamType {
  teams?: string;
  levellogs?: string;
  feedbackId?: string;
  'pre-questions'?: string;
  author?: string;
  feedbacks?: string;
  ''?: string;
}
