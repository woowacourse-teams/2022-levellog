import Input from './Input';

import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'Input',
  component: Input,
} as ComponentMeta<typeof Input>;

const Template: ComponentStory<typeof Input> = (args) => <Input {...args}></Input>;

export const Base = Template.bind({});
Base.args = {};
