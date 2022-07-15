import { ComponentStory, ComponentMeta } from '@storybook/react';

import Header from './Header';

export default {
  title: 'Component',
  component: Header,
} as ComponentMeta<typeof Header>;

const HeaderTemplate: ComponentStory<typeof Header> = () => <Header />;

export const HeaderStory = HeaderTemplate.bind({});

HeaderStory.args = {};
