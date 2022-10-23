import InterviewTeam from './InterviewTeam';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'team/InterviewTeam',
  component: InterviewTeam,
} as ComponentMeta<typeof InterviewTeam>;

const Template: ComponentStory<typeof InterviewTeam> = (args) => <InterviewTeam {...args} />;

export const Base = Template.bind({});
Base.args = {
  team: {
    id: '1',
    title: '모의 인터뷰',
    place: '루터회괸',
    startAt: '2022-10-31T12:59:00',
    teamImage: 'https://avatars.githubusercontent.com/u/32123302?v=4',
    status: 'READY',
    participants: [
      {
        memberId: '2',
        nickname: '결',
        profileUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4',
      },
      {
        memberId: '3',
        nickname: '해리',
        profileUrl: 'https://avatars.githubusercontent.com/u/75592315?v=4',
      },
    ],
  },
};

export const Close = Template.bind({});
Close.args = {
  team: {
    id: '1',
    title: '모의 인터뷰',
    place: '루터회괸',
    startAt: '2022-10-31T12:59:00',
    teamImage: 'https://avatars.githubusercontent.com/u/32123302?v=4',
    status: 'CLOSED',
    participants: [
      {
        memberId: '2',
        nickname: '결',
        profileUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4',
      },
      {
        memberId: '3',
        nickname: '해리',
        profileUrl: 'https://avatars.githubusercontent.com/u/75592315?v=4',
      },
    ],
  },
};
