export const interviewTitleValidate = ({ text }: InterviewTextValidateProps): boolean => {
  if (text.length >= 3 && text.length <= 14) {
    return false;
  }

  return true;
};

export const interviewLocationValidate = ({ text }: InterviewTextValidateProps): boolean => {
  if (text.length >= 3 && text.length <= 12) {
    return false;
  }

  return true;
};

export const interviewDateValidate = ({ text }: InterviewTextValidateProps) => {
  const date = new Date();
  if (
    text.split('-').join('') <
      `${date.getFullYear()}${setDateFormat({ text: String(date.getMonth() + 1) })}${setDateFormat({
        text: String(date.getDate()),
      })}` ||
    text.split('-').join('') >
      `${date.getFullYear() + 1}${setDateFormat({
        text: String(date.getMonth() + 1),
      })}${setDateFormat({ text: String(date.getDate()) })}`
  ) {
    return true;
  }

  return false;
};

export const interviewTimeValidate = ({ text }: InterviewTextValidateProps) => {
  if (text) {
    return false;
  }

  return true;
};

export const interviewIntervieweeValidate = ({ text }: InterviewNumberValidateProps) => {
  if (text >= 1 && text <= 3) {
    return false;
  }

  return true;
};

const setDateFormat = ({ text }: InterviewTextValidateProps) => {
  return text.length === 1 ? `0${text}` : text;
};

interface InterviewTextValidateProps {
  text: string;
}

interface InterviewNumberValidateProps {
  text: number;
}
