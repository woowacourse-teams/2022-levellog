import { UriCustomHookType } from 'types/uri';

const useUriBuilder = () => {
  const feedbackAddUriBuilder = ({
    teamId,
    levellogId,
  }: Pick<UriCustomHookType, 'teamId' | 'levellogId'>) => {
    return `/teams/${teamId}/levellogs/${levellogId}/feedbacks/add`;
  };

  const feedbackEditUriBuilder = ({
    teamId,
    levellogId,
    feedbackId,
    authorId,
  }: Omit<UriCustomHookType, 'preQuestionId'>) => {
    return `/teams/${teamId}/levellogs/${levellogId}/feedbacks/${feedbackId}/author/${authorId}/edit`;
  };

  const feedbacksGetUriBuilder = ({
    teamId,
    levellogId,
  }: Pick<UriCustomHookType, 'teamId' | 'levellogId'>) => {
    return `/teams/${teamId}/levellogs/${levellogId}/feedbacks`;
  };

  const feedbackGetUriBuilder = ({
    teamId,
    levellogId,
    feedbackId,
  }: Pick<UriCustomHookType, 'teamId' | 'levellogId' | 'feedbackId'>) => {
    return `/teams/${teamId}/levellogs/${levellogId}/feedbacks/${feedbackId}`;
  };

  const levellogAddUriBuilder = ({ teamId }: Pick<UriCustomHookType, 'teamId'>) => {
    return `/teams/${teamId}/levellogs/add`;
  };

  const levellogEditUriBuilder = ({
    teamId,
    levellogId,
    authorId,
  }: Pick<UriCustomHookType, 'teamId' | 'levellogId' | 'authorId'>) => {
    return `/teams/${teamId}/levellogs/${levellogId}/author/${authorId}/edit`;
  };

  const teamEditUriBuilder = ({ teamId }: Pick<UriCustomHookType, 'teamId'>) => {
    return `/teams/${teamId}/edit`;
  };

  const teamGetUriBuilder = ({ teamId }: Pick<UriCustomHookType, 'teamId'>) => {
    return `/teams/${teamId}`;
  };

  const preQuestionAddUriBuilder = ({
    teamId,
    levellogId,
  }: Pick<UriCustomHookType, 'teamId' | 'levellogId'>) => {
    return `teams/${teamId}/levellogs/${levellogId}/preQuestions/add`;
  };

  const preQuestionEditUriBuilder = ({
    teamId,
    levellogId,
    preQuestionId,
    authorId,
  }: Omit<UriCustomHookType, 'feedbackId'>) => {
    return `teams/${teamId}/levellogs/${levellogId}/preQuestions/${preQuestionId}/author/${authorId}/edit`;
  };

  const interviewQuestionsGetUriBuilder = ({
    teamId,
    levellogId,
  }: Pick<UriCustomHookType, 'teamId' | 'levellogId'>) => {
    return `teams/${teamId}/levellogs/${levellogId}/interviewQuestions`;
  };

  return {
    feedbackAddUriBuilder,
    feedbackEditUriBuilder,
    feedbackGetUriBuilder,
    feedbacksGetUriBuilder,
    levellogAddUriBuilder,
    levellogEditUriBuilder,
    teamEditUriBuilder,
    teamGetUriBuilder,
    preQuestionAddUriBuilder,
    preQuestionEditUriBuilder,
    interviewQuestionsGetUriBuilder,
  };
};

export default useUriBuilder;
