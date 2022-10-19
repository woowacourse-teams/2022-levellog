import { ComponentMeta, ComponentStory } from '@storybook/react';
import Input from 'components/@commons/input/Input';

export default {
  title: 'Input',
  component: Input,
} as ComponentMeta<typeof Input>;

const Template: ComponentStory<typeof Input> = (args) => <Input {...args}></Input>;

export const Base = Template.bind({});
Base.args = {};
