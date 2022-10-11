import styled from 'styled-components';

import { Editor } from '@toast-ui/react-editor';

import { FeedbackTitle } from 'components/@commons/Style';
import UiEditor from 'components/@commons/markdownEditor/UiEditor';

const FeedbackFormat = ({ feedbackRef }: FeedbackFormProps) => {
  return (
    <S.Container>
      <FeedbackTitle>피드백</FeedbackTitle>
      <S.Content>
        <p>학습 측면에서 좋은 점과 부족한 점은?</p>
        <UiEditor
          needToolbar={false}
          autoFocus={false}
          height={'18.75rem'}
          contentRef={(el: Editor) => (feedbackRef.current[0] = el)}
          initialEditType={'markdown'}
        />
        <p>인터뷰, 말하기 측면에서 좋은 점과 개선할 부분은?</p>
        <UiEditor
          needToolbar={false}
          autoFocus={false}
          height={'18.75rem'}
          contentRef={(el: Editor) => (feedbackRef.current[1] = el)}
          initialEditType={'markdown'}
        />
        <p>기타 피드백 (위 2 질문 외에 다른 피드백도 주세요.)</p>
        <UiEditor
          needToolbar={false}
          autoFocus={false}
          height={'18.75rem'}
          contentRef={(el: Editor) => (feedbackRef.current[2] = el)}
          initialEditType={'markdown'}
        />
      </S.Content>
    </S.Container>
  );
};

interface FeedbackFormProps {
  feedbackRef: React.MutableRefObject<Editor[]>;
}

const S = {
  Container: styled.div`
    overflow: auto;
    width: 100%;
  `,

  FeedbackTitle: styled.h2`
    margin-bottom: 1.25rem;
    font-size: 1.875rem;
  `,

  Content: styled.div`
    display: flex;
    flex-direction: column;
    align-content: space-between;
    gap: 1.5rem;
    width: 100%;
  `,

  FeedbackLabel: styled.p`
    font-size: 1rem;
    font-weight: 600;
  `,
};
export default FeedbackFormat;
