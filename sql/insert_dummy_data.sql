USE expense_manager;

DROP PROCEDURE IF EXISTS create_dummy_user;
DROP PROCEDURE IF EXISTS create_dummy_budget;
DROP PROCEDURE IF EXISTS loop_month;
DROP PROCEDURE IF EXISTS loop_category;
DROP PROCEDURE IF EXISTS create_dummy_expenditure;
DROP PROCEDURE IF EXISTS loop_month_expenditure;
DROP PROCEDURE IF EXISTS loop_day;
DROP PROCEDURE IF EXISTS loop_expenditure;

SHOW PROCEDURE STATUS WHERE Db = 'expense_manager';

DELIMITER //

CREATE PROCEDURE create_dummy_user()
BEGIN
        DECLARE i INT;
        SET i = 1;

        WHILE i <= 100 DO
              INSERT INTO user(email, name, password, role, is_deleted, created_at, updated_at)
              VALUES (CONCAT('test', i, '@naver.com'), CONCAT('test', i), '{noop}testpw1234', 'USER', false, NOW(), NOW());

              SET i = i + 1;
        END WHILE;
END //



CREATE PROCEDURE create_dummy_budget(year VARCHAR(255))
BEGIN
        DECLARE i INT;
        SET i = 1;

        WHILE i <= 100 DO
              CALL loop_month(year, i);
              SET i = i + 1;
        END WHILE;
END //



CREATE PROCEDURE loop_month(year VARCHAR(255),user int)
BEGIN
        DECLARE i INT;
        SET i = 1;

        WHILE i <= 12 DO
              CALL loop_category(year, i, user);
              SET i = i + 1;
        END WHILE;
END //



CREATE PROCEDURE loop_category(year VARCHAR(255), month VARCHAR(255),user int)
BEGIN
        DECLARE i INT;
        SET i = 1;

        WHILE i <= 7 DO
             INSERT INTO budget(user_id, category_id, date, amount, is_deleted, created_at, updated_at)
             VALUES (user, i, CONCAT(year, '-', month, '-01'), ROUND((RAND() * (950000) + 50000) / 1000) * 1000, false, NOW(), NOW());

             SET i = i + 1;
        END WHILE;
END //

CREATE PROCEDURE create_dummy_expenditure(year VARCHAR(255))
BEGIN
    DECLARE i INT;
    SET i = 1;

    WHILE i <= 10 DO
        CALL loop_month_expenditure(year, i);
        SET i = i + 1;
    END WHILE;
END //

CREATE PROCEDURE loop_month_expenditure(year VARCHAR(255), user INT)
BEGIN
    DECLARE i INT;
    SET i = 1;

    WHILE i <= 12 DO
        CALL loop_day(year, i, user);
        SET i = i + 1;
    END WHILE;
END //

CREATE PROCEDURE loop_day(year VARCHAR(255), month VARCHAR(255), user INT)
BEGIN
    DECLARE i INT;
    SET i = 1;

    IF month = 2 THEN
        WHILE i <= 28 DO
            CALL loop_expenditure(year, month, i, user);
            SET i = i + 1;
        END WHILE;
    ELSE
        WHILE i <= 30 DO
            CALL loop_expenditure(year, month, i, user);
            SET i = i + 1;
        END WHILE;
    END IF;
END //

CREATE PROCEDURE loop_expenditure(year VARCHAR(255), month VARCHAR(255), day VARCHAR(255), user INT)
BEGIN
    DECLARE i INT;
    SET i = 1;

    WHILE i <= 10 DO
        INSERT INTO expenditure(spend_at, category_id, user_id, amount, excluded_from_total, memo, is_deleted, created_at, updated_at)
        VALUES (CONCAT(year, '-', month, '-', day), FLOOR(1 + RAND() * 7), user, FLOOR(1 + RAND() * 10) * 1000, false, CONCAT('메모', i), false, NOW(), NOW());
        SET i = i + 1;
    END WHILE;
END //


DELIMITER ;

insert into
    category(type, name, is_deleted, created_at, updated_at)
VALUES
    ('STANDARD','기타', false, NOW(), NOW()),
    ('STANDARD','식비', false, NOW(), NOW()),
    ('STANDARD','교통비', false, NOW(), NOW()),
    ('STANDARD','통신비', false, NOW(), NOW()),
    ('STANDARD','취미활동비', false, NOW(), NOW()),
    ('STANDARD','문화비', false, NOW(), NOW()),
    ('STANDARD','경조사비', false, NOW(), NOW());

CALL create_dummy_user();
CALL create_dummy_budget('2025');
CALL create_dummy_expenditure('2025');