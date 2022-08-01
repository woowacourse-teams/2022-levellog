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

  const onClickMember = ({ id, nickname, profileUrl }: MemberType) => {
    const participant = { id, nickname, profileUrl };
    setParticipants((prev) => prev.concat(participant));
  };

  return {
    members,
    participants,
    onChangeNickname,
    onClickMember,
  };
};

export default useMember;
