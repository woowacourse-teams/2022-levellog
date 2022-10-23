import levellogLog from 'assets/images/levellogLogoCenter.webp';
import notFound from 'assets/images/notFound.webp';

import { ComponentMeta, ComponentStory } from '@storybook/react';
import Image from 'components/@commons/image/Image';

export default {
  title: '@commons/Image',
  component: Image,
} as ComponentMeta<typeof Image>;

const Template: ComponentStory<typeof Image> = (args) => <Image {...args} />;

export const Base = Template.bind({});
Base.args = {
  src: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
  borderRadius: false,
};

export const Radius = Template.bind({});
Radius.args = {
  src: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
  borderRadius: true,
};

export const SizeTiny = Template.bind({});
SizeTiny.args = {
  src: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
  sizes: 'TINY',
  borderRadius: false,
};

export const SizeSmall = Template.bind({});
SizeSmall.args = {
  src: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
  sizes: 'SMALL',
  borderRadius: false,
};

export const SizeMedium = Template.bind({});
SizeMedium.args = {
  src: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
  sizes: 'MEDIUM',
  borderRadius: false,
};

export const SizeLarge = Template.bind({});
SizeLarge.args = {
  src: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
  sizes: 'LARGE',
  borderRadius: false,
};

export const SizeHuge = Template.bind({});
SizeHuge.args = {
  src: levellogLog,
  sizes: 'HUGE',
  borderRadius: false,
};

export const SizeExtrahuge = Template.bind({});
SizeExtrahuge.args = {
  src: levellogLog,
  sizes: 'EXTRA_HUGE',
  borderRadius: false,
};

export const SizeException = Template.bind({});
SizeException.args = {
  src: notFound,
  sizes: 'EXCEPTION',
  borderRadius: false,
};
