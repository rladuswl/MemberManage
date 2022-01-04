# 스프링부트 CRUD 회원 관리 시스템 만들기
<hr/>

## 프로젝트 설명
- 스프링부트를 이용하여 만든 회원 관리 시스템입니다.
- 운동시설에서 회원 관리를 수기로 하시는 것을 보고 만들어 봤습니다.
- 회원정보를 입력하고 등록을하면 등급과 종목, 등급별 할인액이 적용되고, 자동으로 남은 일자와 만료기간등을 표시해줍니다.
- 회원관리를 하기 쉽도록 일괄 날짜 증가 및 감소의 기능이 있고, 
- 업로드, 읽기, 수정, 삭제의 기능을 기본으로두고 JPA를 이용해서 Repository를 만들었습니다.

### 사용기술
- 사용기술 : Spring boot, MariaDB

### Dependency
- Spring Web
- Thymeleaf
- Lombok
- Spring Data Jpa
- MariaDb Driver

### application.properties
- application.properties 파일은 resources 디렉토리 안에 만들고 아래 코드 입력
``` spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
  spring.datasource.username= ID 입력
  spring.datasource.password= PW 입력
  spring.datasource.url=jdbc:mysql://localhost:3306/DB스키마명입력
```

### MariaDB - Entity
- Id(PK), grade, name, type, day, endDay, last, money, ++ address, phone

<hr/>

### 프로젝트 정보 및 설명
- 1인 프로젝트로 진행하였습니다.
- 제작기간 : 2021-11-24 ~ 

### 업데이트 내용
- 스프링부트 CRUD 설계 및 Thymeleaf를 이용하여 프로토타입 구현 (2021-11-24)
- 부트스트랩을 이용한 프론트엔드 구현 (2021-11-27)
- 전화번호 및 주소 DB에 추가 (2021-11-29)
- 등록일과 잔여일 계산하여 남은 일자 표시 (2021-11-29)
- 지불금액 회원등급에 따라서 할인 적용 (2021-12-01)
- 전체 회원 기간 연장 기능 제작 (2021-12-02)
- 잔여일수 0일 남았을 때 명단에서 회원 삭제 (2021-12-04)


### 문제점
- 잔여일수를 현재 날짜 기준으로 줄여야한다. (2021-11-29 해결)<br>
-> 잔여일수 = 마감일자 - 오늘일자<br>


- DB에서 ai를 걸어서 회원을 삭제하면 id 값이 하나씩 밀림


- 로그인 기능 도입 (미완성) <br>


- 관리자 페이지 만들기 (미완성) <br>
