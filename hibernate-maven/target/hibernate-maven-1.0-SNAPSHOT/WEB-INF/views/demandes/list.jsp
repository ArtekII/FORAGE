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
                        <td>${demande.dateDemande}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>
