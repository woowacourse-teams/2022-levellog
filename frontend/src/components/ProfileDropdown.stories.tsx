import ProfileDropdown from './ProfileDropdown';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'ProfileDropdown',
  component: ProfileDropdown,
  parameters: {
    isProfileDropdownShow: true,
  },
} as ComponentMeta<typeof ProfileDropdown>;

const Template: ComponentStory<typeof ProfileDropdown> = (args) => <ProfileDropdown {...args} />;

export const Base = Template.bind({});
