create table public.actor
(
    id   bigserial
        primary key,
    name varchar(255)
);

alter table public.actor
    owner to postgres;

create table public.city
(
    id   bigserial
        primary key,
    name varchar(255)
);

alter table public.city
    owner to postgres;

create table public.movie
(
    id     bigserial
        primary key,
    length integer          not null,
    name   varchar(255),
    rating double precision not null
);

alter table public.movie
    owner to postgres;

create table public.movie_actors
(
    movies_id bigint not null
        constraint fkdild7jsp1x529e6nkqpayxm1b
            references public.movie,
    actors_id bigint not null
        constraint fkoxmxj61v0a9qs12vboo8rxpno
            references public.actor
);

alter table public.movie_actors
    owner to postgres;

create table public.movie_languages
(
    movie_id  bigint not null
        constraint fkl9pko34urm8ncqqeabmnad8x
            references public.movie,
    languages varchar(255)
);

alter table public.movie_languages
    owner to postgres;

create table public.movie_movie_features
(
    movie_id       bigint not null
        constraint fkmn98aceiqg57y46g9xhdox74w
            references public.movie,
    movie_features varchar(255)
);

alter table public.movie_movie_features
    owner to postgres;

create table public.seat
(
    id          bigserial
        primary key,
    seat_number varchar(255),
    seat_type   varchar(255)
);

alter table public.seat
    owner to postgres;

create table public.theatre
(
    id      bigserial
        primary key,
    address varchar(255),
    name    varchar(255)
);

alter table public.theatre
    owner to postgres;

create table public.auditorium
(
    id         bigserial
        primary key,
    capacity   integer not null,
    name       varchar(255),
    theatre_id bigint
        constraint fk7utg9cwqt56kfn88dpr3puw9u
            references public.theatre
);

alter table public.auditorium
    owner to postgres;

create table public.auditorium_auditorium_features
(
    auditorium_id       bigint not null
        constraint fk7m226mlusvkl9792owoyaan5e
            references public.auditorium,
    auditorium_features varchar(255)
);

alter table public.auditorium_auditorium_features
    owner to postgres;

create table public.auditorium_seats
(
    auditorium_id bigint not null
        constraint fkssovqql3i76th7qpwikbos2s4
            references public.auditorium,
    seats_id      bigint not null
        constraint uk_q5yenm227078w6hkal4bujk2k
            unique
        constraint fke89v28dsu5ohngkptnydlwy7s
            references public.seat
);

alter table public.auditorium_seats
    owner to postgres;

create table public.city_theatres
(
    city_id     bigint not null
        constraint fkibkfc4xkxm802unprovcwmfil
            references public.city,
    theatres_id bigint not null
        constraint uk_1avyi6ttmhbk089cdb1f4yaw3
            unique
        constraint fkcgtbbcasxbn1behm98mjd5255
            references public.theatre
);

alter table public.city_theatres
    owner to postgres;

create table public.show
(
    id            bigserial
        primary key,
    end_time      timestamp(6),
    language      varchar(255),
    start_time    timestamp(6),
    auditorium_id bigint
        constraint fkb85d252r5oqxxu988sv50f4ud
            references public.auditorium,
    movie_id      bigint
        constraint fkrko1v6l96s1pp0gvuit6c7l7q
            references public.movie
);

alter table public.show
    owner to postgres;

create table public.show_seattype_mapping
(
    id        bigserial
        primary key,
    price     double precision not null,
    seat_type varchar(255),
    show_id   bigint
        constraint fk2redd0vpndmormegr387snxg1
            references public.show
);

alter table public.show_seattype_mapping
    owner to postgres;

create table public.show_show_features
(
    show_id       bigint not null
        constraint fknxpm7piatoyuhbwuvky57hapq
            references public.show,
    show_features varchar(255)
);

alter table public.show_show_features
    owner to postgres;

create table public.show_show_seat_types
(
    show_id            bigint not null
        constraint fkf3thlyxv1wc6afg0b1i85vach
            references public.show,
    show_seat_types_id bigint not null
        constraint uk_2q01ed00y94klx1331obpolj5
            unique
        constraint fk2bu5jr65lyddiisciqeend4af
            references public.show_seattype_mapping
);

alter table public.show_show_seat_types
    owner to postgres;

create table public.show_seat
(
    id      bigserial
        primary key,
    state   varchar(255),
    seat_id bigint
        constraint fk6i74j78n2rm115651mji6o8yj
            references public.seat,
    show_id bigint
        constraint fke3tntcmo1pp6wj0nnltb8mdxs
            references public.show
);

alter table public.show_seat
    owner to postgres;

create table public.theatre_upcoming_shows
(
    theatre_id        bigint not null
        constraint fks3heqm41ympo0j4vdwbhdhlmr
            references public.theatre,
    upcoming_shows_id bigint not null
        constraint uk_kjrknpkev9xncogn5logkrdak
            unique
        constraint fklxobo6adyj9app0eyu0y0uprf
            references public.show
);

alter table public.theatre_upcoming_shows
    owner to postgres;

create table public.users
(
    id    bigserial
        primary key,
    email varchar(255)
);

alter table public.users
    owner to postgres;

create table public.ticket
(
    id              bigserial
        primary key,
    ticket_status   varchar(255),
    time_of_booking timestamp(6),
    total_amount    double precision not null,
    booked_by_id    bigint
        constraint fks3wrkywnp9rfrm06v9fs93tsk
            references public.users,
    show_id         bigint
        constraint fkd142jb6gj0dhu38tyf2bn5tc
            references public.show
);

alter table public.ticket
    owner to postgres;

create table public.payment
(
    id              bigserial
        primary key,
    amount          double precision not null,
    payment_method  varchar(255),
    payment_status  varchar(255),
    reference_id    varchar(255),
    time_of_payment timestamp(6),
    ticket_id       bigint
        constraint fkknnc0dgh1i8fkalxi2qcj2bwl
            references public.ticket
);

alter table public.payment
    owner to postgres;

create table public.ticket_show_seats
(
    ticket_id     bigint not null
        constraint fkbfdt8c3535o8n02qphcl61w3q
            references public.ticket,
    show_seats_id bigint not null
        constraint fkdifsd5gshm8tvh21wirvpbts
            references public.show_seat
);

alter table public.ticket_show_seats
    owner to postgres;

