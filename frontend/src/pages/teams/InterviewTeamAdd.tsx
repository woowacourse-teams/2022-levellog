import React, { useState } from 'react';
import { useEffect } from 'react';

import styled from 'styled-components';

import useMember from 'hooks/useMember';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import Input from 'components/@commons/Input';
import Member from 'components/teams/Member';
import { MemberType } from 'types/member';

const InterviewTeamAdd = () => {
  const { members, participants, onChangeNickname, onClickMember } = useMember();
  const [nickname, setNickname] = useState('');

  const handleSubmitTeamAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault;
  };

  const handleChangeInput = async (e: React.ChangeEvent<HTMLInputElement>) => {
    await setNickname(e.target.value);
  };

  useEffect(() => {
    onChangeNickname({ nickname });
  }, [nickname]);

  useEffect(() => {
    console.log(participants);
  }, [participants]);

  return (
    <S.Container onSubmit={handleSubmitTeamAddForm}>
      <ContentHeader title={'인터뷰 팀 생성하기'}>
        <Button type={'submit'}>만들기</Button>
      </ContentHeader>
      <S.FormContainer>
        <S.InputContainer>
          <S.Label>제목</S.Label>
          <S.Input minLength={'5'} maxLength={'10'} required />
        </S.InputContainer>
        <S.InputContainer>
          <S.Label>장소</S.Label>
          <S.Input minLength={'3'} maxLength={'10'} required />
        </S.InputContainer>
        <S.InputContainer>
          <S.Label>날짜</S.Label>
          <S.Input type={'date'} required />
        </S.InputContainer>
        <S.InputContainer>
          <S.Label>시간</S.Label>
          <S.Input type={'time'} required />
        </S.InputContainer>
        <S.InputContainer>
          <S.Label>인터뷰어의 수</S.Label>
          <S.Input type={'number'} required />
        </S.InputContainer>
        <S.InputContainer>
          <S.Label>참가자</S.Label>
          {participants.length > 0 && (
            <S.ParticipantsBox>
              {participants.map((participant: MemberType) => (
                <S.Participant key={participant.id}>{participant.nickname}</S.Participant>
              ))}
            </S.ParticipantsBox>
          )}
          <S.Input value={nickname} onChange={handleChangeInput} required />
          {members.length > 0 && (
            <S.MembersBox>
              {members.map((member: MemberType) => (
                <Member
                  key={member.id}
                  member={member}
                  setNickname={setNickname}
                  onClickMember={onClickMember}
                />
              ))}
            </S.MembersBox>
          )}
        </S.InputContainer>
      </S.FormContainer>
    </S.Container>
  );
};

const S = {
  Container: styled.form``,

  FormContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1.5rem;
  `,

  InputContainer: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
  `,

  Label: styled.label`
    font-size: 1rem;
    margin-bottom: 1rem;
  `,

  Input: styled(Input)`
    width: 40.625rem;
    height: 1.125rem;
  `,

  Participant: styled.div`
    border-style: none;
    font-size: 1rem;
  `,

  ParticipantsBox: styled.div`
    display: flex;
    gap: 1rem;
  `,

  MembersBox: styled.div`
    box-sizing: content-box;
    width: 40.625rem;
    height: fit-content;
    padding: 1rem;
    border: 0.0625rem solid ${(props) => props.theme.default.GRAY};
    border-radius: 0.3125rem;
    background-color: ${(props) => props.theme.default.WHITE};
    font-size: 1.2rem;
  `,

  Member: styled.button`
    width: 100%;
    height: 1rem;
    border-style: none;
    background-color: ${(props) => props.theme.default.WHITE};
    font-size: 1.2rem;
  `,
};

export default InterviewTeamAdd;
