<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouvelle Demande</title>
</head>
<body>
    <h1>Créer une demande</h1>
    <p>Formulaire en cours d'implémentation.</p>

    <form action="${pageContext.request.contextPath}/demandes/create" method="post">
        <label for="reference">Référence :</label>
        <input type="text" id="reference" name="reference" value="${references}" readonly><br><br>

        <label for="clientId">Client :</label>
        <select name="clientId" id="clientId" required>
            <option value="">Sélectionnez un client</option>
            ${clients.forEach(client -> {
                <option value="${client.getId()}">${client.getNom()} ${client.getPrenom()}</option>
            })}
        </select>

        <label for="communeId">Commune :</label>
        <select name="communeId" id="communeId" required>
            <option value="">Sélectionnez une commune</option>
            ${communes.forEach(commune -> {
                <option value="${commune.getId()}">${commune.getLibelle()}</option>
            })}
        </select>
    </form>
    <a href="${pageContext.request.contextPath}/demandes">Retour à la liste</a>
</body>
</html>
