import styled from 'styled-components';

import { FeedbackContainer, FeedbackTitle } from 'components/@commons/Style';

// 레벨로그 작성한 유저의 닉네임 가져오기
const LevellogReport = ({ levellog }: LevellogReportProps) => {
  return (
    <FeedbackContainer>
      <FeedbackTitle>레벨로그</FeedbackTitle>
      <S.Content>{levellog}</S.Content>
    </FeedbackContainer>
  );
};

interface LevellogReportProps {
  levellog: string;
}

const S = {
  // FeedbackAdd 페이지가 figma와 달라서 FeedbackContainer를 없애고 따로 스타일 작성해야함.
  Container: styled.div`
    overflow: auto;
    width: 47.0625rem;
    @media (max-width: 520px) {
      max-width: 22.875rem;
    }
  `,

  Content: styled.div`
    overflow: hidden;
    width: 100%;
    min-height: 60.0625rem;
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
