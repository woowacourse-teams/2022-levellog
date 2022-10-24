import Footer from './Footer';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'footer/Footer',
  component: Footer,
} as ComponentMeta<typeof Footer>;

const Template: ComponentStory<typeof Footer> = () => <Footer />;

export const Base = Template.bind({});
