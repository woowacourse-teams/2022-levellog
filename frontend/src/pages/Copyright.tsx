import styled from 'styled-components';

import DefaultProfile from 'assets/images/defaultProfile.webp';
import Error from 'assets/images/error.webp';
import Feedback from 'assets/images/feedback.webp';
import InterviewQuestion from 'assets/images/interviewQuestion.webp';
import InterviewBoth from 'assets/images/interviewboth.webp';
import Interviewee from 'assets/images/interviewee.webp';
import Interviewer from 'assets/images/interviewer.webp';
import SearchFail from 'assets/images/searchFail.webp';

import Image from 'components/@commons/Image';

const Copyright = () => {
  return (
    <S.Container>
      <S.IconBox>
        <Image src={Feedback} sizes={'LARGE'} />
        <S.IconLink
          href="https://www.flaticon.com/free-icons/handwriting"
          title="handwriting icons"
        >
          Handwriting icons created by Freepik - Flaticon
        </S.IconLink>
      </S.IconBox>
      <S.IconBox>
        <Image src={InterviewQuestion} sizes={'LARGE'} />
        <S.IconLink href="https://www.flaticon.com/free-icons/version" title="version icons">
          Version icons created by Freepik - Flaticon
        </S.IconLink>
      </S.IconBox>
      <S.IconBox>
        <Image src={Error} sizes={'LARGE'} />
        <S.IconLink
          href="https://www.flaticon.com/free-icons/traffic-cone"
          title="traffic cone icons"
        >
          Traffic cone icons created by Freepik - Flaticon
        </S.IconLink>
      </S.IconBox>
      <S.IconBox>
        <Image src={DefaultProfile} sizes={'LARGE'} />
        <S.IconLink href="https://www.flaticon.com/free-icons/user" title="user icons">
          User icons created by Flat Icons - Flaticon
        </S.IconLink>
      </S.IconBox>
      <S.IconBox>
        <Image src={Interviewee} sizes={'LARGE'} />
        <S.IconLink href="https://www.flaticon.com/free-icons/interview" title="interview icons">
          Interview icons created by Freepik - Flaticon
        </S.IconLink>
      </S.IconBox>
      <S.IconBox>
        <Image src={InterviewBoth} sizes={'LARGE'} />
        <S.IconLink href="https://www.flaticon.com/free-icons/mutual" title="mutual icons">
          Mutual icons created by Freepik - Flaticon
        </S.IconLink>
      </S.IconBox>
      <S.IconBox>
        <Image src={Interviewer} sizes={'LARGE'} />
        <S.IconLink href="https://www.flaticon.com/free-icons/group" title="group icons">
          Group icons created by DinosoftLabs - Flaticon
        </S.IconLink>
      </S.IconBox>
      <S.IconBox>
        <Image src={SearchFail} sizes={'LARGE'} />
        <S.IconLink
          href="https://www.flaticon.com/free-icons/page-not-found"
          title="page not found icons"
        >
          Page not found icons created by Freepik - Flaticon
        </S.IconLink>
      </S.IconBox>
    </S.Container>
  );
};

const S = {
  Container: styled.div`
    display: flex;
    flex-direction: column;
    max-width: 100rem;
    @media (min-width: 1620px) {
      margin: 2rem calc((100vw - 100rem) / 2);
    }
    @media (max-width: 1620px) {
      margin: 2rem 1.25rem;
    }
  `,

  IconBox: styled.div`
    display: flex;
    align-items: center;
    margin-bottom: 0.625rem;
  `,

  IconLink: styled.a`
    margin-left: 0.3125rem;
    font-size: 16px;
    font-weight: 600;
  `,
};

export default Copyright;
