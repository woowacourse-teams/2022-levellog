import styled from 'styled-components';

import Button from './Button';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'Button',
  component: Button,
} as ComponentMeta<typeof Button>;

const Template: ComponentStory<typeof Button> = (args) => <Button {...args}></Button>;

export const Base = Template.bind({});
Base.args = {
  children: '기본버튼',
};

const InterviewerTemplate: ComponentStory<typeof Button> = (args) => (
  <InterviewerButton {...args}></InterviewerButton>
);

export const AbledInterviewerButton = InterviewerTemplate.bind({});
AbledInterviewerButton.args = {
  children: '레벨로그 작성하기',
  disabled: false,
};

export const DisabledInterviewerButton = InterviewerTemplate.bind({});
DisabledInterviewerButton.args = {
  children: '레벨로그 작성하기',
  disabled: true,
};

const InterviewerButton = styled(Button)`
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.625rem 0.75rem;
  border-radius: 2rem;
  background-color: ${(props) => props.theme.default.INVISIBLE};
  font-size: 1.125rem;
  font-weight: 600;
  color: ${(props) =>
    props.disabled ? props.theme.new_default.GRAY : props.theme.new_default.BLACK};
  :hover {
    box-shadow: 0.25rem 0.25rem 0.25rem ${(props) => props.theme.new_default.GRAY};
  }
`;
