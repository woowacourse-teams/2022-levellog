export const feedbackAddUriBuilder = ({
  teamId,
  levellogId,
}: Pick<UriType, 'teamId' | 'levellogId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/feedback/add`;
};

export const feedbackEditUriBuilder = ({
  teamId,
  levellogId,
  feedbackId,
  authorId,
}: Omit<UriType, 'preQuestionId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/feedback/${feedbackId}/author/${authorId}/edit`;
};

export const feedbacksGetUriBuilder = ({
  teamId,
  levellogId,
}: Pick<UriType, 'teamId' | 'levellogId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/feedbacks`;
};

export const feedbackGetUriBuilder = ({
  teamId,
  levellogId,
  feedbackId,
}: Pick<UriType, 'teamId' | 'levellogId' | 'feedbackId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/feedback/${feedbackId}`;
};

export const levellogAddUriBuilder = ({ teamId }: Pick<UriType, 'teamId'>) => {
  return `/teams/${teamId}/levellogs/add`;
};

export const levellogEditUriBuilder = ({
  teamId,
  levellogId,
  authorId,
}: Pick<UriType, 'teamId' | 'levellogId' | 'authorId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/author/${authorId}/edit`;
};

export const teamEditUriBuilder = ({ teamId }: Pick<UriType, 'teamId'>) => {
  return `/teams/${teamId}/edit`;
};

export const teamGetUriBuilder = ({ teamId }: Pick<UriType, 'teamId'>) => {
  return `/teams/${teamId}`;
};

export const preQuestionAddUriBuilder = ({
  teamId,
  levellogId,
}: Pick<UriType, 'teamId' | 'levellogId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/pre-questions/add`;
};

export const preQuestionEditUriBuilder = ({
  teamId,
  levellogId,
  preQuestionId,
  authorId,
}: Omit<UriType, 'feedbackId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/pre-questions/${preQuestionId}/author/${authorId}/edit`;
};

export const interviewQuestionsGetUriBuilder = ({
  teamId,
  levellogId,
}: Pick<UriType, 'teamId' | 'levellogId'>) => {
  return `/teams/${teamId}/levellogs/${levellogId}/interview-questions`;
};

interface UriType {
  teamId: string | undefined;
  levellogId: string | undefined;
  feedbackId: string | undefined;
  preQuestionId: string | undefined;
  authorId: string | undefined;
}
