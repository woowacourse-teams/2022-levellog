import styled from 'styled-components';

import Button from 'components/@commons/Button';

const FilterButton = ({ children, isActive, ...props }: FilterButtonProps) => {
  return (
    <FilterButtonStyle isActive={isActive} {...props}>
      {children}
    </FilterButtonStyle>
  );
};

interface FilterButtonProps {
  children: string;
  isActive?: boolean;
  [props: string]: any;
}

// 나중에 조건부 코드 리팩터링 해야함
const FilterButtonStyle = styled(Button)<{ isActive: boolean }>`
  padding: 0.625rem;
  border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  border-radius: 1.25rem;
  background-color: ${(props) =>
    props.isActive ? props.theme.new_default.DARK_BLACK : props.theme.new_default.WHITE};
  font-size: 0.75rem;
  color: ${(props) =>
    props.isActive ? props.theme.new_default.WHITE : props.theme.new_default.DARK_BLACK};
  transition: all 0.2s;
  :hover {
    box-shadow: 0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.new_default.GRAY};
  }
`;

export default FilterButton;
