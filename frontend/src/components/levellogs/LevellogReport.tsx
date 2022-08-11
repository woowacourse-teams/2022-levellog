import styled from 'styled-components';

import { FeedbackTitle } from 'components/@commons/Style';
import UiViewer from 'components/@commons/UiViewer';

const LevellogReport = ({ levellog }: LevellogReportProps) => {
  return (
    <S.Container>
      <FeedbackTitle>레벨로그</FeedbackTitle>
      <S.Content>
        <UiViewer content={levellog} />
      </S.Content>
    </S.Container>
  );
};

interface LevellogReportProps {
  levellog: string;
}

const S = {
  Container: styled.div`
    width: 100%;
    @media (max-width: 520px) {
      max-width: 22.875rem;
    }
  `,

  Content: styled.div`
    overflow: auto;
    width: 100%;
    height: 60rem;
    padding: 1rem;
    border-radius: 0.5rem;
    background-color: ${(props) => props.theme.default.WHITE};
    @media (max-width: 520px) {
      flex-direction: column;
      max-width: 22.875rem;
      min-height: 0;
    }
  `,
};

export default LevellogReport;
