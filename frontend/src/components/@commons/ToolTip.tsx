import styled from 'styled-components';

const ToolTip = ({ toolTipText }: ToolTipProps) => {
  return (
    <ToolTipStyle>
      <ToolTipTextBox>
        <span>i</span>
      </ToolTipTextBox>
      <ToolTipContent>{toolTipText}</ToolTipContent>
    </ToolTipStyle>
  );
};

interface ToolTipProps {
  toolTipText: string;
}

const ToolTipContent = styled.div`
  position: absolute;
  top: 0;
  left: 2rem;
  padding: 0.375rem;
  width: 12.5rem;
  min-height: 3.125rem;
  border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  border-radius: 0.5rem;
  background-color: ${(props) => props.theme.new_default.LIGHT_GRAY};
  box-shadow: 0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.new_default.GRAY};
  line-height: 1.25rem;
  opacity: 0;
  transition: all 0.5s;
  white-space: normal;
  &:before {
    position: absolute;
    top: 12%;
    left: -0.4375rem;
    border-width: 0.25rem 0.375rem 0.25rem 0;
    border-style: solid;
    border-color: ${(props) =>
      `${props.theme.default.INVISIBLE} ${props.theme.new_default.LIGHT_GRAY}`};
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

const ToolTipTextBox = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 0.25rem;
  width: 1.5rem;
  height: 1.5rem;
  border: 0.0625rem solid ${(props) => props.theme.new_default.GRAY};
  border-radius: 0.75rem;
  color: ${(props) => props.theme.new_default.GRAY};
  font-weight: 700;
`;

export default ToolTip;
