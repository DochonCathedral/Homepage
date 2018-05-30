
-- # 초기 데이터 밀어넣기 데이터

INSERT INTO Organization(idParent, name, description) VALUES(null, '테스트 조직', '테스트 조직 설명');
INSERT INTO Organization(idParent, name, description) VALUES(1, '테스트 하위 조직', '하위 조직 설명');
INSERT INTO Organization(idParent, name, description) VALUES(null, '대단한 조직', '대단한 조직 설명');

INSERT INTO User(idKakao, name, nickname, accessToken, freshToken, thumbnail)
  VALUES(100000, '양승구', 'ysg0316', 'accessYsg', 'freshYsg', 'pathYsg');
INSERT INTO User(idKakao, name, nickname, accessToken, freshToken, thumbnail)
  VALUES(200000, '이기현', 'varian', null, null, 'pathVarian');
INSERT INTO User(idKakao, name, nickname, accessToken, freshToken, thumbnail)
  VALUES(300000, '유창석', 'bsyo2k', 'accessBsyo', 'freshBsyo', null);
INSERT INTO User(idKakao, name, nickname, accessToken, freshToken, thumbnail)
  VALUES(400000, '이기현멋남', 'varianBest', 'null', 'null', null);

INSERT INTO Registration(idOrganization, idUser, level, description) VALUES(1, 1, 2, '관리자');
INSERT INTO Registration(idOrganization, idUser, level, description) VALUES(2, 1, 3, '신부');
INSERT INTO Registration(idOrganization, idUser, level, description) VALUES(1, 2, 1, '일반인');

INSERT INTO Board(name, description, idOrganization, type) VALUES('테스트 게시판', '테스트 게시판 설명', 1, 1);
INSERT INTO Board(name, description, idOrganization, type) VALUES('자유 게시판', '자유 게시판 설명', 2, 1);
INSERT INTO Board(name, description, idOrganization, type) VALUES('사진 게시판', '사진 게시판 설명', 3, 2);

INSERT INTO Article(title, idUser, idBoard, countHit, contents, status, type, articlePassword, thumbnail)
  VALUES('테스트 게시글', 1, 1, 3, '테스트 글입니다.', 0, 0, null, null);
INSERT INTO Article(title, idUser, idBoard, countHit, contents, status, type, articlePassword, thumbnail)
  VALUES('자유 게시글', 1, 2, 50, '자유 글입니다.', 0, 1, null, null);
INSERT INTO Article(title, idUser, idBoard, countHit, contents, status, type, articlePassword, thumbnail)
  VALUES('비밀 게시글', 2, 2, 25, '비밀 글입니다.', 0, 2, 'password', null);
INSERT INTO Article(title, idUser, idBoard, countHit, contents, status, type, articlePassword, thumbnail)
  VALUES('사진 게시글', 3, 3, 10, '사진입니다.', 0, 0, null, 'pathThumbnail');

INSERT INTO Reply(idArticle, idUser, idParent, contents, status, replyPassword)
  VALUES(1, 1, null, '테스트 댓글', 0, null);
INSERT INTO Reply(idArticle, idUser, idParent, contents, status, replyPassword)
  VALUES(1, 1, 1, '테스트 댓글2', 1, null);
INSERT INTO Reply(idArticle, idUser, idParent, contents, status, replyPassword)
  VALUES(2, 2, null, '테스트 댓글3', 0, null);
INSERT INTO Reply(idArticle, idUser, idParent, contents, status, replyPassword)
  VALUES(1, 3, null, '테스트 댓글4', 0, null);
INSERT INTO Reply(idArticle, idUser, idParent, contents, status, replyPassword)
  VALUES(1, 3, null, '테스트 댓글4', 0, null);

INSERT INTO Schedule(name, dateStart, dateEnd, idOrganization, idUser, description)
  VALUES('일정!', '2018-05-01', '2018-05-14', 1, 1, '5월 일정');
INSERT INTO Schedule(name, dateStart, dateEnd, idOrganization, idUser, description)
  VALUES('결혼식', '2018-05-12', '2018-05-12', 1, 2, '결혼식 설명');
INSERT INTO Schedule(name, dateStart, dateEnd, idOrganization, idUser, description)
  VALUES('하하하 일정!', '2018-05-01', '2018-05-14', 2, 3, '5월 일정2');