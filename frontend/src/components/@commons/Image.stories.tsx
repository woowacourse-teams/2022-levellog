import Image from './Image';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'Image',
  component: Image,
} as ComponentMeta<typeof Image>;

const Template: ComponentStory<typeof Image> = (args) => <Image {...args} />;

export const Base = Template.bind({});
