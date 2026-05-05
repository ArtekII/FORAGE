CREATE DATABASE IF NOT EXISTS forage_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE forage_db;

DROP TABLE IF EXISTS status_demande;
DROP TABLE IF EXISTS demande;
DROP TABLE IF EXISTS commune;
DROP TABLE IF EXISTS district;
DROP TABLE IF EXISTS region;
DROP TABLE IF EXISTS `status`;
DROP TABLE IF EXISTS client;

CREATE TABLE region (
  id BIGINT NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_region_libelle (libelle)
) ENGINE=InnoDB;

CREATE TABLE district (
  id BIGINT NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(255) NOT NULL,
  region_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_district_region_id (region_id),
  UNIQUE KEY uq_district_region_libelle (region_id, libelle),
  CONSTRAINT fk_district_region
    FOREIGN KEY (region_id) REFERENCES region(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE commune (
  id BIGINT NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(255) NOT NULL,
  district_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_commune_district_id (district_id),
  UNIQUE KEY uq_commune_district_libelle (district_id, libelle),
  CONSTRAINT fk_commune_district
    FOREIGN KEY (district_id) REFERENCES district(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE client (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nom VARCHAR(255),
  prenom VARCHAR(255),
  email VARCHAR(255),
  telephone VARCHAR(255),
  adresse VARCHAR(255),
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE demande (
  id BIGINT NOT NULL AUTO_INCREMENT,
  reference VARCHAR(64) NOT NULL,
  client_id BIGINT NOT NULL,
  commune_id BIGINT NOT NULL,
  lieu VARCHAR(255) NOT NULL,
  date_demande DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uq_demande_reference (reference),
  KEY idx_demande_client_id (client_id),
  KEY idx_demande_commune_id (commune_id),
  CONSTRAINT fk_demande_client
    FOREIGN KEY (client_id) REFERENCES client(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_demande_commune
    FOREIGN KEY (commune_id) REFERENCES commune(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `status` (
  id BIGINT NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_status_libelle (libelle)
) ENGINE=InnoDB;

CREATE TABLE status_demande (
  id BIGINT NOT NULL AUTO_INCREMENT,
  demande_id BIGINT NOT NULL,
  status_id BIGINT NOT NULL,
  date_status DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  KEY idx_status_demande_demande_id (demande_id),
  KEY idx_status_demande_status_id (status_id),
  CONSTRAINT fk_status_demande_demande
    FOREIGN KEY (demande_id) REFERENCES demande(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_status_demande_status
    FOREIGN KEY (status_id) REFERENCES `status`(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

INSERT INTO `status` (libelle) VALUES
  ('demande_creer'),
  ('devis_genere'),
  ('devis_valide'),
  ('forage_en_cours'),
  ('forage_termine'),
  ('demande_cloturee');
