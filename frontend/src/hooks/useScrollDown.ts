import { useRef } from 'react';

const useScrollDown = () => {
  const scrollRef = useRef<HTMLElement>(null);

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
  requestFunction: () => Promise<void>;
}

export default useScrollDown;
