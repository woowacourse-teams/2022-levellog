import React from 'react';
import { ComponentStory, ComponentMeta } from '@storybook/react';

import ContentHeader from './ContentHeader';
import Button from './Button';

export default {
  title: 'ContentHeader',
  component: ContentHeader,
} as ComponentMeta<typeof ContentHeader>;

const Template: ComponentStory<typeof ContentHeader> = (args) => (
  <ContentHeader {...args}></ContentHeader>
);

export const ContentHeaderWithButton = Template.bind({});
ContentHeaderWithButton.args = {
  title: '버튼 있는 컨텐트헤더',
  children: <Button>버튼</Button>,
};

export const BaseContentHeader = Template.bind({});
BaseContentHeader.args = {
  title: '기본 컨텐트헤더',
};

export const LevellogAddContentHeader = Template.bind({});
LevellogAddContentHeader.args = {
  title: '레벨로그 작성',
  children: <Button>제출하기</Button>,
};
