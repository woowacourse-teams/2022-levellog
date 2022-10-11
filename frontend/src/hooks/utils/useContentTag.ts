import { useState } from 'react';

const useContentTag = () => {
  const [whichContentShow, setWhichContentShow] = useState({ levellog: true, preQuestion: false });

  const handleClickLevellogTag = () => {
    setWhichContentShow({ levellog: true, preQuestion: false });
  };

  const handleClickPreQuestionTag = () => {
    setWhichContentShow({ levellog: false, preQuestion: true });
  };

  return { whichContentShow, handleClickLevellogTag, handleClickPreQuestionTag };
};

export default useContentTag;
