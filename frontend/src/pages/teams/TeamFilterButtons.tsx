import styled from 'styled-components';

import useUser from 'hooks/useUser';

import FilterButton from 'components/@commons/FilterButton';
import { TeamConditionsType } from 'types/team';

const TeamFilterButtons = ({
  teamsCondition,
  handleClickOpenTeamsButton,
  handleClickCloseTeamsButton,
  handleClickMyTeamsButton,
}: TeamFilterButtonsProps) => {
  const { loginUserId } = useUser();

  return (
    <S.Container>
      <li>
        <FilterButton
          onClick={handleClickOpenTeamsButton}
          isActive={teamsCondition.open}
          aria-label={'진행중인 인터뷰 팀으로 필터링'}
          aria-disabled={teamsCondition.open}
        >
          진행중인 인터뷰
        </FilterButton>
      </li>
      <li>
        <FilterButton
          onClick={handleClickCloseTeamsButton}
          isActive={teamsCondition.close}
          aria-label={'종료된 인터뷰 팀으로 필터링'}
          aria-disabled={teamsCondition.close}
        >
          종료된 인터뷰
        </FilterButton>
      </li>
      {loginUserId && (
        <li>
          <FilterButton
            onClick={handleClickMyTeamsButton}
            isActive={teamsCondition.my}
            aria-label={'나의 인터뷰 팀 목록으로 필터링'}
            aria-disabled={teamsCondition.my}
          >
            나의 인터뷰
          </FilterButton>
        </li>
      )}
    </S.Container>
  );
};

interface TeamFilterButtonsProps {
  teamsCondition: TeamConditionsType;
  handleClickOpenTeamsButton: () => void;
  handleClickCloseTeamsButton: () => void;
  handleClickMyTeamsButton: () => void;
}

const S = {
  Container: styled.ul`
    display: flex;
    align-items: center;
    gap: 0.625rem;
  `,
};

export default TeamFilterButtons;
