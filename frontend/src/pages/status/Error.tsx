import { Exception } from 'pages/status';

import errorImage from 'assets/images/error.webp';

const Error = () => {
  return (
    <Exception>
      <Exception.Image sizes={'EXTRA_HUGE'}>{errorImage}</Exception.Image>
      <Exception.Title>예상치 못한 에러가 발생했습니다!</Exception.Title>
    </Exception>
  );
};

export default Error;
