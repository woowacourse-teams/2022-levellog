import levellogIcon from 'assets/images/levellogIcon.svg';

import { ComponentMeta, ComponentStory } from '@storybook/react';
import Button from 'components/@commons/button/Button';
import InterviewerButton from 'components/teams/InterviewerButton';

export default {
  title: '@commons/Button',
  component: Button,
} as ComponentMeta<typeof Button>;

const Template: ComponentStory<typeof Button> = (args) => <Button {...args}></Button>;

export const Base = Template.bind({});
Base.args = {
  children: '기본버튼',
};

const InterviewerButtonTemplate: ComponentStory<typeof InterviewerButton> = (args) => {
  return <InterviewerButton {...args}></InterviewerButton>;
};

export const InterviewerButtonBase = InterviewerButtonTemplate.bind({});
InterviewerButtonBase.args = {
  isDisabled: false,
  buttonIcon: levellogIcon,
  buttonText: '레벨로그 작성',
};

export const DisabledInterviewerButton = InterviewerButtonTemplate.bind({});
DisabledInterviewerButton.args = {
  isDisabled: true,
  buttonIcon: levellogIcon,
  buttonText: '레벨로그 작성',
};
