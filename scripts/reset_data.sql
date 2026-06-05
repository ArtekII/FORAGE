USE forage_db;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE devis_details;
TRUNCATE TABLE devis;
TRUNCATE TABLE alerte_parametre;
TRUNCATE TABLE statut_demande;
TRUNCATE TABLE demande;
TRUNCATE TABLE client;
TRUNCATE TABLE commune;
TRUNCATE TABLE district;
TRUNCATE TABLE region;
TRUNCATE TABLE statut;
TRUNCATE TABLE type;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO statut (sigle, libelle) VALUES
  ('C', 'Demande Creer'),
  ('DEC', 'Devis Etude Creer'),
  ('DER', 'Devis Etude Refuser'),
  ('DFC', 'Devis Forage Creer'),
  ('DFR', 'Devis Forage Refuser');

INSERT INTO type (libelle) VALUES
  ('Etude'),
  ('Forage');

INSERT INTO alerte_parametre (statut_depart_id, statut_arrivee_id, duree_minutes, niveau) VALUES
  (1, 2, 200, 'JAUNE'),
  (1, 2, 350, 'ROUGE'),
  (1, 4, 1000, 'JAUNE');
