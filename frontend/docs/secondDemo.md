## 프론트엔드 2차 데모데이 기능 요구사항

- [ ] 로그인을 할 수 있어야 한다.

  - [ ] 로그인 버튼을 클릭하면 github로부터 code를 받아와야 한다.
  - [ ] 받아온 code를 서버로 요청보내서 access token과 profileUri를 받아와야 한다.
  - [ ] 받은 access token은 localStorage에 저장해야 한다.

- [ ] 로그인을 하면 서버로부터 받아온 profileUri를 통해 프로필 사진을 보여줘야 한다.

- [ ] 받아온 profileUri가 에러가 발생한다면 default 이미지를 보여줘야 한다.

- [ ] 로그아웃을 할 수 있어야 한다.
  - [ ] 로그아웃 버튼을 클릭하면 localStorage에 있는 access token은 삭제되어야 한다.
