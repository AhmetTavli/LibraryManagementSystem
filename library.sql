create database if not exists library;

use library;

CREATE TABLE IF NOT EXISTS manager_accept (
    manager_accept_id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    manager_email VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS library_user (
    user_id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    email VARCHAR(60) NOT NULL,
    user_password VARCHAR(50) NOT NULL,
    gender VARCHAR(1) NOT NULL,
    birthday VARCHAR(45) NOT NULL,
    user_role VARCHAR(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS main (
    main_id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    manager_id INT,
    user_id INT
);

CREATE TABLE IF NOT EXISTS book (
    book_id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    b_name VARCHAR(30),
    author VARCHAR(30),
    b_year INT,
    isbn VARCHAR(30),
    quality VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS book_log (
    bl_id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    u_id INT,
    book_id INT,
    date_added DATE,
    isSubscribed BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS hire_book (
    hb_id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    u_id INT,
    b_id INT,
    h_date DATE,
    h_return DATE,
    time_left INT,
    penalty VARCHAR(30),
    action_confirmed VARCHAR(1)
);

CREATE TABLE IF NOT EXISTS request (
    req_id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    u_id INT,
    i_name VARCHAR(60),
    author VARCHAR(60),
    i_type VARCHAR(60),
    u_comment VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS user_mail (
    u_mail INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    mail_from VARCHAR(100),
    body VARCHAR(1000),
    u_id INT
);

# tests
insert into manager_accept(manager_email) values ("ahmet.tavli89@gmail.com");
insert into hire_book (u_id, b_id, h_date, h_return, time_left, penalty, action_confirmed) values (13, 8, "2019-01-14", "2019-01-18", 4, "0 £", "Y");
insert into hire_book (u_id, b_id, h_date, h_return, time_left, penalty, action_confirmed) values (13, 13, "2019-01-17", "2019-01-18", 1, "0 £", "Y");
insert into hire_book (u_id, b_id, h_date, h_return, time_left, penalty, action_confirmed) values (13, 13, "2019-01-18", "2019-01-10", 1, "8 £", "Y");
insert into request (u_id, i_name, author, i_type, u_comment) values(?, ?, ?, ?, ?);

SELECT 
*
FROM 
user_mail;
SELECT 
    *
FROM
    manager_accept
WHERE
    manager_accept.manager_email = 'ahmet.tavli89@gmail.com';
SELECT 
    *
FROM
    library_user
WHERE
    library_user.email = 'ahmet.tavli89@gmail.com'
        AND user_password = '[C@3b763ad1';
SELECT 
    *
FROM
    library_user;
SELECT 
    *
FROM
    manager;
SELECT 
    *
FROM
    book;
SELECT 
    user_id
FROM
    library_user l
WHERE
    l.email = 'taner@yilmaz.com';
SELECT 
    *
FROM
    library_user l
WHERE
    l.email = 'ahmet.tavli89@gmail.com'
        AND l.user_password = '[C@3b763ad1';
SELECT 
    *
FROM
    book
WHERE
    isbn = '978-0-340-99858-8';
SELECT 
    *
FROM
    book_log;
SELECT 
    *
FROM
    hire_book;
SELECT 
    *
FROM
    library_user;
SELECT 
    *
FROM
    request;
SELECT 
    *
FROM
    book;
SELECT 
    *
FROM
    user_mail;
SELECT 
    b_name AS 'Book Name',
    author AS 'Authors',
    b_year AS 'Published Year',
    DAY(date_added) AS 'Day',
    MONTH(date_added) AS 'Month',
    YEAR(date_added) AS 'Year'
FROM
    book b,
    book_log bl
WHERE
    b.book_id = bl.book_id;

SELECT 
    b_name AS 'Book Name',
    author AS 'Author',
    b_year AS 'Published Year',
    isbn AS 'ISBN',
    h_date AS 'Hired Date',
    h_return AS 'Return Date',
    penalty AS 'Payment',
    time_left AS 'Day Left',
    CASE
        WHEN action_confirmed <> 'N' THEN 'Manager Confirmed'
        ELSE 'Not Confirmed'
    END
FROM
    hire_book hb,
    book b
WHERE
    hb.b_id = b.book_id AND u_id = 13;

SELECT 
    b_name AS 'Book Name',
    author AS 'Author',
    b_year AS 'Published Year',
    isbn AS 'ISBN',
    h_date AS 'Hired Date',
    h_return AS 'Return Date',
    penalty AS 'Payment',
    time_left AS 'Day Left',
    CASE
        WHEN action_confirmed <> 'N' THEN 'Manager Confirmed'
        ELSE 'Not Confirmed'
    END
FROM
    hire_book hb,
    book b
WHERE
    h_return - h_date <= 5
        AND hb.b_id = b.book_id;

use library;

SELECT 
    first_name AS 'Name',
    surname AS 'Surname',
    email,
    b_name AS 'Book Name',
    b_year AS 'Published Year',
    isbn AS 'ISBN',
    h_date AS 'Hired Date',
    time_left AS 'Day Left',
    penalty AS 'Payment',
    CASE
        WHEN action_confirmed <> 'N' THEN 'Manager Confirmed'
        ELSE 'Not Confirmed'
    END AS 'Confirm Situation'
FROM
    hire_book hb,
    book b,
    library_user l
WHERE
    hb.b_id = b.book_id
        AND l.user_id = hb.u_id
        AND action_confirmed = 'N';

SELECT 
    b_name AS 'Book Name',
    author AS 'Author',
    b_year AS 'Published Year',
    isbn AS 'ISBN',
    h_date AS 'Hired Date',
    h_return AS 'Return Date',
    penalty AS 'Payment',
    time_left AS 'Day Left',
    CASE
        WHEN action_confirmed <> 'N' THEN 'Manager Confirmed'
        ELSE 'Not Confirmed'
    END AS 'ads'
FROM
    hire_book hb,
    book b
WHERE
    hb.b_id = b.book_id AND u_id = 13;

SELECT 
    b_name AS 'Book Name',
    author AS 'Author',
    b_year AS 'Published Year',
    isbn AS 'ISBN',
    h_date AS 'Hired Date',
    h_return AS 'Return Date',
    penalty AS 'Payment',
    time_left AS 'Day Left',
    CASE
        WHEN action_confirmed <> 'N' THEN 'Manager Confirmed'
        ELSE 'Not Confirmed'
    END AS 'ads'
FROM
    hire_book hb,
    book b
WHERE
    hb.b_id = b.book_id;
    

# -request query 1
use library;

SELECT 
    l.first_name AS 'Name',
    l.surname AS 'Surname',
    l.email,
    r.i_type AS 'Request Type',
    r.i_name AS 'Request Name',
    r.author AS 'Author'
FROM
    request r,
    library_user l
WHERE
    r.u_id = l.user_id;

set sql_safe_updates = 0;

UPDATE book_log 
SET 
    isSubscribed = TRUE
WHERE
    u_id = 13;
UPDATE hire_book 
SET 
    time_left = 0
WHERE
    hb_id = 9;
UPDATE library_user 
SET 
    user_password = '123'
WHERE
    user_id = 12;
DELETE FROM book_log 
WHERE
    book_id = 0;
DELETE FROM library_user 
WHERE
    gender = 'M';
DELETE FROM book 
WHERE
    book_id = 4;
