import { Link } from 'react-router-dom';

//그룹, 로그인 기능이 없는동안 레벨로그 RUD 체크.
const Home = () => {
  return (
    <>
      <Link to="/interview/teams">
        <button>전체 팀 버튼</button>
      </Link>
    </>
  );
};

export default Home;
