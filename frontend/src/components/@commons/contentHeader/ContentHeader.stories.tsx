import { ComponentMeta, ComponentStory } from '@storybook/react';
import Button from 'components/@commons/button/Button';
import ContentHeader from 'components/@commons/contentHeader/ContentHeader';

export default {
  title: 'ContentHeader',
  component: ContentHeader,
} as ComponentMeta<typeof ContentHeader>;

const Template: ComponentStory<typeof ContentHeader> = (args) => (
  <ContentHeader {...args}>{args.children}</ContentHeader>
);

export const Base = Template.bind({});
Base.args = {
  title: '타이틀',
};

export const WithRightbutton = Template.bind({});
WithRightbutton.args = {
  title: '타이틀',
  children: <Button>버튼</Button>,
};

export const WithImageRightbutton = Template.bind({});
WithImageRightbutton.args = {
  imageUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
  title: '타이틀',
  children: <Button>버튼</Button>,
};

export const WithSubtitleRightbutton = Template.bind({});
WithSubtitleRightbutton.args = {
  title: '타이틀',
  subTitle: '서브 타이틀',
  children: <Button>버튼</Button>,
};

export const WithLeftbutton = Template.bind({});
WithLeftbutton.args = {
  title: '타이틀',
  children: [<Button>버튼</Button>, <span />],
};
