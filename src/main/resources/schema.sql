
-- # 개발환경의 H2와 개발계/운영계 환경의 DB서버가 같은 설정을 유지하도록 해당 파일을 잘 관리해야 함

-- 생성의 반대 순으로 삭제
DROP TABLE FileAttachment IF EXISTS;
DROP TABLE MetaData IF EXISTS;
DROP TABLE Schedule IF EXISTS;
DROP TABLE DormantUser IF EXISTS;
DROP TABLE LeftUser IF EXISTS;
DROP TABLE Menu IF EXISTS;
DROP TABLE Registration IF EXISTS;
DROP TABLE Reply IF EXISTS;
DROP TABLE Article IF EXISTS;
DROP TABLE Board IF EXISTS;
DROP TABLE Organization IF EXISTS;
DROP TABLE User IF EXISTS;


-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- User Table Create SQL
CREATE TABLE User
(
    `idUser`       INT              NOT NULL    AUTO_INCREMENT COMMENT '회원번호',
    `idKakao`      BIGINT           NOT NULL    COMMENT '카톡ID, (카톡로그인을 통해 받아옴)',
    `name`         VARCHAR(10)      NOT NULL    COMMENT '이름',
    `nickname`     VARCHAR(10)      NOT NULL    COMMENT '세례명',
    `dateJoined`   DATETIME         NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '가입일자',
    `accessToken`  VARCHAR(45)      NULL        COMMENT 'accessToken (카톡)',
    `freshToken`   VARCHAR(45)      NULL        COMMENT 'freshToken (카톡)',
    `thumbnail`    VARCHAR(1000)    NULL        COMMENT 'thumbnail',
    PRIMARY KEY (idUser)
);


-- Organization Table Create SQL
CREATE TABLE Organization
(
    `idOrganization`  INT              NOT NULL    AUTO_INCREMENT COMMENT '조직번호',
    `idParent`        INT              NULL        COMMENT '부모 조직번호',
    `name`            VARCHAR(10)      NOT NULL    COMMENT '조직이름',
    `description`     VARCHAR(1000)    NULL        COMMENT '조직정보',
    PRIMARY KEY (idOrganization)
);


-- Board Table Create SQL
CREATE TABLE Board
(
    `idBoard`         INT            NOT NULL    AUTO_INCREMENT COMMENT '게시판번호',
    `name`            VARCHAR(45)    NOT NULL    COMMENT '게시판이름',
    `description`     VARCHAR(45)    NULL        COMMENT '게시판설명',
    `idOrganization`  INT            NOT NULL    COMMENT '단체번호',
    `type`            TINYINT        NOT NULL    COMMENT '게시판타입(일반게시판, 갤러리 등)',
    PRIMARY KEY (idBoard)
);

ALTER TABLE Board ADD CONSTRAINT FK_Board_idOrganization_Organization_idOrganization FOREIGN KEY (idOrganization)
 REFERENCES Organization (idOrganization)  ON DELETE RESTRICT ON UPDATE RESTRICT;


-- Article Table Create SQL
CREATE TABLE Article
(
    `idArticle`        INT               NOT NULL    AUTO_INCREMENT COMMENT '글번호',
    `title`            VARCHAR(45)       NOT NULL    COMMENT '제목',
    `idUser`           INT               NULL        COMMENT '회원번호',
    `idBoard`          INT               NOT NULL    COMMENT '게시판번호',
    `dateCreated`      DATETIME          NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '작성일자',
    `countHit`         INT               NOT NULL    COMMENT '조회 수',
    `contents`         VARCHAR(65535)    NOT NULL    COMMENT '글내용(HTML 로?)',
    `status`           TINYINT           NOT NULL    COMMENT '글 상태(수정됨, 삭제됨, 등)',
    `type`             TINYINT           NOT NULL    COMMENT '글 타입(비밀글, 공지글 등)',
    `articlePassword`  VARCHAR(45)       NULL        COMMENT '글 비밀번호',
    `thumbnail`        VARCHAR(1000)     NULL        COMMENT 'thumbnail',
    PRIMARY KEY (idArticle)
);

ALTER TABLE Article ADD CONSTRAINT FK_Article_idUser_User_idUser FOREIGN KEY (idUser)
 REFERENCES User (idUser)  ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Article ADD CONSTRAINT FK_Article_idBoard_Board_idBoard FOREIGN KEY (idBoard)
 REFERENCES Board (idBoard)  ON DELETE RESTRICT ON UPDATE RESTRICT;


-- Reply Table Create SQL
CREATE TABLE Reply
(
    `idReply`        INT             NOT NULL    AUTO_INCREMENT COMMENT '댓글번호',
    `idArticle`      INT             NOT NULL    COMMENT '글번호',
    `idUser`         INT             NULL        COMMENT '회원번호',
    `idParent`       INT             NULL    COMMENT '부모 댓글번호',
    `dateCreated`    DATETIME        NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '작성일자',
    `contents`       VARCHAR(200)    NOT NULL    COMMENT '댓글',
    `status`         TINYINT         NOT NULL    COMMENT '댓글 상태(작성됨상태, 삭제상태 등등..)',
    `replyPassword`  VARCHAR(45)     NULL        COMMENT '댓글 비밀번호',
    PRIMARY KEY (idReply)
);

ALTER TABLE Reply ADD CONSTRAINT FK_Reply_idArticle_Article_idArticle FOREIGN KEY (idArticle)
 REFERENCES Article (idArticle)  ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Reply ADD CONSTRAINT FK_Reply_idUser_User_idUser FOREIGN KEY (idUser)
 REFERENCES User (idUser)  ON DELETE RESTRICT ON UPDATE RESTRICT;


-- Registration Table Create SQL
CREATE TABLE Registration
(
    `idOrganization`  INT            NOT NULL    COMMENT '단체번호',
    `idUser`          INT            NOT NULL    COMMENT '회원번호',
    `dateRegistered`  DATETIME       NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    `level`           TINYINT        NOT NULL    COMMENT '직책',
    `description`     VARCHAR(45)    NULL        COMMENT '설명(직책명 등)',
    PRIMARY KEY (idOrganization, idUser)
);

ALTER TABLE Registration ADD CONSTRAINT FK_Registration_idOrganization_Organization_idOrganization FOREIGN KEY (idOrganization)
 REFERENCES Organization (idOrganization)  ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Registration ADD CONSTRAINT FK_Registration_idUser_User_idUser FOREIGN KEY (idUser)
 REFERENCES User (idUser)  ON DELETE RESTRICT ON UPDATE RESTRICT;


-- Menu Table Create SQL
CREATE TABLE Menu
(
    `idMenu`       INT            NOT NULL    AUTO_INCREMENT COMMENT '메뉴번호',
    `idMenuGroup`  INT            NOT NULL    COMMENT '메뉴그룹번호',
    `name`         VARCHAR(45)    NOT NULL    COMMENT '메뉴명',
    `depth`        VARCHAR(45)    NOT NULL    COMMENT 'Depth',
    `order`        VARCHAR(45)    NOT NULL    COMMENT '순서',
    `url`          VARCHAR(45)    NOT NULL    COMMENT 'url',
    PRIMARY KEY (idMenu)
);


-- LeftUser Table Create SQL
CREATE TABLE LeftUser
(
    `idUser`             INT            NOT NULL    COMMENT '회원번호',
    `idKakao`            BIGINT         NULL        COMMENT '카톡ID, (카톡로그인을 통해 받아옴)',
    `name`               VARCHAR(10)    NOT NULL    COMMENT '이름',
    `nickname`           VARCHAR(10)    NOT NULL    COMMENT '세례명',
    `dateJoined`         DATETIME       NOT NULL    COMMENT '가입일자',
    `dateStatusChanged`  DATETIME       NOT NULL    COMMENT '상태변경일시',
    PRIMARY KEY (idUser)
);


-- DormantUser Table Create SQL
CREATE TABLE DormantUser
(
    `idUser`             INT            NOT NULL    COMMENT '회원번호',
    `idKakao`            BIGINT         NULL        COMMENT '카톡ID, (카톡로그인을 통해 받아옴)',
    `name`               VARCHAR(10)    NOT NULL    COMMENT '이름',
    `nickname`           VARCHAR(10)    NOT NULL    COMMENT '세례명',
    `dateJoined`         DATETIME       NOT NULL    COMMENT '가입일자',
    `dateStatusChanged`  DATETIME       NOT NULL    COMMENT '상태변경일시',
    PRIMARY KEY (idUser)
);

-- MetaData Table Create SQL
CREATE TABLE MetaData
(
    `idMeta`  INT    NOT NULL    AUTO_INCREMENT COMMENT '메타번호',
    PRIMARY KEY (idMeta)
);

-- Schedule Table Create SQL
CREATE TABLE Schedule
(
    `idSchedule`      INT            NOT NULL    AUTO_INCREMENT COMMENT '일정번호',
    `name`            VARCHAR(45)    NOT NULL    COMMENT '일정이름',
    `dateStart`       DATETIME       NOT NULL    COMMENT '시작일시',
    `dateEnd`         DATETIME       NOT NULL    COMMENT '종료일시',
    `idOrganization`  INT            NOT NULL    COMMENT '단체번호',
    `idUser`          INT            NOT NULL    COMMENT '회원번호',
    `description`     VARCHAR(45)    NULL        COMMENT '설명',
    PRIMARY KEY (idSchedule)
);

ALTER TABLE Schedule ADD CONSTRAINT FK_Schedule_idOrganization_Organization_idOrganization FOREIGN KEY (idOrganization)
 REFERENCES Organization (idOrganization)  ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE Schedule ADD CONSTRAINT FK_Schedule_idUser_User_idUser FOREIGN KEY (idUser)
 REFERENCES User (idUser)  ON DELETE RESTRICT ON UPDATE RESTRICT;


-- FileAttachment Table Create SQL
CREATE TABLE FileAttachment
(
    `idFile`     INT            NOT NULL    AUTO_INCREMENT COMMENT '파일번호',
    `name`       VARCHAR(45)    NULL        COMMENT '파일명',
    `idArticle`  INT            NULL        COMMENT '글번호',
    PRIMARY KEY (idFile)
);

ALTER TABLE FileAttachment ADD CONSTRAINT FK_FileAttachment_idArticle_Article_idArticle FOREIGN KEY (idArticle)
 REFERENCES Article (idArticle)  ON DELETE RESTRICT ON UPDATE RESTRICT;


