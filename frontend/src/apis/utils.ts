import { RequestType } from 'types';

export const 엑세스토큰이없는경우헤더제거 = ({
  accessToken,
  method,
  url,
  headers,
}: RequestType) => {
  if (!accessToken) {
    return {
      accessToken,
      method,
      url,
    };
  }

  return { accessToken, method, url, headers };
};
