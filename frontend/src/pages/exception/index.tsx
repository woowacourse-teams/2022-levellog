import styled from 'styled-components';

import Button from 'components/@commons/Button';
import Image from 'components/@commons/Image';

const S = {
  Container: styled.div`
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 0.875rem;
  `,

  Title: styled.h1`
    font-size: 1.25rem;
  `,

  Text: styled.p`
    font-size: 1rem;
    color: ${(props) => props.theme.new_default.GRAY};
  `,

  Spinner: styled.div`
    width: 6.25rem;
    height: 6.25rem;
    border: 0.5rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    border-top: 0.5rem solid ${(props) => props.theme.new_default.BLUE};
    border-radius: 3.125rem;
    animation: spinner 1.5s linear infinite;
    @keyframes spinner {
      0% {
        transform: rotate(0deg);
      }
      100% {
        transform: rotate(360deg);
      }
    }
  `,
};

const ExceptionContainer = ({ children }: ExceptionContainerProps) => {
  return <S.Container>{children}</S.Container>;
};

const ExceptionErrorTitle = ({ children }: ExceptionTitleProps) => {
  return <S.Title>{children}</S.Title>;
};

const ExceptionErrorText = ({ children }: ExceptionTextProps) => {
  return <S.Text>{children}</S.Text>;
};

const ExceptionImage = ({ children }: ExceptionImage) => {
  return <Image src={children} sizes={'EXTRA_HUGE'} />;
};

const ExceptionButton = ({ children }: ExceptionButtonProps) => {
  return <Button>{children}</Button>;
};

const ExceptionLoading = () => {
  return <S.Spinner />;
};

export const Exception = Object.assign(ExceptionContainer, {
  Title: ExceptionErrorTitle,
  Text: ExceptionErrorText,
  Image: ExceptionImage,
  Button: ExceptionButton,
  Loading: ExceptionLoading,
});

interface ExceptionContainerProps {
  children: JSX.Element | JSX.Element[];
}

interface ExceptionTitleProps {
  children: string;
}

interface ExceptionTextProps {
  children: string;
}

interface ExceptionImage {
  children: string;
}

interface ExceptionButtonProps {
  children: string;
}
