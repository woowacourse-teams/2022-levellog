import { ComponentMeta, ComponentStory } from '@storybook/react';
import ToolTip from 'components/@commons/ToolTip';

export default {
  title: 'ToolTip',
  component: ToolTip,
} as ComponentMeta<typeof ToolTip>;

const Template: ComponentStory<typeof ToolTip> = (args) => <ToolTip {...args}>?</ToolTip>;

export const Base = Template.bind({});
Base.args = {
  toolTipText: '툴팁입니다.툴팁입니다.',
};
