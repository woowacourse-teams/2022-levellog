import styled, { css } from 'styled-components';

const FlexBox = ({ children, ...props }: FlexBoxType) => {
  return <FlexBoxStyle {...props}>{children}</FlexBoxStyle>;
};

type FlexBoxType = {
  children: React.ReactNode;
  flexFlow?: string;
  justifyContent?: string;
  alignItems?: string;
  alignContent?: string;
  gap?: number;
};

const FlexBoxStyle = styled.div`
  ${({
    flexFlow = 'row wrap',
    justifyContent = 'flex-start',
    alignItems = 'stretch',
    alignContent = 'flex-start',
    gap = 0,
  }: Omit<FlexBoxType, 'children'>) => css`
    display: flex;
    flex-flow: ${flexFlow};
    justify-content: ${justifyContent};
    align-items: ${alignItems};
    align-content: ${alignContent};
    gap: ${gap}rem;
  `}
`;

export default FlexBox;
