import styled from 'styled-components';

import { FeedbackContainer, FeedbackTitle } from 'components/@commons/Style';

// 레벨로그 작성한 유저의 닉네임 가져오기
const LevellogReport = ({ levellog }: LevellogReportProps) => {
  return (
    <FeedbackContainer>
      <FeedbackTitle>레벨로그</FeedbackTitle>
      <LevellogContainer>{levellog}</LevellogContainer>
    </FeedbackContainer>
  );
};

interface LevellogReportProps {
  levellog: string;
}

const LevellogContainer = styled.div`
  overflow: hidden;
  width: 100%;
  min-height: 60.0625rem;
  padding: 1rem;
  background-color: ${(props) => props.theme.default.WHITE};
  border-radius: 0.5rem;
  @media (max-width: 520px) {
    max-width: 22.875rem;
    min-height: 0;
    flex-direction: column;
  }
`;

export default LevellogReport;
