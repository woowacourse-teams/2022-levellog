import { Suspense } from 'react';

import styled from 'styled-components';

import Loading from 'pages/status/Loading';

import { MESSAGE } from 'constants/constants';

import Button from 'components/@commons/button/Button';
import SearchMember from 'components/teams/SearchMember';
import TeamFormInput from 'components/teams/TeamFormInput';
import {
  interviewDateValidate,
  interviewInterviewerValidate,
  interviewLocationValidate,
  interviewTimeValidate,
  interviewTitleValidate,
} from 'utils/validate';

const TeamForm = ({ purpose, teamInfoRef, handleClickTeamButton }: TeamFormProps) => {
  return (
    <S.Container>
      <S.Title>인터뷰 팀 {purpose}</S.Title>
      <TeamFormInput
        label={'제목'}
        errorText={MESSAGE.INTERVIEW_TITLE_VALIDATE_FAIL}
        validate={interviewTitleValidate}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[0] = el)}
      />
      <TeamFormInput
        label={'장소'}
        errorText={MESSAGE.INTERVIEW_LOCATION_VALIDATE_FAIL}
        validate={interviewLocationValidate}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[1] = el)}
      />
      <TeamFormInput
        label={'날짜'}
        type={'date'}
        errorText={MESSAGE.INTERVIEW_DATE_VALIDATE_FAIL}
        validate={interviewDateValidate}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[2] = el)}
      />
      <TeamFormInput
        label={'시간'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[3] = el)}
        type={'time'}
        errorText={MESSAGE.INTERVIEW_TIME_VALIDATE_FAIL}
        validate={interviewTimeValidate}
      />
      <TeamFormInput
        label={'인터뷰어의 수'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[4] = el)}
        type={'number'}
        errorText={MESSAGE.INTERVIEW_INTERVIEWEE_VALIDATE_FAIL}
        validate={interviewInterviewerValidate}
      />
      <Suspense fallback={<Loading />}>
        <SearchMember />
      </Suspense>
      <S.SubmitButton onClick={handleClickTeamButton}>{purpose}</S.SubmitButton>
    </S.Container>
  );
};

interface TeamFormProps {
  purpose: '추가하기' | '수정하기';
  teamInfoRef: React.MutableRefObject<HTMLInputElement[]>;
  handleClickTeamButton: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const S = {
  Container: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1.5rem;
    width: 45.125rem;
    height: max-content;
    margin: 1.25rem auto 1.25rem auto;
    padding: 0 1.875rem 1.875rem 1.875rem;
    border-radius: 1.25rem;
    box-shadow: 0.0625rem 0.25rem 0.625rem ${(props) => props.theme.new_default.GRAY};
    @media (min-width: 560px) and (max-width: 760px) {
      width: 32.5rem;
    }
    @media (max-width: 560px) {
      justify-content: center;
      width: calc(100vw - 2.5rem);
      box-shadow: none;
    }
  `,

  Title: styled.h1`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 6.25rem;
    font-weight: 600;
  `,

  SubmitButton: styled(Button)`
    width: 100%;
    height: 3.125rem;
  `,
};

export default TeamForm;
