import Feedback from './Feedback';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'Feedback',
  component: Feedback,
} as ComponentMeta<typeof Feedback>;

const Template: ComponentStory<typeof Feedback> = (args) => <Feedback {...args} />;

export const Base = Template.bind({});
Base.args = {
  loginUserId: '1',
  teamId: '1',
  levellogId: '1',
  feedbackInfo: {
    id: 1,
    updatedAt: '',
    from: {
      id: '1',
      nickname: '결',
      profileUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
    },
    feedback: {
      study: '잘하시네요',
      speak: '잘하시네요',
      etc: '잘하시네요',
    },
  },
  teamStatus: 'IN_PROGRESS',
};

export const Others = Template.bind({});
Others.args = {
  loginUserId: '1',
  teamId: '1',
  levellogId: '1',
  feedbackInfo: {
    id: 1,
    updatedAt: '',
    from: {
      id: '2',
      nickname: '결',
      profileUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
    },
    feedback: {
      study: '잘하시네요',
      speak: '잘하시네요',
      etc: '잘하시네요',
    },
  },
  teamStatus: 'CLOSED',
};
