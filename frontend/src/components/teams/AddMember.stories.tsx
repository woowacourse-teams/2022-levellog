import AddMember from './AddMember';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'team/AddMember',
  component: AddMember,
} as ComponentMeta<typeof AddMember>;

const Template: ComponentStory<typeof AddMember> = (args) => <AddMember {...args} />;

export const Base = Template.bind({});
Base.args = {
  addMember: {
    id: '1',
    nickname: '결',
    profileUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4',
  },
  removeEvent: () => {},
};
