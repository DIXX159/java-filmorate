drop table LIKES;
drop table FRIENDS;
drop table USERS;
drop table FILM_GENRE;
drop table GENRE;
drop table FILMS;
drop table MPA;

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE,
    DURATION     INTEGER           not null,
    RATE         INTEGER,
    MPA          INTEGER,
    constraint "FILMS_pk"
        primary key (FILM_ID)
);
create unique index FILMS_NAME_DESCRIPTION_DATE_UINDEX
    on FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE);

create table IF NOT EXISTS USERS
(
    USER_ID  integer primary key auto_increment,
    EMAIL    CHARACTER VARYING,
    LOGIN    CHARACTER VARYING not null,
    NAME     CHARACTER VARYING,
    BIRTHDAY DATE,
    constraint USERS_PK
    primary key (USER_ID)
    );
create table IF NOT EXISTS LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER,
    RATE INTEGER not null
    );
create table IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER           not null,
    FRIEND_ID INTEGER           not null,
    STATUS    CHARACTER VARYING not null
);
create table IF NOT EXISTS GENRE
(
    GENRE_ID INTEGER auto_increment,
    GENRE    CHARACTER VARYING not null
    );
create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null
    );

create table IF NOT EXISTS MPA
(
    MPA_ID INTEGER auto_increment,
    MPA    CHARACTER VARYING not null,
    constraint "MPA_pk"
        primary key (MPA_ID)
);
/*create unique index FILM_GENRE_ID_INDEX
    on FILM_GENRE (FILM_ID, GENRE_ID);*/