import { useRef } from 'react';

const useScrollDown = <T extends HTMLElement>() => {
  const scrollRef = useRef<T>(null);

  const afterRequestScrollDown = ({ requestFunction }: AfterRequestScrollDownProps) => {
    requestFunction().then(() => {
      setTimeout(() => {
        if (scrollRef.current) {
          scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
        }
      }, 0);
    });
  };

  return { scrollRef, afterRequestScrollDown };
};

interface AfterRequestScrollDownProps {
  requestFunction: () => Promise<any>;
}

export default useScrollDown;
