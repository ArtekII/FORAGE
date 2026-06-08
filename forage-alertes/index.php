<?php
$reference = $_POST['reference'] ?? null;
$resultat = null;
$message = null;

if ($reference) {
    $url = 'http://localhost:8080/Gestion_Forage/api/alertes/demandes';
    $json = file_get_contents($url);
    $demandes = json_decode($json, true);

    foreach ($demandes as $demande) {
        if (strcasecmp(trim($demande['reference']), trim($reference)) === 0) {
            $resultat = $demande;
            break;
        }
    }

    if (!$resultat) {
        $message = "Demande introuvable.";
    }
}
?>

<form method="post">
    <label>Numero de demande :</label>
    <input type="text" name="reference" required>
    <button type="submit">Rechercher</button>
</form>

<?php if ($message): ?>
    <p><?= htmlspecialchars($message) ?></p>
<?php endif; ?>

<?php if ($resultat): ?>
    <h2>Demande <?= htmlspecialchars($resultat['reference']) ?></h2>
    <ul>
        <li>Client : <?= htmlspecialchars($resultat['client']) ?></li>
        <li>Lieu : <?= htmlspecialchars($resultat['lieu']) ?></li>
        <li>Commune : <?= htmlspecialchars($resultat['commune']) ?></li>
        <li>Date : <?= htmlspecialchars($resultat['dateDemande']) ?></li>
    </ul>

    <h3>Alertes</h3>
    <table border="1" cellpadding="6">
        <tr>
            <th>Niveau</th>
            <th>Statut depart</th>
            <th>Statut arrivee</th>
            <th>Duree</th>
            <th>Seuil</th>
        </tr>

        <?php foreach ($resultat['alertes'] as $alerte): ?>
            <tr>
                <td><?= htmlspecialchars($alerte['niveau']) ?></td>
                <td><?= htmlspecialchars($alerte['statutDepart']) ?></td>
                <td><?= htmlspecialchars($alerte['statutArrivee']) ?></td>
                <td><?= htmlspecialchars($alerte['dureeMinutes']) ?></td>
                <td><?= htmlspecialchars($alerte['seuilMinutes']) ?></td>
            </tr>
        <?php endforeach; ?>
    </table>
<?php endif; ?>