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

export const Submit = Template.bind({});
Submit.args = {
  children: '제출하기',
};
