import styled from 'styled-components';
import { ImageSizeType } from 'types';

import Button from 'components/@commons/button/Button';
import Image from 'components/@commons/image/Image';

const ExceptionContainer = ({ children }: ExceptionContainerProps) => {
  return <S.Container>{children}</S.Container>;
};

const ExceptionErrorTitle = ({ children }: ExceptionTitleProps) => {
  return <S.Title>{children}</S.Title>;
};

const ExceptionErrorText = ({ children }: ExceptionTextProps) => {
  return <S.Text>{children}</S.Text>;
};

const ExceptionImage = ({ sizes, borderRadius = false, children }: ExceptionImage) => {
  return <Image src={children} sizes={sizes} borderRadius={borderRadius} />;
};

const ExceptionButton = ({ children, onClick }: ExceptionButtonProps) => {
  return <S.Button onClick={onClick}>{children}</S.Button>;
};

const ExceptionLoading = () => {
  return <S.Spinner />;
};

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
  sizes: ImageSizeType;
  borderRadius?: boolean;
  children: string;
}

interface ExceptionButtonProps {
  children: string | JSX.Element;
  onClick?: () => void;
}

const S = {
  Container: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 0.875rem;
    height: calc(100vh - 16.25rem);
  `,

  Title: styled.h1`
    font-size: 1.25rem;
  `,

  Button: styled(Button)`
    cursor: pointer;
  `,

  Text: styled.p`
    color: ${(props) => props.theme.default.GRAY};
    font-size: 1rem;
  `,

  Spinner: styled.div`
    width: 6.25rem;
    height: 6.25rem;
    border: 0.5rem solid ${(props) => props.theme.default.LIGHT_GRAY};
    border-top: 0.5rem solid ${(props) => props.theme.default.BLUE};
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

export const Exception = Object.assign(ExceptionContainer, {
  Title: ExceptionErrorTitle,
  Text: ExceptionErrorText,
  Image: ExceptionImage,
  Button: ExceptionButton,
  Loading: ExceptionLoading,
});
