import styled from 'styled-components';

import useLevellogQuery from 'hooks/levellog/useLevellogQuery';
import usePreQuestionQuery from 'hooks/preQuestion/usePreQuestionQuery';

import Button from 'components/@commons/Button';
import UiViewer from 'components/@commons/markdownEditor/UiViewer';

const WriterDocument = ({
  whichContentShow,
  handleClickLevellogTag,
  handleClickPreQuestionTag,
}: LevellogReportProps) => {
  const { levellogError, levellogInfo } = useLevellogQuery();
  const { preQuestionError, preQuestion } = usePreQuestionQuery();

  return (
    <S.Container>
      <S.ButtonBox>
        <S.LevellogButton whichContentShow={whichContentShow} onClick={handleClickLevellogTag}>
          레벨로그
        </S.LevellogButton>
        <S.PreQuestionButton
          whichContentShow={whichContentShow}
          onClick={handleClickPreQuestionTag}
        >
          사전질문
        </S.PreQuestionButton>
      </S.ButtonBox>
      <S.Content>
        {whichContentShow.levellog && (
          <UiViewer
            content={levellogError ? '레벨로그가 존재하지 않습니다.' : levellogInfo!.content}
          />
        )}
        {whichContentShow.preQuestion && (
          <UiViewer
            content={preQuestionError ? '사전 질문이 존재하지 않습니다.' : preQuestion!.content}
          />
        )}
      </S.Content>
    </S.Container>
  );
};

interface LevellogReportProps {
  whichContentShow: {
    levellog: boolean;
    preQuestion: boolean;
  };
  handleClickLevellogTag: () => void;
  handleClickPreQuestionTag: () => void;
}

const S = {
  Container: styled.div`
    overflow: auto;
    position: relative;
    width: 100%;
    height: calc(100vh - 21.875rem);
    box-shadow: 0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.new_default.GRAY};
    border-radius: 0.1875rem 0.5rem 0.1875rem 0.1875rem;
    border: 1px solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  `,

  ButtonBox: styled.div`
    position: absolute;
    top: 0;
    width: 100%;
    z-index: 10;
  `,

  LevellogButton: styled(Button)`
    width: 6.25rem;
    height: 1.5rem;
    padding: 0;
    border-radius: 0 0 0.375rem 0.375rem;
    background-color: ${(props) =>
      props.whichContentShow.levellog
        ? props.theme.new_default.BLACK
        : props.theme.new_default.GRAY};
    font-size: 0.875rem;
  `,

  PreQuestionButton: styled(Button)`
    width: 6.25rem;
    height: 1.5rem;
    padding: 0;
    border-radius: 0 0 0.375rem 0.375rem;
    font-size: 0.875rem;
    background-color: ${(props) =>
      props.whichContentShow.preQuestion
        ? props.theme.new_default.BLACK
        : props.theme.new_default.GRAY};
    font-size: 0.875rem;
  `,

  Content: styled.div`
    overflow: auto;
    width: 100%;
    padding: 2rem 1rem;
    border-radius: 0.5rem;
    background-color: ${(props) => props.theme.default.WHITE};
    @media (max-width: 520px) {
      flex-direction: column;
      max-width: 22.875rem;
      min-height: 0;
    }
  `,
};

export default WriterDocument;
