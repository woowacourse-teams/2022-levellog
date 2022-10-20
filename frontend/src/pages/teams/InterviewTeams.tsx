import { Suspense } from 'react';
import { useNavigate } from 'react-router-dom';

import styled from 'styled-components';

import useTeamsCondition from 'hooks/team/useTeamsCondition';
import useSnackbar from 'hooks/utils/useSnackbar';

import Loading from 'pages/status/Loading';

import plusIcon from 'assets/images/plus.svg';
import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import Teams from '../../components/teams/Teams';
import TeamFilterButtons from './TeamFilterButtons';
import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import Image from 'components/@commons/Image';

const InterviewTeams = () => {
  const {
    teamsCondition,
    handleClickCloseTeamsButton,
    handleClickMyTeamsButton,
    handleClickOpenTeamsButton,
  } = useTeamsCondition();
  const { showSnackbar } = useSnackbar();
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const handleClickTeamAddButton = () => {
    if (accessToken) {
      navigate(ROUTES_PATH.INTERVIEW_TEAMS_ADD);
      return;
    }

    showSnackbar({ message: MESSAGE.NEED_LOGIN_SERVICE });
  };

  return (
    <>
      <ContentHeader title={'인터뷰 팀'}>
        <TeamFilterButtons
          teamsCondition={teamsCondition}
          handleClickOpenTeamsButton={handleClickOpenTeamsButton}
          handleClickCloseTeamsButton={handleClickCloseTeamsButton}
          handleClickMyTeamsButton={handleClickMyTeamsButton}
        />
        <span />
      </ContentHeader>
      <S.Container>
        <Suspense fallback={<Loading />}>
          <Teams teamsCondition={teamsCondition} />
        </Suspense>
        <S.TeamAddButton
          aria-label={'팀 추가하기 페이지로 이동'}
          onClick={handleClickTeamAddButton}
        >
          {'팀 추가하기'}
          <S.ImageBox aria-hidden={true}>
            <Image src={plusIcon} sizes={'TINY'} />
          </S.ImageBox>
        </S.TeamAddButton>
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

  Empty: styled.div`
    display: flex;
    flex-direction: column;
    gap: 3.125rem;
    @media (max-width: 560px) {
      justify-content: center;
      gap: 2.5rem;
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
    bottom: 5rem;
    z-index: 10;
    width: 8.125rem;
    height: 3.125rem;
    margin: 0 auto;
    border-radius: 2rem;
    background-color: ${(props) => props.theme.new_default.DARK_BLACK};
    font-size: 1rem;
  `,

  ImageBox: styled.div`
    margin: 0 0 0.0625rem 0.3125rem;
  `,
};

export default InterviewTeams;
