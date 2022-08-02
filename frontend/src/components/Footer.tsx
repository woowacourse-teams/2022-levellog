import { Link } from 'react-router-dom';

import styled from 'styled-components';

import levellogLogo from 'assets/images/levellogLogo.png';

import { LogoStyle } from 'components/@commons/Style';

const Footer = () => {
  return (
    <S.Container>
      <S.Content>
        <S.Title>@2022 Level Log</S.Title>
        <Link to={'/'}>
          <LogoStyle src={levellogLogo} alt={'레벨로그 로고'} />
        </Link>
      </S.Content>
    </S.Container>
  );
};

const S = {
  Container: styled.footer`
    width: 100%;
    height: 9rem;
    margin-top: 7.125rem;
    border-top: 1px solid ${(props) => props.theme.default.BLACK};
  `,

  Content: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-top: 1rem;
  `,

  Title: styled.p`
    margin-bottom: 0.3125rem;
    font-size: 1.25rem;
    font-weight: 600;
  `,
};

export default Footer;
