# SGS_Auth_Personal
- Access Token을 이용한 로그인 기능 구현
- 추가로 개선할 사항: [Todo](https://github.com/njsh4261/auth/issues/7)

## Skills
- Spring Boot
    - JWT 라이브러리 사용하여 Token
    - Spring Security는 사용하지 않음
    - Thymeleaf를 통한 view 제공
- MySQL
    - 사용자 정보 및 권한 정보 저장
- Redis
    - Token을 server side에서 캐싱하는 용도로 사용

## How to Run
- `/integrated/src/main/resources/application.yml`에서 `_here`으로 끝나는 항목을 개인 환경에 따라 수정
- `personal-project.url.auth`와 `personal-project.url.admin`은 Spring Boot server url과 동일
- 설정 후 아래 명령어대로 build & run (Windows는 `gradlew.bat`으로 build)
```
~/integrated > ./gradlew build
~/integrated > java -jar ./build/libs/integrated-0.0.1-SNAPSHOT.jar
```

## Architecture
![architecture](https://user-images.githubusercontent.com/54832818/147417338-6908722b-7f70-41a5-baff-0652e0c29431.png)

## Views
![signin](https://user-images.githubusercontent.com/54832818/147417339-1f2614fc-6d69-430f-a060-a2f94fb85603.png)
- 로그인 페이지
- 이메일 또는 비밀번호가 틀릴 경우 에러 메시지 표시
- 유효한 토큰 인증 정보가 있으면 관리자 페이지로 리다이렉트

![signup](https://user-images.githubusercontent.com/54832818/147417340-e074d122-3658-4437-a481-f16ef5a4cceb.png)
- 회원가입 페이지
- 이미 가입한 이메일로 가입 시도할 경우 에러 메시지 표시
- 유효한 토큰 인증 정보가 있으면 관리자 페이지로 리다이렉트

![admin](https://user-images.githubusercontent.com/54832818/147417336-85bf4793-6fef-4498-b622-7c54ddff6801.png)
- 관리자 페이지
- 회원 목록을 표시
- 현재 접속한 유저가 admin 권한일 경우 각 유저의 이름에 유저 정보 수정 페이지 링크 표시
  - normal user 권한일 경우 링크가 표시되지 않음
- 유효한 토큰 인증 정보가 없으면 로그인 페이지로 리다이렉트

![userpage](https://user-images.githubusercontent.com/54832818/147417341-fe93e16d-262f-41f6-9610-d26bae22d6ee.png)
- 유저 정보 수정 페이지
- 유저의 이메일, 이름, 권한을 수정 가능
- 현재 접속한 유저가 스스로를 삭제하려 하는 경우 관리자 페이지로 리다이렉트
- 현재 접속한 유저가 normal user 권한인 상태로 비정상적으로 접근한 경우 관리자 페이지로 리다이렉트
- 유효한 토큰 인증 정보가 없으면 로그인 페이지로 리다이렉트

## API Doc
| Method | URI                     | Desc                | Body     | 토큰 필요 |
|--------|-------------------------|---------------------|----------|-------|
| GET    | /                       | 로그인 페이지로 리다이렉트      || X        |
| GET    | /signin                 | 로그인 페이지 표시          || X        |
| GET    | /signup                 | 회원가입 페이지 표시         || X        |
| POST   | /auth/signin            | 로그인 요청              | LoginDto | X    |
| POST   | /auth/signup            | 회원가입 요청             | UserDto  | X        |
| GET    | /signout                | 로그아웃 요청             || X        |
| GET    | /admin                  | 관리자 페이지 표시          || O        |
| GET    | /admin/user-view        | 유저 정보 수정 페이지 표시     || O        |
| PUT    | /admin/edit-user/{id}   | {id}에 해당하는 유저 정보 수정 | User     | O        |
| DELETE | /admin/delete-user/{id} | {id}에 해당하는 유저 삭제    || O        |

- LoginDto: 이메일, 비밀번호
- UserDto: 이메일, 비밀번호, 이름
- User: 이메일, 이름, 권한
