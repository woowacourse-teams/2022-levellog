import axios from 'axios';
import { SERVER_PATH } from '../constants/constants';
import { LevellogType } from '../types';

export const postLevellog = (levellogContent: LevellogType) => {
  axios({
    method: 'post',
    url: SERVER_PATH.LEVELLOGS,
    data: levellogContent,
  });
};

export const getLevellog = (id: number) =>
  axios({
    method: 'get',
    url: `${SERVER_PATH.LEVELLOGS}/${id}`,
  });
