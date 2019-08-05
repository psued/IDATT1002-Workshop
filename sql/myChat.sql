-- Adminer 4.7.1 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS myChat;
CREATE DATABASE myChat /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE myChat;

DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS user_groupChat;
DROP TABLE IF EXISTS groupChat;
DROP TABLE IF EXISTS user;

CREATE TABLE `user` (
  userId INT(11) NOT NULL AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL UNIQUE,
  password TEXT,
  salt BLOB,
  PRIMARY KEY (userId)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE groupChat(
  groupChatId INT(11) NOT NULL AUTO_INCREMENT,
  groupChatName VARCHAR(64) NOT NULL,
  PRIMARY KEY (groupChatId)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE user_groupChat (
  userId INT(11) NOT NULL,
  groupChatId INT(11) NOT NULL,
  PRIMARY KEY (userId, groupChatId),
  CONSTRAINT user_groupChat_ibfk_3 FOREIGN KEY (userId) REFERENCES user (userId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT user_groupChat_ibfk_4 FOREIGN KEY (groupChatId) REFERENCES groupChat (groupChatId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE message (
  messageId INT(11) NOT NULL AUTO_INCREMENT,
  userId1 INT(11) NOT NULL,
  userId2 INT(11),
  timestamp TIMESTAMP NOT NULL,
  messageContent VARCHAR (500) NOT NULL,
  groupChatId INT(11),
  PRIMARY KEY (messageId),
  KEY userId1 (userId1),
  KEY userId2 (userId2),
  CONSTRAINT message_ibfk_3 FOREIGN KEY (userId1) REFERENCES user (userId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT message_ibfk_4 FOREIGN KEY (userId2) REFERENCES user (userId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT message_ibfk_5 foreign key (groupChatId) REFERENCES groupChat (groupChatId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





INSERT INTO user (username) VALUES ('Ola');
INSERT INTO user (username) VALUES ('Kari');

INSERT INTO message (userId1, userId2, timestamp, messageContent) VALUES (1, 2, NOW(), 'Hei');
-- 2019-06-17 13:40:12
