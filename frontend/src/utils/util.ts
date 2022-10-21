import { NavigateFunction } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import { ShowSnackbarProps } from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

export const debounce: DebounceType = {
  flag: '',
  action({ func, args, setState, navigate, timer = 300 }) {
    if (this.flag) {
      clearTimeout(this.flag);
    }
    this.flag = setTimeout(async () => {
      const res = await func(args && { ...args });
      setState && setState(res.data);
      res && navigate && navigate[0](navigate[1]);
    }, timer);
  },
};

export const tryCatch = async <T>({
  func,
  args,
  snackbar,
}: TryCatchProps): Promise<T | boolean> => {
  try {
    const res = await func(args && { ...args });
    return res.data;
  } catch (err: unknown) {
    if (axios.isAxiosError(err)) {
      const responseBody: AxiosResponse = err.response!;
      if (err instanceof Error) {
        snackbar({
          message: responseBody.data.message || MESSAGE.CAN_NOT_EXPECT_ERROR,
        });
      }
    } else {
      snackbar({ message: MESSAGE.CAN_NOT_EXPECT_ERROR });
    }

    return false;
  }
};

export const checkFirstWordFinalConsonant = ({ word }: CheckFirstWordFinalConsonantType) => {
  if (typeof word !== 'string') return;

  let lastLetter = word[word.length - 1];
  let uniCode = lastLetter.charCodeAt(0);

  if (uniCode < 44032 || uniCode > 55203) return;

  if ((uniCode - hangeulFirstTextUnicode) % finalConsonantNumber !== 0) {
    return `${word}이 `;
  } else {
    return `${word}가 `;
  }
};

export const convertDateAndTime = ({ startAt }: any) => {
  const year = startAt.slice(0, 4);
  const month = startAt.slice(5, 7);
  const day = startAt.slice(8, 10);
  const time = `${startAt.slice(11, 13)}시 ${startAt.slice(14, 16)}분`;

  return `${year}년 ${month}월 ${day}일 ${time}`;
};

interface DebounceActionArgsType {
  func: Function;
  args?: { [props: string]: any };
  setState?: React.Dispatch<React.SetStateAction<any>>;
  navigate?: [NavigateFunction, string];
  timer?: number;
}

interface DebounceType {
  flag: '' | ReturnType<typeof setTimeout>;
  action: ({ func, args }: DebounceActionArgsType) => any;
}

interface TryCatchProps {
  func: Function;
  args?: { [props: string]: any };
  snackbar: ({ message }: ShowSnackbarProps) => any;
}
interface CheckFirstWordFinalConsonantType {
  word: string;
}
