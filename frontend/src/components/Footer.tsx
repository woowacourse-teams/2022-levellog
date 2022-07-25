import { Link } from 'react-router-dom';

import styled from 'styled-components';

import levellogLogo from 'assets/images/levellogLogo.png';

import { LogoStyle } from './@commons/Style';

const Footer = () => {
  return (
    <FooterContainer>
      <FooterContent>
        <FooterTitle>@2022 Level Log</FooterTitle>
        <Link to="/">
          <LogoStyle src={levellogLogo} alt="레벨로그 로고" />
        </Link>
      </FooterContent>
    </FooterContainer>
  );
};

const FooterContainer = styled.footer`
  width: 100%;
  height: 9rem;
  border-top: 1px solid ${(props) => props.theme.default.BLACK};
  margin-top: 7.125rem;
`;

const FooterContent = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 1rem;
`;

const FooterTitle = styled.p`
  margin-bottom: 0.3125rem;
  font-size: 1.25rem;
  font-weight: 600;
`;

export default Footer;
