import styled from 'styled-components';

import Button from 'components/@commons/Button';
import UiViewer from 'components/@commons/UiViewer';
import { LevellogInfoType } from 'types/levellog';

const WriterDocument = ({
  levellogInfo,
  preQuestion,
  whichContentShow,
  handleClickLevellogTag,
  handleClickPreQuestionTag,
}: LevellogReportProps) => {
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
        {whichContentShow.levellog && <UiViewer content={levellogInfo.content} />}
        {whichContentShow.preQuestion && <UiViewer content={preQuestion} />}
      </S.Content>
    </S.Container>
  );
};

interface LevellogReportProps {
  levellogInfo: LevellogInfoType;
  preQuestion: string;
  whichContentShow: {
    levellog: boolean;
    preQuestion: boolean;
  };
  handleClickLevellogTag: () => void;
  handleClickPreQuestionTag: () => void;
}

const S = {
  Container: styled.div`
    position: relative;
    width: 100%;
    height: calc(100% - 50px);
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
      props.whichContentShow.levellog && props.theme.default.LIGHT_GRAY};
    font-size: 0.875rem;
    :hover {
      background-color: ${(props) => props.theme.default.LIGHT_GRAY};
    }
  `,

  PreQuestionButton: styled(Button)`
    width: 6.25rem;
    height: 1.5rem;
    padding: 0;
    border-radius: 0 0 0.375rem 0.375rem;
    background-color: ${(props) =>
      props.whichContentShow.preQuestion && props.theme.default.LIGHT_GRAY};
    font-size: 0.875rem;
    :hover {
      background-color: ${(props) => props.theme.default.LIGHT_GRAY};
    }
  `,

  Content: styled.div`
    overflow: auto;
    width: 100%;
    height: 60rem;
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
