export const interviewTitleValidate = ({ value }: InterviewStringValueValidateProps): boolean => {
  if (value.length >= 3 && value.length <= 14) {
    return true;
  }

  return false;
};

export const interviewLocationValidate = ({
  value,
}: InterviewStringValueValidateProps): boolean => {
  if (value.length >= 3 && value.length <= 12) {
    return true;
  }

  return false;
};

export const interviewDateValidate = ({ value }: InterviewStringValueValidateProps) => {
  const date = new Date();
  if (
    value.split('-').join('') >=
      `${date.getFullYear()}${setDateFormat({ value: String(date.getMonth() + 1) })}${setDateFormat(
        {
          value: String(date.getDate()),
        },
      )}` &&
    value.split('-').join('') <
      `${date.getFullYear() + 1}${setDateFormat({
        value: String(date.getMonth() + 1),
      })}${setDateFormat({ value: String(date.getDate()) })}`
  ) {
    return true;
  }

  return false;
};

export const interviewTimeValidate = ({ value }: InterviewStringValueValidateProps) => {
  if (value) {
    return true;
  }

  return false;
};

export const interviewInterviewerValidate = ({ value }: InterviewNumberValueValidateProps) => {
  if (value - Math.floor(value) === 0 && value >= 1 && value <= 3) {
    return true;
  }

  return false;
};

export const interviewParticipantValidate = ({ value }: InterviewNumberValueValidateProps) => {
  if (value > 1) {
    return true;
  }

  return false;
};

const setDateFormat = ({ value }: InterviewStringValueValidateProps) => {
  return value.length === 1 ? `0${value}` : value;
};

interface InterviewStringValueValidateProps {
  value: string;
}

interface InterviewNumberValueValidateProps {
  value: number;
}
