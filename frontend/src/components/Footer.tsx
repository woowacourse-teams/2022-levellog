import { Link } from 'react-router-dom';

import styled from 'styled-components';

import { ROUTES_PATH } from 'constants/constants';

const Footer = () => {
  return (
    <S.Container>
      <S.Title>@ 2022 LevelLog</S.Title>
      <Link to={ROUTES_PATH.COPYRIGHT} aria-label={'저작권 정보 페이지로 이동'}>
        <S.Content>저작권</S.Content>
      </Link>
    </S.Container>
  );
};

const S = {
  Container: styled.footer`
    bottom: 0;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    width: 100%;
    height: 4.375rem;
    border-top: 0.125rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    background-color: ${(props) => props.theme.new_default.WHITE};
    @media (min-width: 1620px) {
      padding: 0 calc((100vw - 100rem) / 2);
    }
    @media (max-width: 1620px) {
      padding: 0 1.25rem;
    }
  `,

  Title: styled.p`
    margin-right: 0.625rem;
    color: ${(props) => props.theme.new_default.DARK_GRAY};
    font-size: 1rem;
    font-weight: 500;
  `,

  Content: styled.p`
    margin-right: 0.625rem;
    color: ${(props) => props.theme.new_default.DARK_GRAY};
    font-size: 1rem;
    font-weight: 500;
    &:hover {
      border-bottom: 1px solid ${(props) => props.theme.new_default.DARK_GRAY};
    }
  `,
};

export default Footer;
