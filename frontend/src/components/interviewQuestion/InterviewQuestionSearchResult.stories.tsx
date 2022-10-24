import InterviewQuestionSearchResult from './InterviewQuestionSearchResult';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'interviewQuestion/InterviewQuestionSearchResult',
  component: InterviewQuestionSearchResult,
} as ComponentMeta<typeof InterviewQuestionSearchResult>;

const Template: ComponentStory<typeof InterviewQuestionSearchResult> = (args) => {
  return <InterviewQuestionSearchResult {...args} />;
};

export const Base = Template.bind({});
Base.args = {
  interviewQuestion: {
    id: 1,
    content: 'react query를 사용하신 이유가 있나요?',
    like: false,
    likeCount: 2,
  },
};

export const Like = Template.bind({});
Like.args = {
  interviewQuestion: {
    id: 1,
    content: 'react query를 사용하신 이유가 있나요?',
    like: true,
    likeCount: 2,
  },
};
