import Button from './Button';
import ContentHeader from './ContentHeader';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'ContentHeader',
  component: ContentHeader,
} as ComponentMeta<typeof ContentHeader>;

const Template: ComponentStory<typeof ContentHeader> = (args) => (
  <ContentHeader {...args}></ContentHeader>
);

export const Base = Template.bind({});
Base.args = {
  title: '기본 컨텐트헤더',
};

export const WithButton = Template.bind({});
WithButton.args = {
  title: '버튼 있는 컨텐트헤더',
  children: <Button>버튼</Button>,
};

export const LevellogAdd = Template.bind({});
LevellogAdd.args = {
  title: '레벨로그 작성',
  children: <Button>제출하기</Button>,
};
