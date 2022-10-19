import styled from 'styled-components';

import Button from 'components/@commons/Button';

const BottomBar = ({ buttonText, handleClickRightButton }: BottomBarProps) => {
  return (
    <Container>
      <LeftBox></LeftBox>
      <RightBox>
        <Button onClick={handleClickRightButton}>{buttonText}</Button>
      </RightBox>
    </Container>
  );
};

interface BottomBarProps {
  buttonText: string;
  handleClickRightButton: () => void;
}

const Container = styled.div`
  display: flex;
  position: relative;
  justify-content: space-between;
  align-items: center;
  height: 4.375rem;
  padding: 0.3125rem 0;
`;

const LeftBox = styled.div``;

const RightBox = styled.div`
  display: flex;
  gap: 1rem;
`;

export default BottomBar;
