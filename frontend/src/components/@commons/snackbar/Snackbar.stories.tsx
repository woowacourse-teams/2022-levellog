import { ComponentMeta, ComponentStory } from '@storybook/react';
import Snackbar from 'components/@commons/snackbar/Snackbar';

export default {
  title: '@commons/Snackbar',
  component: Snackbar,
} as ComponentMeta<typeof Snackbar>;

const Template: ComponentStory<typeof Snackbar> = (args) => <Snackbar {...args}></Snackbar>;

export const Base = Template.bind({});
Base.args = {
  children: '스낵바',
};
