import { ComponentMeta, ComponentStory } from '@storybook/react';
import ToolTip from 'components/@commons/toolTip/ToolTip';

export default {
  title: 'ToolTip',
  component: ToolTip,
} as ComponentMeta<typeof ToolTip>;

const Template: ComponentStory<typeof ToolTip> = (args) => <ToolTip {...args}>?</ToolTip>;

export const Base = Template.bind({});
Base.args = {
  toolTipText:
    '이것이 멋진 일이긴 하나 그렇게 중요하거나 만족을 주는 것은 아니다. 항상 성취를 목적으로 삼고 성공에 대해선 잊어라.',
};
