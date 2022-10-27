import ProfileDropdown from './ProfileDropdown';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'header/ProfileDropdown',
  component: ProfileDropdown,
} as ComponentMeta<typeof ProfileDropdown>;

const HeaderTemplate: ComponentStory<typeof ProfileDropdown> = (args) => {
  return <ProfileDropdown {...args} />;
};

export const Base = HeaderTemplate.bind({});
Base.args = {
  isShowProfileDropdown: true,
  loginUserNickname: '결',
  handleClickLogoutButton: () => {},
};
