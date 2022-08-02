import { useState } from 'react';

import axios, { AxiosResponse } from 'axios';

import { requestGetMembers } from 'apis/member';
import { MembersCustomHookType, MemberType } from 'types/member';

const useMember = () => {
  const [members, setMembers] = useState<MemberType[]>([]);
  const [participants, setParticipants] = useState<MemberType[]>([]);
  const accessToken = localStorage.getItem('accessToken');

  const onChangeNickname = async ({ nickname }: MembersCustomHookType) => {
    try {
      if (nickname === '') {
        await setMembers([]);
        return;
      }
      const res = await requestGetMembers({ accessToken, nickname });
      const members = res.data.members.filter((member) =>
        participants.every((participant) => participant.id !== member.id),
      );
      setMembers(members);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const responseBody: AxiosResponse = err.response!;
        if (err instanceof Error) alert(responseBody.data.message);
      }
    }
  };

  const updateParticipants = ({ id, nickname, profileUrl }: MemberType) => {
    const inputtedParticipant = { id, nickname, profileUrl };

    if (participants.every((participant) => inputtedParticipant.id !== participant.id)) {
      setParticipants((prev) => prev.concat(inputtedParticipant));
      console.log('추가');
      return;
    }
    setParticipants(
      participants.filter((participant) => inputtedParticipant.id !== participant.id),
    );
    console.log('삭제');
  };

  return {
    members,
    participants,
    onChangeNickname,
    updateParticipants,
  };
};

export default useMember;
