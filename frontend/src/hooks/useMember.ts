import { useState } from 'react';

import axios, { AxiosResponse } from 'axios';

import useUser from 'hooks/useUser';

import useUtil from './useUtil';
import { requestGetMembers } from 'apis/member';
import { MembersCustomHookType, MemberType } from 'types/member';

const useMember = () => {
  const {
    loginUserId: id,
    loginUserNickname: nickname,
    loginUserProfileUrl: profileUrl,
  } = useUser();
  const [members, setMembers] = useState<MemberType[]>([]);
  const [participants, setParticipants] = useState<MemberType[]>([{ id, nickname, profileUrl }]);
  const accessToken = localStorage.getItem('accessToken');
  const { isThrottle } = useUtil();

  const updateMembers = async ({ nickname = '' }: MembersCustomHookType) => {
    try {
      if (isThrottle()) return;
      console.log('요청');
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
      return;
    }
    setParticipants(
      participants.filter((participant) => inputtedParticipant.id !== participant.id),
    );
  };

  return {
    members,
    participants,
    updateMembers,
    updateParticipants,
  };
};

export default useMember;
