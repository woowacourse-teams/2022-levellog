import styled from 'styled-components';

import usePreQuestionAdd from 'hooks/preQuestion/usePreQuestionAdd';

import BottomBar from 'components/@commons/BottomBar';
import UiEditor from 'components/@commons/markdownEditor/UiEditor';
import LevellogContent from 'components/levellogs/LevellogContent';

const PreQuestionAdd = () => {
  const { preQuestionRef, handleClickPreQuestionAddButton } = usePreQuestionAdd();
  return (
    <S.Container>
      <S.Content>
        <S.LeftContent>
          <LevellogContent />
        </S.LeftContent>
        <S.RightContent>
          <UiEditor
            needToolbar={true}
            autoFocus={true}
            contentRef={preQuestionRef}
            initialEditType={'markdown'}
          />
        </S.RightContent>
      </S.Content>
      <BottomBar
        buttonText={'작성하기'}
        handleClickRightButton={handleClickPreQuestionAddButton}
      ></BottomBar>
    </S.Container>
  );
};

const S = {
  Container: styled.div`
    display: flex;
    overflow: auto;
    flex-direction: column;
    height: calc(100vh - 8.75rem);
    @media (min-width: 1620px) {
      padding: 1.25rem calc((100vw - 100rem) / 2) 0 calc((100vw - 100rem) / 2);
    }
    @media (max-width: 1620px) {
      padding: 1.25rem 1.25rem 0 1.25rem;
    }
    @media (max-width: 520px) {
      height: max-content;
    }
  `,

  Content: styled.div`
    display: flex;
    gap: 1rem;
    height: calc(100vh - 14.375rem);
    @media (max-width: 520px) {
      flex-direction: column;
    }
  `,

  LeftContent: styled.div`
    width: 50%;
    height: inherit;
    @media (max-width: 520px) {
      width: 100%;
    }
  `,

  LevellogTitle: styled.h2`
    margin-bottom: 1.875rem;
    font-size: 1.875rem;
  `,

  RightContent: styled.div`
    width: 50%;
    height: inherit;
    @media (max-width: 520px) {
      width: 100%;
      height: 31.25rem;
    }
  `,

  UiEditorContainer: styled.div`
    overflow: auto;
    width: 50%;
    @media (max-width: 520px) {
      max-width: 22.875rem;
    }
  `,

  Title: styled.h2`
    margin-bottom: 1.875rem;
    font-size: 1.875rem;
  `,
};

export default PreQuestionAdd;
