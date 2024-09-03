
# 6조 ThreeK


## 목차

1. [서비스 소개](#서비스-소개)
2. [기술 스택](#기술-스택)
3. [핵심 기능](#핵심-기능)
4. [프로젝트 구성도](#프로젝트-구성도)
5. [개발 팀 소개](#개발-팀-소개)
6. [개발 기간](#개발-기간)
7. [실행방법](#실행-방법)
## 서비스 소개

배달 음식 주문 관리 플랫폼(백엔드 서버)을 개발하였습니다.

1. 누구나
- 회원가입, 로그인을 통해 고객 권한을 부여 받아 서비스를 정상적으로 이용할 수 있습니다.
- 가게와 상품을 검색할 수 있습니다.
2. 고객(CUSTOMER)
- 인증된 사용자로서 가게의 상품을 고르고 주문할 수 있습니다.
- 요청한 주문을 결제할 수 있습니다.
3. 사장(OWNER)
- 본인 명의의 가게를 등록할 수 있습니다.
- 가게에 상품을 등록할 수 있습니다.
3. 매니저(MANAGER)
- 각종 서비스의 관리자(admin) 기능 접근
- 공지사항 작성 및 제공
4. 마스터(MASTER)
- 매니저 권한 부여
5. 각 역할들은 하위 권한에 있는 모든 기능들을 포함하여 사용 가능합니다.<br>(MASTER-> MANAGER-> OWNER-> CUSTOMER)


## 기술 스택

### Back-end

| <img src="https://profilinator.rishav.dev/skills-assets/java-original-wordmark.svg" alt="Java" width="70px" height="70px" /> | <img src="https://www.seekpng.com/png/full/142-1425436_spring-boot.png" alt="Spring-Boot" width="160px" height="70px" /> | <img src="https://velog.velcdn.com/images/junyoungs7/post/868647fc-b2e6-439a-8260-6956b734fd2c/image.png" alt="Spring-Boot" width="70px" height="70px" /> | 
|:----------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------:|
|                                                             Java                                                             |                                                       Spring-Boot                                                       |                                Spring Cloud                                 |

| <img src="https://seeklogo.com/images/J/jwt-logo-65D86B4640-seeklogo.com.png" alt="jwt" width="70px" height="70px" /> | <img src="https://img1.daumcdn.net/thumb/R800x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fzeffk%2Fbtrc3pNddy4%2FbTtid8MLs8HPjAmADYDfa0%2Fimg.png" alt="SpringSecurity" width="70px" height="70px"> | <img src="https://static-00.iconduck.com/assets.00/swagger-icon-512x512-halz44im.png" alt="QueryDSL" width="70px" height="70px"> |
|:---------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------:|
|                                                          JWT                                                          |                                                                                                     SpringSecurity                                                                                                     |                                                             Swagger                                                              |

| <img src="https://minkukjo.github.io/assets//img/spring-data-logo.png" alt="JPA" width="70px" height="70px"> | <img src="https://blog.kakaocdn.net/dn/xdWQ8/btrPIFeXOCO/pkEovWQcHWznekbkRYC43k/img.png" alt="QueryDSL" width="70px" height="70px"> | 
|:------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------:|
|                                               Spring Data JPA                                                |                                                              QueryDSL                                                               |

### Database

| <img src="https://seeklogo.com/images/P/postgresql-logo-5309879B58-seeklogo.com.png" alt="Postgresql" width="70px" height="70px"> | <img src="https://profilinator.rishav.dev/skills-assets/redis-original-wordmark.svg" alt="Redis" width="70px" height="70px"> |
|:---------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------:|
|                                                            PostgreSQL                                                             |                                                            Redis                                                             |


### Deploy

| <img src="https://seeklogo.com/images/A/aws-ec2-elastic-compute-cloud-logo-2F9E73DBA5-seeklogo.com.png" alt="EC2" height="50px" width="50px" /> | <img src="https://profilinator.rishav.dev/skills-assets/docker-original-wordmark.svg" alt="docker" width="50px" height="50px" /> |
| :---: | :---: |
| EC2 | Docker |

|<img src="https://profilinator.rishav.dev/skills-assets/git-scm-icon.svg" alt="Git" height="50px" width="50px" />  | <img src="https://avatars.githubusercontent.com/u/44036562?s=200&v=4" alt="Github Actions" height="70px" width="70px"> | 
| :---: | :---: |
| Git | Github Actions |

## 핵심 기능

|     기능명     |                 기능 설명                  |
|:-----------:|:--------------------------------------:|
| 로그인 / 회원가입  |  로그인 후 부여 된 권한을 바탕으로 기능을 이용할 수 있습니다.   |
|    검색 기능    | 가게, 상품, 주문, 결제를 각각의 파라미터로 검색 할 수 있습니다. |
|    상품 주문    |      사용자는 상품을 골라 주문을 생성할 수 있습니다.       |
|    주문 결제    |          주문을 결제하여 가게에 전달합니다.           |
|    가게 관리    |    가게 주인은 본인의 가게를 플랫폼에 등록할 수 있습니다.     |
|    상품 관리    |     본인의 가게에 판매할 음식 상품을 등록할 수 있습니다.     |
|    주문 관리    |         주문을 조회 하고 상태 별로 관리합니다.         |
|    결제 관리    |          결제 내역을 조회 할 수 있습니다.           |
| 상품 설명 AI 생성 |  상품 설명 작성 시 AI API로 연동되어 내용을 추천받습니다.   |
|   관리자 모드    |            권한 부여 및 전체 권한 보유            |



## 프로젝트 구성도

|                           인프라 설계도(Infrastructure Diagram)                            |
|:------------------------------------------------------------------------------------:|
| <image src = "https://i.postimg.cc/PrZLKSZp/Infra.png" width="800px" height="500px"> |

|                       개체-관계 모델(ERD)                       |
|:---------------------------------------------------------:|
| <image src="https://i.postimg.cc/CLW1qkQh/Erd.png" width="800px" height="500px"> |

## 개발 팀 소개

| <image src="https://avatars.githubusercontent.com/u/105557972?v=4" width="100px" height="100px"> | <image src="https://avatars.githubusercontent.com/u/50134759?v=4" width="100px" height="100px"> | <image src="https://avatars.githubusercontent.com/u/97355232?v=4" width="100px" height="100px"> | 
|:-----------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------:| 
|[강태원](https://github.com/rivertw777) <br>팀장 <br>(유저, 인증, 서버)|                   [김지희](https://github.com/mii2026) <br>(주문, 결제, 공지사항 / 배포)              |                     [김민철](https://github.com/kmc198989) <br>(가게, 상품, 리뷰)                |

## 개발 기간

24.08.22~24.09.02 (12일)

## 실행 방법

### 사전 요구 사항

1. **Java 17** 설치
2. **PostgreSQL** 설치 및 실행
3. **Redis** 설치 및 실행
4. 구글 api key를 application.properties 파일에 적용

### 데이터베이스 설정
1. CREATE DATABASE 3k;
2. insert into p_locations(name) values ('종로구'); <br>
   insert into p_categories(name) values ('한식');

### API 명세서
https://www.notion.so/Chapter-3-AI-6-91aa84bb6a944c068d0162e36dfbad2a?pvs=4
