# Besoin Fonctionnel - Gestion Demande/Statut

## 1. Interface "gestion demande-statut"

- Ajouter une ligne de statut pour une demande.
- Choisir la demande via une liste déroulante (avec chargement AJAX des informations liées).
- Saisir la date et l'heure du statut (précision à la minute).
- Choisir le statut (liste déroulante) et saisir une observation.
- Enregistrer dans la table `demande_statut`.


## 2. Modification d'une ligne existante

Champs modifiables :

- Demande
- Date
- Observation
- Statut

Contraintes :

- Le statut proposé dépend de la demande (note : indiqué comme "seulement ce que la demande a déjà passé").
- La mise à jour doit porter au minimum sur `idDemande` et `idStatut`.

## 3. Colonne "Durée Travailler (D.T)"

- Ajouter une colonne `D.T` exprimée en minutes.
- Cette durée représente le temps entre deux changements de statut.
- Le calcul démarre à partir du premier changement réel (pas de durée initiale au tout début).
- Ne pas compter les week-ends.
- Considérer uniquement les heures de travail : 08:00 à 16:00.
- Recalculer automatiquement après chaque modification.
- Calcul par intervalle (entre 2 statuts), sans cumul global dans cette colonne.
- Centraliser la logique de calcul dans un composant/service unique.

## 4. Paramétrage des alertes

Créer une table de paramétrage :

- `idStatut1`
- `idStatut2`
- `duree` (minutes cumulées entre les statuts 1 et 2)
- `alerte` (niveau/couleur)

Exemples de règles :

- Durée de passage entre 1 et 2 > 350 -> alerte rouge
- Durée de passage entre 1 et 2 > 200 -> alerte jaune
- Durée de passage entre 4 et 6 > 1000 -> alerte jaune
- Durée totale entre 1 et 10 > 10 000 -> alerte rouge

Duree cree jusqu'a terminer (Par exemple statut1 = 1, statut2 = 10)
On regarde pour chaque demande son statut, trier par date

## 5. Affichage des alertes

- Dans la liste des demandes, afficher uniquement celles qui ont au moins une alerte.
- Les demandes sans alerte ne doivent pas apparaître dans cette liste d'alertes.

## 6. API d'alerte (mention d'origine)

- Une API (mentionnée comme PHP dans la note) reçoit un numéro de demande via formulaire.
- L'API retourne la liste des alertes associées.

## 7. Points à clarifier

- Priorité des alertes si plusieurs seuils correspondent (jaune et rouge).
- Signification exacte de "status déjà passé" (historique pur ou transitions autorisées).
- Alignement technique entre backend actuel (Java/Spring) et mention d'API PHP.


Le php mandray idDemande, 
donc liste_demande avec information

A faire : script de reinitialisation sql