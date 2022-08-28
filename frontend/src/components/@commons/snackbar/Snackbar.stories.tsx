import { ComponentMeta, ComponentStory } from '@storybook/react';
import Snackbar from 'components/@commons/snackbar/Snackbar';

export default {
  title: 'Snackbar',
  component: Snackbar,
} as ComponentMeta<typeof Snackbar>;

const Template: ComponentStory<typeof Snackbar> = () => <Snackbar>{'이건 스낵바여'}</Snackbar>;

export const Base = Template.bind({});
