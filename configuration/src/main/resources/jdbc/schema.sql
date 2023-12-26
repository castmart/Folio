create database folio;
\c folio
create table ticket(
    id uuid PRIMARY KEY,
    ticket_number varchar not null,
    owner_name varchar not null,
    owner_email varchar not null,
    owner_phone_number varchar not null,
    shoe_description varchar not null,
    completion_date timestamp not null,
    status varchar not null
);
