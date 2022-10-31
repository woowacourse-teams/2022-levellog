import styled from 'styled-components';

import Button from 'components/@commons/button/Button';

const FilterButton = ({ children, isActive = false, ...props }: FilterButtonProps) => {
  return (
    <FilterButtonStyle isActive={isActive} {...props}>
      {children}
    </FilterButtonStyle>
  );
};

interface FilterButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: string;
  isActive?: boolean;
}

const FilterButtonStyle = styled(Button)<{ isActive: boolean }>`
  margin-right: 0.625rem;
  padding: 0.625rem;
  border: 0.0625rem solid ${(props) => props.theme.default.LIGHT_GRAY};
  border-radius: 1.25rem;
  background-color: ${(props) =>
    props.isActive ? props.theme.default.DARK_BLACK : props.theme.default.WHITE};
  font-size: 0.75rem;
  color: ${(props) =>
    props.isActive ? props.theme.default.WHITE : props.theme.default.DARK_BLACK};
  transition: all 0.2s;
  :hover {
    box-shadow: 0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.default.GRAY};
  }
`;

export default FilterButton;
