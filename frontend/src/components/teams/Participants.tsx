import styled from 'styled-components';

import useUser from 'hooks/useUser';

import Button from 'components/@commons/Button';
import Image from 'components/@commons/Image';
import { MemberType } from 'types/member';

const Participant = ({ participant, updateParticipants }: ParticipantProps) => {
  const { loginUserId } = useUser();
  const { id, nickname, profileUrl } = participant;

  const handleClickDeleteButton = () => {
    updateParticipants({ id, nickname, profileUrl });
  };

  return (
    <S.Participant>
      <Image src={profileUrl} sizes={'SMALL'} />
      <S.Nickname>{nickname}</S.Nickname>
      {loginUserId !== id && <S.DeleteButton onClick={handleClickDeleteButton}>X</S.DeleteButton>}
    </S.Participant>
  );
};

interface ParticipantProps {
  participant: MemberType;
  updateParticipants: ({ id, nickname, profileUrl }: MemberType) => void;
}

const S = {
  Participant: styled.div`
    display: flex;
    align-items: center;
    gap: 0.25rem;
    margin-bottom: 10px;
    padding: 0.5rem;
    box-shadow: 4px 4px 4px ${(props) => props.theme.default.GRAY};
    border-style: none;
    border-radius: 1.5625rem;
    background-color: ${(props) => props.theme.default.WHITE};
    font-size: 1rem;
  `,

  Nickname: styled.p`
    font-size: 1rem;
    font-weight: 600;
  `,

  DeleteButton: styled(Button)`
    background-color: ${(props) => props.theme.default.WHITE};
    border: none;
    padding: 0 2px 0 2px;
    color: ${(props) => props.theme.default.RED};
  `,
};

export default Participant;
