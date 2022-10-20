import styled from 'styled-components';

import { GITHUB_AVATAR_SIZE_LIST } from 'constants/constants';

import Button from 'components/@commons/Button';
import Image from 'components/@commons/Image';
import { MemberType } from 'types/member';

const AddMember = ({ addMember, removeEvent }: ParticipantProps) => {
  const { id, nickname, profileUrl } = addMember;

  const handleClickDeleteButton = () => {
    removeEvent({ id, nickname, profileUrl });
  };

  return (
    <S.Container>
      <Image src={profileUrl} sizes={'SMALL'} githubAvatarSize={GITHUB_AVATAR_SIZE_LIST.SMALL} />
      <S.Nickname>{nickname}</S.Nickname>
      <S.DeleteButton onClick={handleClickDeleteButton}>X</S.DeleteButton>
    </S.Container>
  );
};

interface ParticipantProps {
  addMember: MemberType;
  removeEvent: ({ id, nickname, profileUrl }: MemberType) => void;
}

const S = {
  Container: styled.div`
    display: flex;
    align-items: center;
    gap: 0.25rem;
    padding: 0.5rem;
    box-shadow: 0.25rem 0.25rem 0.25rem ${(props) => props.theme.new_default.GRAY};
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    border-radius: 1.5625rem;
    background-color: ${(props) => props.theme.new_default.WHITE};
    font-size: 1rem;
  `,

  Nickname: styled.p`
    font-size: 1rem;
    font-weight: 600;
  `,

  DeleteButton: styled(Button)`
    background-color: ${(props) => props.theme.new_default.WHITE};
    border: none;
    padding: 0 0.125rem 0 0.125rem;
    color: ${(props) => props.theme.new_default.RED};
  `,
};

export default AddMember;
