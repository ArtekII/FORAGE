# Durée Travailler (D.T.) et alertes pour `demande-statut`

## Ce que cela implique

1. **Ajout d'une métrique métier**
   - La colonne `D.T` n’est pas une propriété simple de l’entité, elle représente un intervalle calculé entre deux changements de statut.
   - Elle doit être exprimée en minutes ouvrées, pas en minutes réelles.

2. **Calcul basé sur l’historique trié**
   - Pour chaque demande, il faut récupérer tous ses `StatutDemande` triés par `dateStatut`.
   - Le calcul du D.T. pour une ligne dépend du statut précédent.
   - La première ligne n’a pas de durée initiale, car on démarre à partir du premier changement réel.

3. **Règles de temps de travail**
   - Ne compter que les heures de travail : de 08:00 à 16:00.
   - Exclure les week-ends.
   - Si un intervalle s’étend sur plusieurs jours, il faut décomposer jour par jour et ne compter que les minutes ouvrées.
   - Exemple : du vendredi 15h00 au lundi 10h00 compte uniquement vendredi 15-16 et lundi 08-10.

4. **Recalcul après chaque modification**
   - Toute modification de date ou de statut peut changer plusieurs durées : la ligne modifiée, et potentiellement la ou les lignes suivantes.
   - Il faut centraliser ce calcul dans un service (CalculDureeTravail dans le dossier service) unique plutôt que le recalculer dans la vue ou dans plusieurs contrôleurs.

5. **Pas de cumul global dans la colonne**
   - La colonne `D.T` est une durée d’intervalle entre deux statuts, pas un total cumulatif.
   - Le total peut être calculé séparément si besoin, mais la colonne doit rester par intervalle.

6. **Implications pour la base de données**
   - Soit on stocke une colonne dédiée dans `statut_demande` (par exemple `duree_travail_minutes`), soit on calcule la valeur à la volée.
   - Si on stocke la valeur, il faut la maintenir à jour à chaque insertion ou modification.
   - Il est mieux de ne pas stocker

7. **Contraintes métier supplémentaires**
   - Le calcul doit être stable et centralisé : un service unique gère la logique des heures ouvrées et week-ends.
   - Les règles doivent pouvoir évoluer sans dupliquer de code.

## Relation avec les alertes (a ignorer pour le moment a revoir plus tard)

- La table de paramétrage d’alertes lie des paires de statuts à une durée seuil.
- Elle permet d’évaluer des durées entre des statuts définis (1→2, 4→6, 1→10, etc.).
- Pour chaque demande, on trie les statuts par date et on calcule les durées pertinentes.
- Si la durée dépasse le seuil paramétré, une alerte doit être générée.

## Impact sur les vues

- La liste de demande-statut doit pouvoir afficher le D.T. pour chaque intervalle.
- La liste des alertes doit filtrer les demandes qui dépassent au moins une règle.
- Les formules de modification doivent mettre à jour les D.T. liés immédiatement.
