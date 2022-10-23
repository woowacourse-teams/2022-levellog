import { ComponentMeta, ComponentStory } from '@storybook/react';
import BottomBar from 'components/@commons/bottomBar/BottomBar';

export default {
  title: '@commons/BottomBar',
  component: BottomBar,
} as ComponentMeta<typeof BottomBar>;

const Template: ComponentStory<typeof BottomBar> = (args) => {
  return <BottomBar {...args} />;
};

export const Base = Template.bind({});
Base.args = {
  buttonText: '제출하기',
  handleClickRightButton: () => {},
};
