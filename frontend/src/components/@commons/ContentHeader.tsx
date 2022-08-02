import styled from 'styled-components';

const ContentHeader = ({ title, children }: ContentHeaderProps) => {
  return (
    <ContentHeaderStyle>
      <h1>{title}</h1>
      <ButtonBox>{children}</ButtonBox>
    </ContentHeaderStyle>
  );
};

interface ContentHeaderProps {
  title: string;
  children?: React.ReactElement;
}

const ContentHeaderStyle = styled.div`
  display: flex;
  position: relative;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 7.5rem;
  @media (max-width: 560px) {
    justify-content: start;
    font-size: 0.75rem;
  }
`;

const ButtonBox = styled.div`
  position: absolute;
  right: 0;
`;

export default ContentHeader;
