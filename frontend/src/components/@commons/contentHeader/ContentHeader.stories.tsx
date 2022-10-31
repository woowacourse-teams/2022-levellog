import FilterButton from '../FilterButton';
import { ComponentMeta, ComponentStory } from '@storybook/react';
import Button from 'components/@commons/button/Button';
import ContentHeader from 'components/@commons/contentHeader/ContentHeader';

export default {
  title: '@commons/ContentHeader',
  component: ContentHeader,
} as ComponentMeta<typeof ContentHeader>;

const Template: ComponentStory<typeof ContentHeader> = (args) => {
  return <ContentHeader {...args}></ContentHeader>;
};

export const Base = Template.bind({});
Base.args = {
  title: '타이틀',
};

export const WithRightButton = Template.bind({});
WithRightButton.args = {
  title: '타이틀',
  children: <Button>버튼</Button>,
};

export const WithImageRightButton = Template.bind({});
WithImageRightButton.args = {
  imageUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
  title: '타이틀',
  children: <Button>버튼</Button>,
};

export const WithSubtitleRightButton = Template.bind({});
WithSubtitleRightButton.args = {
  title: '타이틀',
  subTitle: '서브 타이틀',
  children: <Button>버튼</Button>,
};

export const WithLeftButton = Template.bind({});
WithLeftButton.args = {
  title: '타이틀',
  children: [<Button>버튼</Button>, <span />],
};

export const WithAllProps = Template.bind({});
WithAllProps.args = {
  imageUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
  title: '타이틀',
  subTitle: '서브 타이틀',
  children: [
    <div>
      <FilterButton>첫번째 좌측 버튼</FilterButton>
      <FilterButton>두번째 좌측 버튼</FilterButton>
      <FilterButton>세번째 좌측 버튼</FilterButton>
    </div>,
    <Button>우측 버튼</Button>,
  ],
};
