import styled from 'styled-components';

const ToolTip = ({ toolTipText }: ToolTipProps) => {
  return (
    <ToolTipStyle>
      <ToolTipText>
        <span>?</span>
      </ToolTipText>
      <ToolTipContent>{toolTipText}</ToolTipContent>
    </ToolTipStyle>
  );
};

interface ToolTipProps {
  toolTipText: string;
}

const ToolTipContent = styled.div`
  position: absolute;
  top: -0.25rem;
  left: 2rem;
  padding: 0.375rem;
  width: 12.5rem;
  min-height: 3.125rem;
  border: 0.0625rem solid ${(props) => props.theme.default.BLACK};
  border-radius: 0.5rem;
  background-color: ${(props) => props.theme.default.WHITE};
  line-height: 1.25rem;
  opacity: 0;
  transition: all 0.5s;
  &:before {
    position: absolute;
    top: 30%;
    left: -0.4375rem;
    border-width: 0.25rem 0.375rem 0.25rem 0;
    border-style: solid;
    border-color: ${(props) => `${props.theme.default.INVISIBLE} ${props.theme.default.BLACK}`};
    transform: translateY(-50%);
    content: '';
  }
  &:after {
    position: absolute;
    top: -2.5rem;
    left: 0;
    width: 0.625rem;
    height: 2.5rem;
    content: '';
  }
`;

const ToolTipStyle = styled.div`
  position: relative;
  &:hover {
    ${ToolTipContent} {
      opacity: 0.99;
    }
  }
`;

const ToolTipText = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 1.5rem;
  height: 1.5rem;
  border: 0.0625rem solid ${(props) => props.theme.default.BLACK};
  border-radius: 0.75rem;
`;

export default ToolTip;
