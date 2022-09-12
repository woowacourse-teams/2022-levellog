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
