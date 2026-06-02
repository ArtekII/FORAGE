# Architecture proposée pour `demande-statut`, D.T. et alertes

## Objectif

Structurer le projet pour gérer :
- le calcul de la durée de travail entre deux statuts,
- le stockage ou le calcul des D.T.,
- le paramétrage des alertes sur des paires ou des parcours de statuts.

## Composants principaux

### 1. Entité `StatutDemande`

Champs conseillés :
- `id`
- `demande` (relation vers `Demande`)
- `statut` (relation vers `Statut`)
- `dateStatut`
- `observation`
- `dureeTravailMinutes` (optionnel si on stocke le résultat)

> Si on stocke `dureeTravailMinutes`, il faut la recalculer automatiquement à chaque insertion ou modification.

### 2. Entité `AlerteStatut`

Champs :
- `id`
- `statutDepart` (`idStatut1`)
- `statutArrivee` (`idStatut2`)
- `dureeSeuil` (minutes)
- `niveau` (texte / couleur / type)
- `typeAlerte` (intervalle simple vs parcours total)

Cette table permet de paramétrer des règles extensibles et de distinguer :
- intervalle simple `1 → 2`
- parcours total `1 → 10`

### 3. Service `WorkingTimeCalculator`

Responsabilité :
- calculer les minutes ouvrées entre deux `LocalDateTime`.
- exclure les weekends.
- ne compter que la plage 08:00–16:00.

Méthodes clés :
- `long calculateWorkingMinutes(LocalDateTime start, LocalDateTime end)`
- `boolean isWorkingDay(LocalDate date)`
- `LocalDateTime clampToWorkHours(LocalDateTime date)`

### 4. Service `StatutDemandeDurationService`

Responsabilité :
- calculer les D.T. pour une demande donnée.
- recalculer les durées après mise à jour de l’historique.

Méthodes :
- `List<StatutDemandeDto> computeDurationsForDemande(Long demandeId)`
- `void recalculateDurations(Long demandeId)`
- `void recalculateDurationsAfterUpdate(StatutDemande statutDemande)`

Ce service doit :
- récupérer les `StatutDemande` triés par date,
- calculer chaque intervalle en appelant `WorkingTimeCalculator`,
- mettre à jour les valeurs si elles sont stockées,
- retourner des DTOs pour les vues.

### 5. Service `AlertService`

Responsabilité :
- lire les règles d’alerte paramétrées,
- évaluer chaque demande sur l’historique de ses statuts,
- déterminer si une alerte s’applique.

Méthodes :
- `List<AlerteResult> evaluateAlertsForDemande(Long demandeId)`
- `List<Demande> getDemandesWithAlerts()`

### 6. Contrôleur / API

Points d’accès proposés :
- `GET /demande-statut` : liste des statuts
- `GET /demande-statut/create`
- `GET /demande-statut/edit/{id}`
- `POST /demande-statut/create`
- `POST /demande-statut/edit`
- `GET /alerts?demandeId=...` : retourne alertes pour une demande
- `GET /alertes` : liste des demandes en alerte

### 7. Vues

- Vue `demande-statut/list` : afficher chaque ligne, éventuellement colonne D.T.
- Vue `demande-statut/edit` : modification avec statuts filtrés.
- Vue `alerts/list` : demandes avec alerte uniquement.
- Vue optionnelle `demande-statut/history` : historique trié et D.T. par intervalle.

## Propositions de structure de packages

- `model` : `Demande`, `Statut`, `StatutDemande`, `AlerteStatut`
- `repository` : `StatutDemandeRepository`, `AlerteStatutRepository`
- `service` :
  - `DemandeStatutService`
  - `WorkingTimeCalculator`
  - `StatutDemandeDurationService`
  - `AlertService`
- `controller` :
  - `DemandeStatusController`
  - `AlertController`
  - `DevisApiController` (API existante)
- `dto` (optionnel) : `StatutDemandeDto`, `AlerteResult`

## Exemple de flux de calcul

1. Création/édition d’un `StatutDemande`.
2. Appel de `StatutDemandeDurationService.recalculateDurations(demandeId)`.
3. Le service charge tous les statuts pour la demande, triés par date.
4. Il calcule chaque D.T. via `WorkingTimeCalculator`.
5. Il stocke ou retourne les durées.
6. Il appelle `AlertService` si des alertes doivent être recalculées.

## Exemple de DTO pour la vue

```java
public class StatutDemandeDto {
    private Long id;
    private String referenceDemande;
    private String statutLibelle;
    private LocalDateTime dateStatut;
    private long dureeTravailMinutes;
    private String observation;
}
```

## Remarque importante

- Si `StatutDemande.dateStatut` reste `updatable = false`, la modification de date via formulaire ne fonctionnera pas correctement.
- Il faudra probablement rendre ce champ modifiable si l’on veut gérer les corrections d’historique.
