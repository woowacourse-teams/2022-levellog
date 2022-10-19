import { ComponentMeta, ComponentStory } from '@storybook/react';
import Image from 'components/@commons/images/Image';

export default {
  title: 'Image',
  component: Image,
} as ComponentMeta<typeof Image>;

const Template: ComponentStory<typeof Image> = (args) => <Image {...args} />;

export const Base = Template.bind({});
Base.args = {
  src: 'https://cdn.pixabay.com/photo/2017/08/30/12/45/girl-2696947__480.jpg',
  borderRadius: false,
};

export const Radius = Template.bind({});
Radius.args = {
  src: 'https://cdn.pixabay.com/photo/2017/08/30/12/45/girl-2696947__480.jpg',
  borderRadius: true,
};
