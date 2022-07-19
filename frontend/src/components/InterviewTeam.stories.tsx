import InterviewTeam from './InterviewTeam';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'InterviewTeam',
  component: InterviewTeam,
} as ComponentMeta<typeof InterviewTeam>;

const Template: ComponentStory<typeof InterviewTeam> = (args) => <InterviewTeam {...args} />;

export const Base = Template.bind({});
Base.args = {
  id: 1,
  teamImage: 'https://avatars.githubusercontent.com/u/81607552?v=4',
  hostId: 1,
  title: '제이슨조',
  place: '잠실 트랙룸',
  startAt: '2022-07-15',
  participants: [
    { id: 1, nickname: '페퍼', profileUrl: 'https://avatars.githubusercontent.com/u/28749734?v=4' },
    {
      id: 2,
      nickname: 'OzRagwort',
      profileUrl: 'https://avatars.githubusercontent.com/u/32123302?v=4',
    },
    {
      id: 3,
      nickname: '유정이',
      profileUrl: 'https://avatars.githubusercontent.com/u/76840965?v=4',
    },
    { id: 4, nickname: '릭', profileUrl: 'https://avatars.githubusercontent.com/u/76840965?v=4' },
    { id: 5, nickname: '알린', profileUrl: 'https://avatars.githubusercontent.com/u/76840965?v=4' },
  ],
};
