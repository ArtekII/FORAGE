<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des demandes</title>
</head>
<body>
    <h1>Liste des demandes</h1>
    <form method="get" action="${pageContext.request.contextPath}/demandes">

        <label for="reference">Rerchercher par reference</label>
        <input type="text" name="reference">

        <label for="clientId"></label>
        <select name="clientId" id="clientId">
            <option value="">Sélectionnez un client</option>
            <c:forEach var="client" items="${clients}">
                <option value="${client.id}">${client.nom} ${client.prenom}</option>
            </c:forEach>
        </select>

        <label for="lieu">Lieu :</label>
        <input type="text" name="lieu" id="lieu">

        <label for="communeId">Commune :</label>
        <select name="communeId" id="communeId">
            <option value="">Sélectionnez une commune</option>
            <c:forEach var="commune" items="${communes}">
                <option value="${commune.id}">${commune.libelle}</option>
            </c:forEach>
        </select>

        <label for="dateDebut">Date debut :</label>
        <input type="datetime-local" name="dateDebut">

        <label for="dateFin">Date fin :</label>
        <input type="datetime-local" name="dateFin">

        <button type="submit">Filtrer</button>

    </form>


    <c:choose>
        <c:when test="${empty demandes}">
            <p>Aucune demande trouvée.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <tr>
                    <th>ID</th>
                    <th>Référence</th>
                    <th>Client</th>
                    <th>Lieu</th>
                    <th>Commune</th>
                    <th>Date demande</th>
                </tr>
                <c:forEach var="demande" items="${demandes}">
                    <tr>
                        <td>${demande.id}</td>
                        <td>${demande.reference}</td>
                        <td>${demande.client.nom} ${demande.client.prenom}</td>
                        <td>${demande.lieu}</td>
                        <td>${demande.commune.libelle}</td>
                        <td>${demande.formattedDateDemande}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

    <a href="${pageContext.request.contextPath}/demandes/create">Ajouter une demande</a>
</body>
</html>
