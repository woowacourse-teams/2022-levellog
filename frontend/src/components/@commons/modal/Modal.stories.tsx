import { ComponentMeta, ComponentStory } from '@storybook/react';
import Modal from 'components/@commons/modal/Modal';

export default {
  title: 'Modal',
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
      profileUrl: '',
    },
    content: '작성된 레벨로그가 보이는 영역입니다.',
  },
  contentName: '레벨로그',
};
