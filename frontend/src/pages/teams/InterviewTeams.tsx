import { useEffect } from 'react';
import { Link } from 'react-router-dom';

import styled from 'styled-components';

import useSnackbar from 'hooks/useSnackbar';
import useTeams from 'hooks/useTeams';

import plusIcon from 'assets/images/plus.svg';
import { ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FilterButton from 'components/@commons/FilterButton';
import Image from 'components/@commons/Image';
import Snackbar from 'components/@commons/Snackbar';
import SnackbarContainer from 'components/SnackbarContainer';
import InterviewTeam from 'components/teams/InterviewTeam';
import { InterviewTeamType } from 'types/team';

const InterviewTeams = () => {
  const { teams, getTeams, handleClickInterviewGroup } = useTeams();
  const { snackbars, showSnackbar } = useSnackbar();

  const handleOnClick = () => {
    console.log('handleOnClick');
    showSnackbar({ message: '' });
  };

  useEffect(() => {
    getTeams();
  }, []);

  return (
    <>
      <button onClick={handleOnClick}>추가</button>
      <SnackbarContainer>
        {snackbars.map((snackbar, index) => (
          <Snackbar key={index}>{snackbar}</Snackbar>
        ))}
      </SnackbarContainer>

      <ContentHeader title={'인터뷰 팀'}>
        <div>
          <FilterButton isActive={true}>진행중인 인터뷰</FilterButton>
          <FilterButton>종료된 인터뷰</FilterButton>
          <FilterButton>나의 인터뷰</FilterButton>
        </div>
        <span />
      </ContentHeader>
      <S.Container>
        <S.Content onClick={handleClickInterviewGroup}>
          {teams.map((team: InterviewTeamType) => (
            <InterviewTeam key={team.id} {...team} />
          ))}
        </S.Content>
        <Link to={ROUTES_PATH.INTERVIEW_TEAMS_ADD}>
          <S.TeamAddButton>
            {'팀 추가하기'}
            <S.ImageBox>
              <Image src={plusIcon} sizes={'TINY'} />
            </S.ImageBox>
          </S.TeamAddButton>
        </Link>
      </S.Container>
    </>
  );
};

const S = {
  Container: styled.main`
    overflow: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    @media (min-width: 1620px) {
      padding: 0 calc((100vw - 100rem) / 2);
      padding-bottom: 6.25rem;
    }
    @media (min-width: 1187.5px) and (max-width: 1620px) {
      padding: 0 calc((100vw - 74.2188rem) / 2);
      padding-bottom: 6.25rem;
    }
    @media (min-width: 775px) and (max-width: 1207.5px) {
      padding: 0 calc((100vw - 48.4375rem) / 2);
      padding-bottom: 6.25rem;
    }
    @media (min-width: 560px) and (max-width: 800px) {
      padding: 0 calc((100vw - 22.6563rem) / 2);
      padding-bottom: 6.25rem;
    }
    @media (max-width: 560px) {
      padding: 0 1.25rem;
      padding-bottom: 6.25rem;
    }
  `,

  Content: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 3.125rem;
    @media (max-width: 560px) {
      justify-content: center;
      gap: 2.5rem;
    }
  `,

  TeamAddButton: styled(Button)`
    display: flex;
    justify-content: center;
    align-items: center;
    position: fixed;
    left: 0;
    right: 0;
    bottom: 6.875rem;
    z-index: 10;
    width: 8.125rem;
    height: 3.125rem;
    margin: 0 auto;
    border-radius: 2rem;
    background-color: ${(props) => props.theme.new_default.DARK_BLACK};
    font-size: 1rem;
  `,

  ImageBox: styled.div`
    margin: 0 0 1px 5px;
  `,
};

export default InterviewTeams;
