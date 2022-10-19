import { UserType } from 'types/index';

export interface LevellogType {
  content: string;
}

export interface LevellogInfoType extends LevellogType {
  author: UserType;
}
