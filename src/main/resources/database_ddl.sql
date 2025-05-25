-- Таблица пользователей
create table public.users
(
    id               bigserial
        primary key,
    firstname        varchar(20)  not null,
    second_name      varchar(20)  not null,
    age              integer,
    email            varchar(255) not null
        unique,
    sex              varchar(10),
    telephone_number varchar(12),
    created          timestamp default CURRENT_TIMESTAMP,
    updated          timestamp default CURRENT_TIMESTAMP
);

alter table public.users
    owner to dog_101;

-- Таблица безопасности (логины, пароли, роли)
create table public.security
(
    id       bigserial
        primary key,
    login    varchar(255) not null,
    password varchar(255) not null,
    role     varchar(50)  not null,
    created  timestamp default CURRENT_TIMESTAMP,
    updated  timestamp default CURRENT_TIMESTAMP,
    user_id  bigint
        unique
        references public.users
            on delete cascade
);

alter table public.security
    owner to dog_101;

-- Таблица отзывов на книги
create table public.reviews
(
    id         bigserial
        primary key,
    text       text,
    rating     double precision not null
        constraint reviews_rating_check
            check ((rating >= (1.0)::double precision) AND (rating <= (10.0)::double precision)),
    created timestamp default CURRENT_TIMESTAMP,
    user_id    bigint
        references public.users
            on delete cascade,
    book_id    bigint
        references public.books
            on delete cascade,
    unique (user_id, book_id)
);

alter table public.reviews
    owner to dog_101;

-- Таблица книг
create table public.books
(
    id           bigint default nextval('books_id_seq'::regclass) not null
        primary key,
    title        varchar(255)                                     not null,
    author       varchar(255)                                     not null,
    genre        varchar(100),
    description  text,
    release_year integer
);

alter table public.books
    owner to dog_101;




