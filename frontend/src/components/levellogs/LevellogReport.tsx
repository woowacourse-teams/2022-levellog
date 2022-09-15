import styled from 'styled-components';

import UiViewer from 'components/@commons/markdownEditor/UiViewer';
import { LevellogInfoType } from 'types/levellog';

const LevellogReport = ({ levellogInfo }: LevellogReportProps) => {
  return (
    <S.Container>
      <UiViewer content={levellogInfo.content} />
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
    height: 100%;
    padding: 1rem;
    border-radius: 0.5rem;
    border: 1px solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    background-color: ${(props) => props.theme.default.WHITE};
    @media (max-width: 520px) {
      flex-direction: column;
    }
  `,
};

export default LevellogReport;
