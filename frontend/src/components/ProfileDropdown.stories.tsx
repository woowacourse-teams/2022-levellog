import ProfileDropdown from './ProfileDropdown';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Component',
  component: ProfileDropdown,
  parameters: {
    isProfileDropdownShow: true,
  },
} as ComponentMeta<typeof ProfileDropdown>;

const ProfileDropdownTemplate: ComponentStory<typeof ProfileDropdown> = (args) => (
  <ProfileDropdown {...args} />
);

export const ProfileDropdownStory = ProfileDropdownTemplate.bind({});

// ProfileDropdown.args = {
//   isProfileDropdownShow: true
// };
