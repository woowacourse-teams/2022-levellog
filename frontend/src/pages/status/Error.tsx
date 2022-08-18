import { Exception } from 'pages/status';

import NotFoundImage from 'assets/images/free-icon-cone-7564055.webp';

const Error = () => {
  return (
    <Exception>
      <Exception.Image>{NotFoundImage}</Exception.Image>
      <Exception.Title>예상치 못한 에러가 발생했습니다!</Exception.Title>
    </Exception>
  );
};

export default Error;
