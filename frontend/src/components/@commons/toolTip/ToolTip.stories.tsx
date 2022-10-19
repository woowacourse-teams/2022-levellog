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
    '내 어머니는 성취와 성공의 차이를 분명히 하셨다. 어머니는 말씀하셨다. 성취란 네가 열심히 공부하고 일했으며 네가 가진 최선을 다했다는 인식이다. 성공은 남들에게 추앙받는 것이며, 이것이 멋진 일이긴 하나 그렇게 중요하거나 만족을 주는 것은 아니다. 항상 성취를 목적으로 삼고 성공에 대해선 잊어라.',
};
