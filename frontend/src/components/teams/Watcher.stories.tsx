import Watcher from './Watcher';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'team/Watcher',
  component: Watcher,
} as ComponentMeta<typeof Watcher>;

const Template: ComponentStory<typeof Watcher> = (args) => <Watcher {...args} />;

export const Base = Template.bind({});
Base.args = {
  watcher: {
    memberId: '1',
    nickname: '결',
    profileUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4',
  },
};
