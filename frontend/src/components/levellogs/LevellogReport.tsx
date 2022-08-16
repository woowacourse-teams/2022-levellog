import styled from 'styled-components';

import UiViewer from 'components/@commons/UiViewer';
import { LevellogInfoType } from 'types/levellog';

const LevellogReport = ({ levellogInfo }: LevellogReportProps) => {
  return (
    <S.Container>
      <UiViewer content={levellog} />
    </S.Container>
  );
};

interface LevellogReportProps {
  levellogInfo: LevellogInfoType;
}

const S = {
  Container: styled.div`
    overflow: auto;
    width: 100%;
    height: 60rem;
    padding: 1rem;
    border-radius: 0.5rem;
    background-color: ${(props) => props.theme.default.WHITE};
    @media (max-width: 520px) {
      flex-direction: column;
      min-height: 0;
    }
  `,
};

export default LevellogReport;
