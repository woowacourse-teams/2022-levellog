import styled from 'styled-components';

const ContentHeader = ({ title, children }: InputProps) => {
  return (
    <ContentHeaderStyle>
      <h1>{title}</h1>
      <ButtonBox>{children}</ButtonBox>
    </ContentHeaderStyle>
  );
};

interface InputProps {
  title: string;
  children?: React.ReactElement;
}

const ButtonBox = styled.div`
  position: absolute;
  right: 0;
`;

const ContentHeaderStyle = styled.div`
  position: relative;
  width: 100%;
  height: 120px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export default ContentHeader;
