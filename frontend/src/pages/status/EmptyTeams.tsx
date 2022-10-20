import { Exception } from 'pages/status';

import emptyImage from 'assets/images/empty.webp';

const EmptyTeams = () => {
  return (
    <Exception>
      <Exception.Image sizes={'EXCEPTION'}>{emptyImage}</Exception.Image>
      <Exception.Title>조건에 해당하는 팀이 없습니다!</Exception.Title>
    </Exception>
  );
};

export default EmptyTeams;
