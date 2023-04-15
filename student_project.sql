DROP TABLE IF EXISTS jc_passport_office;
DROP TABLE IF EXISTS jc_register_office;
DROP TABLE IF EXISTS jc_country_structure;
DROP TABLE IF EXISTS jc_street;

CREATE TABLE jc_street
(
    street_code integer not null,
    street_name varchar(300),
    PRIMARY KEY (street_code)
);

CREATE TABLE jc_country_structure
(
    area_id char(12) not null,
    area_name varchar(200),
    PRIMARY KEY (area_id)
);

CREATE TABLE jc_passport_office
(
    passport_office_id integer not null,
    passport_office_area_id char(12) not null,
    passport_office_name varchar(200),
    PRIMARY KEY (passport_office_id),
    FOREIGN KEY(passport_office_area_id) REFERENCES jc_contry_structure(area_id) ON DELETE RESTRICT
);

CREATE TABLE jc_register_office
(
    register_office_id integer not null,
    register_office_area_id char(12) not null,
    register_office_name varchar(200),
    PRIMARY KEY (register_office_id),
    FOREIGN KEY(register_office_area_id) REFERENCES jc_contry_structure(area_id) ON DELETE RESTRICT
);