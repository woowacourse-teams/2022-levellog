import Header from './Header';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'Header',
  component: Header,
} as ComponentMeta<typeof Header>;

const HeaderTemplate: ComponentStory<typeof Header> = () => <Header />;

export const Base = HeaderTemplate.bind({});

Base.args = {};
