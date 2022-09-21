import styled from 'styled-components';

import DefaultProfile from 'assets/images/defaultProfile.webp';
import Error from 'assets/images/error.webp';
import Feedback from 'assets/images/feedback.webp';
import InterviewQuestion from 'assets/images/interviewQuestion.webp';
import InterviewBoth from 'assets/images/interviewboth.webp';
import Interviewee from 'assets/images/interviewee.webp';
import Interviewer from 'assets/images/interviewer.webp';

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
        <S.IconLink href="https://www.flaticon.com/free-icons/change" title="change icons">
          Change icons created by Freepik - Flaticon
        </S.IconLink>
      </S.IconBox>
      <S.IconBox>
        <Image src={Interviewer} sizes={'LARGE'} />
        <S.IconLink href="https://www.flaticon.com/free-icons/group" title="group icons">
          Group icons created by Freepik - Flaticon
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
    margin: 2rem auto 6.25rem auto;
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
