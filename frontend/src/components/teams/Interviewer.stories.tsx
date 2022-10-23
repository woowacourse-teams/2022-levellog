import Interviewer from './Interviewer';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'team/Interviewer',
  component: Interviewer,
} as ComponentMeta<typeof Interviewer>;

const Template: ComponentStory<typeof Interviewer> = (args) => <Interviewer {...args} />;

export const Base = Template.bind({});
Base.args = {
  participant: {
    memberId: '1',
    levellogId: '2',
    preQuestionId: '3',
    nickname: '인터뷰하는 사람',
    profileUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=100',
  },
  interviewees: [1, 2],
  interviewers: [3, 4],
  teamStatus: 'IN_PROGRESS',
  userInTeam: true,
  onClickOpenLevellogModal: () => {},
  onClickOpenPreQuestionModal: () => {},
};

export const LevellogNotYet = Template.bind({});
LevellogNotYet.args = {
  participant: {
    memberId: '',
    levellogId: '',
    preQuestionId: '',
    nickname: '인터뷰하는 사람',
    profileUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=100',
  },
  interviewees: [1, 2],
  interviewers: [3, 4],
  teamStatus: 'READY',
  userInTeam: true,
  onClickOpenLevellogModal: () => {},
  onClickOpenPreQuestionModal: () => {},
};

export const PrequestionNotYet = Template.bind({});
PrequestionNotYet.args = {
  participant: {
    memberId: '1',
    levellogId: '2',
    preQuestionId: '',
    nickname: '인터뷰하는 사람',
    profileUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=100',
  },
  interviewees: [1, 2, 3],
  interviewers: [1, 4, 5],
  teamStatus: 'CLOSED',
  userInTeam: true,
  onClickOpenLevellogModal: () => {},
  onClickOpenPreQuestionModal: () => {},
};
