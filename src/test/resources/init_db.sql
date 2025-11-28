-- =========================================
-- 데이터베이스 초기화 스크립트
-- =========================================

-- 기존 테이블 삭제
DROP TABLE IF EXISTS expenditure;
DROP TABLE IF EXISTS budget;
DROP TABLE IF EXISTS category_budget_summary;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS user;

-- =========================================
-- 테이블 생성
-- =========================================

CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    role ENUM('ADMIN','USER') NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id)
);

CREATE TABLE category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    type ENUM('CUSTOM','STANDARD') NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id)
);

CREATE TABLE budget (
    id BIGINT NOT NULL AUTO_INCREMENT,
    date DATE NOT NULL,
    amount BIGINT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

create table category_budget_summary
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    total_amount BIGINT NOT NULL,
    category_id  BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE expenditure (
    id BIGINT NOT NULL AUTO_INCREMENT,
    spend_at DATETIME(6) NOT NULL,
    amount BIGINT NOT NULL,
    memo VARCHAR(255),
    excluded_from_total BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- =========================================
-- 더미 데이터 삽입
-- =========================================

-- USER
INSERT INTO user (email, password, name, role, is_deleted, created_at, updated_at)
VALUES
    ('user01@example.com', '{noop}password001!@#', '사용자01', 'USER', FALSE, NOW(), NOW()),
    ('user02@example.com', '{noop}password001!@#', '사용자02', 'USER', FALSE, NOW(), NOW()),
    ('user03@example.com', '{noop}password001!@#', '사용자03', 'USER', FALSE, NOW(), NOW()),
    ('user04@example.com', '{noop}password001!@#', '사용자04', 'USER', FALSE, NOW(), NOW()),
    ('user05@example.com', '{noop}password001!@#', '사용자05', 'USER', FALSE, NOW(), NOW());

-- CATEGORY
INSERT INTO category (name, type, is_deleted, created_at, updated_at)
VALUES
    ('식비', 'STANDARD', FALSE, NOW(), NOW()),
    ('교통비', 'STANDARD', FALSE, NOW(), NOW()),
    ('경조사비', 'STANDARD', FALSE, NOW(), NOW()),
    ('문화비', 'STANDARD', FALSE, NOW(), NOW()),
    ('기타', 'STANDARD', FALSE, NOW(), NOW());

-- BUDGET (2025년 1월~6월, 임의 금액)
INSERT INTO budget (date, amount, is_deleted, user_id, category_id, created_at, updated_at)
VALUES
    ('2025-01-01', 200000, FALSE, 1, 1, NOW(), NOW()),
    ('2025-01-01', 150000, FALSE, 1, 2, NOW(), NOW()),
    ('2025-01-01', 100000, FALSE, 1, 3, NOW(), NOW()),
    ('2025-01-01', 120000, FALSE, 1, 4, NOW(), NOW()),
    ('2025-01-01', 80000, FALSE, 1, 5, NOW(), NOW()),
    ('2025-01-01', 210000, FALSE, 2, 1, NOW(), NOW()),
    ('2025-01-01', 160000, FALSE, 2, 2, NOW(), NOW()),
    ('2025-01-01', 110000, FALSE, 2, 3, NOW(), NOW()),
    ('2025-01-01', 130000, FALSE, 2, 4, NOW(), NOW()),
    ('2025-01-01', 90000, FALSE, 2, 5, NOW(), NOW());

-- EXPENDITURE (2025년 1월 1일 기준, 임의 금액 및 메모)
INSERT INTO expenditure (spend_at, amount, memo, excluded_from_total, is_deleted, user_id, category_id, created_at, updated_at)
VALUES
    ('2025-01-01 09:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 1, NOW(), NOW()),
    ('2025-01-01 12:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 2, NOW(), NOW()),
    ('2025-01-01 14:30:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 3, NOW(), NOW()),
    ('2025-01-01 18:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 4, NOW(), NOW()),
    ('2025-01-01 20:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 5, NOW(), NOW()),
    ('2025-01-01 10:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 1, NOW(), NOW()),
    ('2025-01-01 13:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 2, NOW(), NOW()),
    ('2025-01-01 15:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 3, NOW(), NOW()),
    ('2025-01-01 19:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 4, NOW(), NOW()),
    ('2025-01-01 21:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 5, NOW(), NOW()),

    ('2025-01-02 09:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 1, NOW(), NOW()),
    ('2025-01-02 12:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 2, NOW(), NOW()),
    ('2025-01-02 14:30:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 3, NOW(), NOW()),
    ('2025-01-02 18:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 4, NOW(), NOW()),
    ('2025-01-02 20:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 5, NOW(), NOW()),
    ('2025-01-02 10:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 1, NOW(), NOW()),
    ('2025-01-02 13:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 2, NOW(), NOW()),
    ('2025-01-02 15:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 3, NOW(), NOW()),
    ('2025-01-02 19:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 4, NOW(), NOW()),
    ('2025-01-02 21:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 5, NOW(), NOW()),

    ('2025-01-03 09:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 1, NOW(), NOW()),
    ('2025-01-03 12:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 2, NOW(), NOW()),
    ('2025-01-03 14:30:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 3, NOW(), NOW()),
    ('2025-01-03 18:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 4, NOW(), NOW()),
    ('2025-01-03 20:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 5, NOW(), NOW()),
    ('2025-01-03 10:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 1, NOW(), NOW()),
    ('2025-01-03 13:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 2, NOW(), NOW()),
    ('2025-01-03 15:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 3, NOW(), NOW()),
    ('2025-01-03 19:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 4, NOW(), NOW()),
    ('2025-01-03 21:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 5, NOW(), NOW()),

    ('2025-01-04 09:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 1, NOW(), NOW()),
    ('2025-01-04 12:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 2, NOW(), NOW()),
    ('2025-01-04 14:30:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 3, NOW(), NOW()),
    ('2025-01-04 18:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 4, NOW(), NOW()),
    ('2025-01-04 20:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 5, NOW(), NOW()),
    ('2025-01-04 10:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 1, NOW(), NOW()),
    ('2025-01-04 13:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 2, NOW(), NOW()),
    ('2025-01-04 15:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 3, NOW(), NOW()),
    ('2025-01-04 19:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 4, NOW(), NOW()),
    ('2025-01-04 21:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 5, NOW(), NOW()),

    ('2025-01-05 09:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 1, NOW(), NOW()),
    ('2025-01-05 12:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 2, NOW(), NOW()),
    ('2025-01-05 14:30:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 3, NOW(), NOW()),
    ('2025-01-05 18:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 4, NOW(), NOW()),
    ('2025-01-05 20:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 5, NOW(), NOW()),
    ('2025-01-05 10:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 1, NOW(), NOW()),
    ('2025-01-05 13:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 2, NOW(), NOW()),
    ('2025-01-05 15:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 3, NOW(), NOW()),
    ('2025-01-05 19:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 4, NOW(), NOW()),
    ('2025-01-05 21:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 5, NOW(), NOW()),

    ('2025-01-06 09:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 1, NOW(), NOW()),
    ('2025-01-06 12:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 2, NOW(), NOW()),
    ('2025-01-06 14:30:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 3, NOW(), NOW()),
    ('2025-01-06 18:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 4, NOW(), NOW()),
    ('2025-01-06 20:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 5, NOW(), NOW()),
    ('2025-01-06 10:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 1, NOW(), NOW()),
    ('2025-01-06 13:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 2, NOW(), NOW()),
    ('2025-01-06 15:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 3, NOW(), NOW()),
    ('2025-01-06 19:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 4, NOW(), NOW()),
    ('2025-01-06 21:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 5, NOW(), NOW()),

    ('2025-01-07 09:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 1, NOW(), NOW()),
    ('2025-01-07 12:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 2, NOW(), NOW()),
    ('2025-01-07 14:30:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 3, NOW(), NOW()),
    ('2025-01-07 18:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 4, NOW(), NOW()),
    ('2025-01-07 20:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 5, NOW(), NOW()),
    ('2025-01-07 10:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 1, NOW(), NOW()),
    ('2025-01-07 13:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 2, NOW(), NOW()),
    ('2025-01-07 15:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 3, NOW(), NOW()),
    ('2025-01-07 19:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 4, NOW(), NOW()),
    ('2025-01-07 21:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 5, NOW(), NOW()),

    ('2025-01-08 09:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 1, NOW(), NOW()),
    ('2025-01-08 12:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 2, NOW(), NOW()),
    ('2025-01-08 14:30:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 3, NOW(), NOW()),
    ('2025-01-08 18:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 4, NOW(), NOW()),
    ('2025-01-08 20:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 5, NOW(), NOW()),
    ('2025-01-08 10:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 1, NOW(), NOW()),
    ('2025-01-08 13:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 2, NOW(), NOW()),
    ('2025-01-08 15:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 3, NOW(), NOW()),
    ('2025-01-08 19:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 4, NOW(), NOW()),
    ('2025-01-08 21:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 5, NOW(), NOW()),

    ('2025-01-09 09:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 1, NOW(), NOW()),
    ('2025-01-09 12:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 2, NOW(), NOW()),
    ('2025-01-09 14:30:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 3, NOW(), NOW()),
    ('2025-01-09 18:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 4, NOW(), NOW()),
    ('2025-01-09 20:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 5, NOW(), NOW()),
    ('2025-01-09 10:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 1, NOW(), NOW()),
    ('2025-01-09 13:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 2, NOW(), NOW()),
    ('2025-01-09 15:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 3, NOW(), NOW()),
    ('2025-01-09 19:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 4, NOW(), NOW()),
    ('2025-01-09 21:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 5, NOW(), NOW()),

    ('2025-01-10 09:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 1, NOW(), NOW()),
    ('2025-01-10 12:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 2, NOW(), NOW()),
    ('2025-01-10 14:30:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 3, NOW(), NOW()),
    ('2025-01-10 18:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 4, NOW(), NOW()),
    ('2025-01-10 20:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 1, 5, NOW(), NOW()),
    ('2025-01-10 10:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 1, NOW(), NOW()),
    ('2025-01-10 13:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 2, NOW(), NOW()),
    ('2025-01-10 15:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 3, NOW(), NOW()),
    ('2025-01-10 19:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 4, NOW(), NOW()),
    ('2025-01-10 21:00:00', FLOOR(1 + RAND() * 10) * 1000, '메모', FALSE, FALSE, 2, 5, NOW(), NOW());