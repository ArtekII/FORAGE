CREATE DATABASE IF NOT EXISTS forage_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE forage_db;

DROP TABLE IF EXISTS status_demande;
DROP TABLE IF EXISTS `status`;
DROP TABLE IF EXISTS demande;

CREATE TABLE demande (
  id BIGINT NOT NULL AUTO_INCREMENT,
  reference VARCHAR(64) NOT NULL,
  nom_client VARCHAR(255) NOT NULL,
  date_demande DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  region VARCHAR(255) NOT NULL,
  district VARCHAR(255) NOT NULL,
  commune VARCHAR(255) NOT NULL,
  fokontany VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_demande_reference (reference)
) ENGINE=InnoDB;

CREATE TABLE `status` (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nom_status VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_status_nom_status (nom_status)
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

INSERT INTO `status` (nom_status) VALUES
  ('demande_creer'),
  ('devis_genere'),
  ('devis_valide'),
  ('forage_en_cours'),
  ('forage_termine'),
  ('demande_cloturee');
