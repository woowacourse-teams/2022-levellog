import { ComponentMeta, ComponentStory } from '@storybook/react';
import Modal from 'components/@commons/modal/Modal';

export default {
  title: '@commons/Modal',
  component: Modal,
} as ComponentMeta<typeof Modal>;

const Template: ComponentStory<typeof Modal> = (args) => {
  return <Modal {...args}></Modal>;
};

export const LevellogModal = Template.bind({});
LevellogModal.args = {
  modalContent: {
    author: {
      id: '123',
      nickname: '작성자 닉네임',
      profileUrl: 'https://avatars.githubusercontent.com/u/79692272?v=4&s=44',
    },
    content: '작성된 레벨로그가 보이는 영역입니다.',
  },
  contentName: '레벨로그',
};
