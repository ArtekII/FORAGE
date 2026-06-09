-- Insertion des Régions
INSERT INTO region (libelle) VALUES 
('Analamanga'), 
('Vakinankaratra'), 
('Atsinanana');

-- Insertion des Districts (liés aux régions ci-dessus)
INSERT INTO district (libelle, region_id) VALUES 
('Bekily', 1), 
('Ambatolampy', 2), 
('Toamasina I', 3);

-- Insertion des Communes (liées aux districts ci-dessus)
INSERT INTO commune (libelle, district_id) VALUES 
('Ambahita', 1), 
('Ambatolampy Centre', 2), 
('Tanambao', 3);

INSERT INTO client (nom, prenom, email, telephone, adresse) VALUES 
('Rakoto', '', 'rakoto@email.mg', '+261 34 00 123 45', 'Lot IVG 42 Ambodivona');


