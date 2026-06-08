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
('Andria', 'Mamy', 'mamy.andria@gmail.com', '+261 32 11 555 99', 'Rue de l Indépendance'),
('Dupont', 'Marie', 'marie.dupont@orange.fr', '+261 33 88 777 00', 'Villa 4, Résidence du Port');

