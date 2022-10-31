import { createRef } from 'react';

import '@toast-ui/editor/dist/toastui-editor.css';

import { ComponentMeta, ComponentStory } from '@storybook/react';
import UiEditor from 'components/@commons/markdownEditor/UiEditor';

export default {
  title: '@commons/UiEditor',
  component: UiEditor,
} as ComponentMeta<typeof UiEditor>;

const Template: ComponentStory<typeof UiEditor> = (args) => <UiEditor {...args} />;

export const MARKDOWN = Template.bind({});
MARKDOWN.args = {
  needToolbar: true,
  autoFocus: false,
  height: '50rem',
  contentRef: createRef(),
  initialEditType: 'markdown',
};

export const WYSIWYG = Template.bind({});
WYSIWYG.args = {
  needToolbar: true,
  autoFocus: false,
  height: '50rem',
  contentRef: createRef(),
  initialEditType: 'wysiwyg',
};
