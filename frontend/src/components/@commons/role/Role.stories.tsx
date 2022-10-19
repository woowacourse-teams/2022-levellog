import { ComponentMeta, ComponentStory } from '@storybook/react';
import Role from 'components/@commons/role/Role';

export default {
  title: 'Role',
  component: Role,
} as ComponentMeta<typeof Role>;

const Template: ComponentStory<typeof Role> = (args) => <Role {...args}>?</Role>;

export const Interviewer = Template.bind({});
Interviewer.args = {
  role: '인터뷰어',
};

export const Interviewee = Template.bind({});
Interviewee.args = {
  role: '인터뷰이',
};

export const InterviewEach = Template.bind({});
InterviewEach.args = {
  role: '상호 인터뷰',
};
