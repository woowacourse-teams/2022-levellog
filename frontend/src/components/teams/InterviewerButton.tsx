import styled from 'styled-components';

import Button from 'components/@commons/button/Button';
import Image from 'components/@commons/image/Image';

const InterviewerButton = ({
  isDisabled,
  buttonIcon,
  buttonText,
  onClick,
}: InterviewerButtonProps) => {
  return (
    <S.Container disabled={isDisabled} onClick={onClick}>
      <Image src={buttonIcon} sizes={'SMALL'} borderRadius={false} />
      <S.Text>{buttonText}</S.Text>
    </S.Container>
  );
};

interface InterviewerButtonProps {
  isDisabled: boolean;
  buttonIcon: string;
  buttonText: string;
  onClick?: () => void;
}

const S = {
  Container: styled(Button)`
    display: flex;
    align-items: center;
    gap: 1rem;
    padding: 0.625rem 0.75rem;
    border-radius: 2rem;
    background-color: ${(props) => props.theme.default.INVISIBLE};
    font-size: 1.125rem;
    font-weight: 600;
    color: ${(props) =>
      props.disabled ? props.theme.new_default.GRAY : props.theme.new_default.BLACK};
    :hover {
      box-shadow: 0.25rem 0.25rem 0.25rem ${(props) => props.theme.new_default.GRAY};
    }
  `,

  Text: styled.p`
    font-size: 1.25rem;
  `,
};

export default InterviewerButton;
