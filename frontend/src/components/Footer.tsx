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
    bottom: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 70px;
    border-top: 0.125rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    background-color: ${(props) => props.theme.new_default.WHITE};
  `,

  Title: styled.p`
    font-size: 1.25rem;
    font-weight: 300;
  `,
};

export default Footer;
