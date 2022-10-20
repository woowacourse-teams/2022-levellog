import usePreQuestionQuery from 'hooks/preQuestion/usePreQuestionQuery';

import UiViewer from 'components/@commons/markdownEditor/UiViewer';

const PreQuestionUiViewer = () => {
  const { preQuestionError, preQuestion } = usePreQuestionQuery();

  return (
    <UiViewer
      content={preQuestionError ? '사전 질문이 존재하지 않습니다.' : preQuestion!.content}
    />
  );
};

export default PreQuestionUiViewer;
