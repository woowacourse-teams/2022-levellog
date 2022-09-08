export const checkFirstWordFinalConsonant = ({ word }: CheckFirstWordFinalConsonantType) => {
  if (typeof word !== 'string') return;

  let lastLetter = word[word.length - 1];
  let uniCode = lastLetter.charCodeAt(0);

  if (uniCode < 44032 || uniCode > 55203) return;

  return (uniCode - 44032) % 28 != 0;
};

interface CheckFirstWordFinalConsonantType {
  word: string;
}
