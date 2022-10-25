import axios from 'axios';

export const fetcher = axios.create({
  baseURL: `${process.env.API_URI}`,
});

export const AuthorizationHeader = (accessToken: string | null) => {
  return {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  };
};
