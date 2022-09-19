import { ComponentMeta, ComponentStory } from '@storybook/react';
import UiViewer from 'components/@commons/markdownEditor/UiViewer';

export default {
  title: 'UiViewer',
  component: UiViewer,
} as ComponentMeta<typeof UiViewer>;

const Template: ComponentStory<typeof UiViewer> = (args) => <UiViewer {...args} />;

export const Base = Template.bind({});
Base.args = {
  content: `## 안녕하세요
   - 안
   - 녕
  `,
};
