import axios, { AxiosResponse } from 'axios';

import { WrongAccessToken } from 'apis/utils';

const errorHandler = ({ err, showSnackbar }: ErrorHandleProps) => {
  if (axios.isAxiosError(err) && err instanceof Error) {
    const responseBody: AxiosResponse = err.response!;
    if (WrongAccessToken({ message: responseBody.data.message, showSnackbar })) {
      showSnackbar({ message: responseBody.data.message });
    }
  }
};

interface ErrorHandleProps {
  err: unknown;
  showSnackbar: ({ message }: { message: string }) => void;
}

export default errorHandler;
