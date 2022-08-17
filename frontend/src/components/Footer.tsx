import styled from 'styled-components';

const Footer = () => {
  return (
    <S.Container>
      <S.Title>@2022 Level Log</S.Title>
    </S.Container>
  );
};

const S = {
  Container: styled.footer`
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 70px;
    margin-top: 7.125rem;
    border-top: 0.125rem solid ${(props) => props.theme.default.LIGHT_GRAY};
  `,

  Title: styled.p`
    font-size: 1.25rem;
    font-weight: 300;
  `,
};

export default Footer;
