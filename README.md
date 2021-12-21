# Auth
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
- `/application.yml` 템플릿을 `/integrated/src/main/resources/application.yml`으로 복사, `_here`으로 끝나는 항목을 개인 환경에 따라 수정
- `personal-project.url.auth`와 `personal-project.url.admin`은 Spring Boot server url과 동일
- 설정 후 아래 명령어대로 build & run (Windows는 `gradlew.bat`으로 build)
```
~/integrated > ./gradlew build
~/integrated > java -jar ./build/libs/integrated-0.0.1-SNAPSHOT.jar
```

## Architecture
![](https://github.com/njsh4261/auth/blob/main/architecture_simple.drawio.png)
