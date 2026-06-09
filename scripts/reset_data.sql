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
  ('DEA', 'Devis Etude Accepter'),
  ('DFC', 'Devis Forage Creer'),
  ('DFA', 'Devis Forage Accepter'),
  ('FC', 'Forage Creer'),
  ('FCT', 'Forage Terminer');

INSERT INTO type (libelle) VALUES
  ('Etude'),
  ('Forage');

INSERT INTO alerte_parametre (statut_depart_id, statut_arrivee_id, intervalle_minutes_1, intervalle_minutes_2, niveau) VALUES
  (1, 2, 480, 600, 'JAUNE'),
  (1, 2, 1440, 2880, 'ROUGE'),
  (2, 3, 240, 300, 'JAUNE'),
  (2, 3, 360, 480, 'ROUGE'),
  (3, 4, 60, 120, 'JAUNE'),
  (3, 4, 180, 240, 'ROUGE'),
  (4, 5, 240, 480, 'JAUNE'),
  (4, 5, 600, 720, 'ROUGE'),
  (5, 6, 1200, 1800, 'JAUNE'),
  (5, 6, 1801, 3600, 'ROUGE'),
  (6, 7, 3600, 4200, 'JAUNE'),
  (6, 7, 4800, 6000, 'ROUGE');
