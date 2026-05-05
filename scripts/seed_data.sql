-- Insertion des Régions
INSERT INTO region (libelle) VALUES 
('Analamanga'), 
('Vakinankaratra'), 
('Atsinanana');

-- Insertion des Districts (liés aux régions ci-dessus)
INSERT INTO district (libelle, region_id) VALUES 
('Antananarivo Renivohitra', 1), 
('Ambatolampy', 2), 
('Toamasina I', 3);

-- Insertion des Communes (liées aux districts ci-dessus)
INSERT INTO commune (libelle, district_id) VALUES 
('Ankadifotsy', 1), 
('Isotry', 1), 
('Ambatolampy Centre', 2), 
('Tanambao', 3);

INSERT INTO client (nom, prenom, email, telephone, adresse) VALUES 
('Ranaivo', 'Jean', 'jean.ranaivo@email.mg', '+261 34 00 123 45', 'Lot IVG 42 Ambodivona'),
('Andria', 'Mamy', 'mamy.andria@gmail.com', '+261 32 11 555 99', 'Rue de l’Indépendance'),
('Dupont', 'Marie', 'marie.dupont@orange.fr', '+261 33 88 777 00', 'Villa 4, Résidence du Port');

INSERT INTO demande (reference, client_id, commune_id, lieu, date_demande) VALUES 
('FOR-2026-001', 1, 1, 'Terrain Nord - Lot 12', '2026-05-01 09:00:00'),
('FOR-2026-002', 2, 3, 'Près du marché couvert', '2026-05-03 14:30:00'),
('FOR-2026-003', 3, 4, 'Zone portuaire - Hangar B', '2026-05-05 10:15:00');

-- Suivi pour la demande 1 (FOR-2026-001) : Déjà en cours de forage
INSERT INTO status_demande (demande_id, status_id, date_status) VALUES 
(1, 1, '2026-05-01 09:00:00'),
(1, 2, '2026-05-01 16:00:00'),
(1, 3, '2026-05-02 10:00:00'),
(1, 4, '2026-05-04 08:00:00');

-- Suivi pour la demande 2 (FOR-2026-002) : Nouveau dossier
INSERT INTO status_demande (demande_id, status_id, date_status) VALUES 
(2, 1, '2026-05-03 14:30:00');

-- Suivi pour la demande 3 (FOR-2026-003) : Devis envoyé mais non validé
INSERT INTO status_demande (demande_id, status_id, date_status) VALUES 
(3, 1, '2026-05-05 10:15:00'),
(3, 2, '2026-05-05 11:45:00');