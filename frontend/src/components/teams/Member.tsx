import { Dispatch, SetStateAction } from 'react';

import styled from 'styled-components';

import { MemberType } from 'types/member';

const Member = ({ member, setNickname, onClickMember }: MemberProps) => {
  const { id, nickname, profileUrl } = member;

  const handleClickMember = () => {
    setNickname('');
    onClickMember({ id, nickname, profileUrl });
  };

  return (
    <S.Member type={'button'} onClick={handleClickMember}>
      {nickname}
    </S.Member>
  );
};

interface MemberProps {
  member: MemberType;
  setNickname: Dispatch<SetStateAction<string>>;
  onClickMember: ({ id, nickname, profileUrl }: MemberType) => void;
}

const S = {
  Member: styled.button`
    width: 100%;
    height: 1rem;
    border-style: none;
    background-color: ${(props) => props.theme.default.WHITE};
    font-size: 1.2rem;
  `,
};

export default Member;
