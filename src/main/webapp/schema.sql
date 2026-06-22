-- CINÉast Database Schema
-- Run this once to set up your MySQL database

CREATE DATABASE IF NOT EXISTS rate_my_movie
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE rate_my_movie;

CREATE TABLE IF NOT EXISTS users (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  username   VARCHAR(50)  NOT NULL UNIQUE,
  email      VARCHAR(100) NOT NULL UNIQUE,
  password   VARCHAR(255) NOT NULL,          -- BCrypt hashed
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reviews (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  movieId    INT          NOT NULL,
  userId     INT          NOT NULL,
  content    TEXT         NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_reviews_user FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_reviews_movie ON reviews(movieId);
CREATE INDEX idx_reviews_user  ON reviews(userId);
