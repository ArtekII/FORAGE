<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des devis</title>
</head>
<body>
    <h1>Liste des devis</h1>

    <c:choose>
        <c:when test="${empty devis}">
            <p>Aucune devis trouvée.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <tr>
                    <th>ID</th>
                    <th>Client</th>
                    <th>Date devis</th>
                    <th>Montant total</th>
                </tr>
                <c:forEach var="dev" items="${devis}">
                    <tr>
                        <td>${dev.id}</td>
                        <td>${dev.client.nom} ${dev.client.prenom}</td>
                        <td>${dev.formattedDateEmission}</td>
                        <td><fmt:formatNumber value="${dev.montantTotal}" minFractionDigits="2" maxFractionDigits="2" /></td>
                        <td><a href="${pageContext.request.contextPath}/devis/details/${dev.id}">Voir les details</a></td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

    <a href="${pageContext.request.contextPath}/devis/create">Ajouter une devis</a>
    <a href="${pageContext.request.contextPath}/devis/pdf">
        Exporter en PDF
    </a>
</body>
</html>
