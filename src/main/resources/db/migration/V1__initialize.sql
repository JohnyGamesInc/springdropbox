SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  username              VARCHAR(50) NOT NULL,
  password              CHAR(80) NOT NULL,
  first_name            VARCHAR(50) NOT NULL,
  last_name             VARCHAR(50) NOT NULL,
  email                 VARCHAR(50) NOT NULL,
  phone                 VARCHAR(15) NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  name                  VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS users_roles;

CREATE TABLE users_roles (
  user_id               INT(11) NOT NULL,
  role_id               INT(11) NOT NULL,

  PRIMARY KEY (user_id, role_id),

--  KEY FK_ROLE_idx (role_id),

  CONSTRAINT FK_USER_ID_01 FOREIGN KEY (user_id)
  REFERENCES users (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION,

  CONSTRAINT FK_ROLE_ID FOREIGN KEY (role_id)
  REFERENCES roles (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS files;

CREATE TABLE files (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  user_id            INT(11) NOT NULL,
  title                 VARCHAR (255) NOT NULL,
  path                  VARCHAR(250) NOT NULL,
  size                  BIGINT(20) NOT NULL,
  create_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT FK_FILE_USER_ID FOREIGN KEY (user_id)
  REFERENCES users (id)
  ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO users (username,password,first_name,last_name,email,phone)
VALUES
('itjohn','$2a$10$QtvH2U/PCMyk2N3UCQ9H2Ok7.XmpXP9P.3IY1aAYJWML0V5XlwCda','Evgenii','Ivanov','ivanjohnoff@gmail.com','+79118449995');
--'123' pass

INSERT INTO roles (name)
VALUES
('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO users_roles (user_id, role_id)
VALUES
(1, 1),
(1, 2);