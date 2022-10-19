import { Exception } from 'pages/status';

import errorImage from 'assets/images/error.webp';

const EmptyTeams = () => {
  return (
    <Exception>
      <Exception.Image sizes={'EXTRA_HUGE'}>{errorImage}</Exception.Image>
      <Exception.Title>조건에 해당하는 팀이 없습니다!</Exception.Title>
    </Exception>
  );
};

export default EmptyTeams;
