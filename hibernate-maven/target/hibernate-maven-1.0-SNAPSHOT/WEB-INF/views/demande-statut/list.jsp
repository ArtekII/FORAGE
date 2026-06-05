<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des demandes de statut</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/global.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
    <h1>Liste des demandes de statut</h1>

    <c:choose>
        <c:when test="${empty demandeStatuts}">
            <p>Aucune demande de statut trouvée.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <tr>
                    <th>ID</th>
                    <th>Demande</th>
                    <th>Client</th>
                    <th>Statut</th>
                    <th>Date statut</th>
                    <th>D.T. (min)</th>
                    <th>Observation</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="status" items="${demandeStatuts}">
                    <tr>
                        <td>${status.id}</td>
                        <td>${status.demande.reference}</td>
                        <td>${status.demande.client.nom} ${status.demande.client.prenom}</td>
                        <td>${status.statut.libelle}</td>
                        <td>${status.formattedDateStatut}</td>
                        <td>${status.formattedDureeTravail}</td>
                        <td>${status.observation}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/demande-statut/edit/${status.id}">Modifier</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

    <a href="${pageContext.request.contextPath}/demande-statut/create">Ajouter demande statut</a>
</body>
</html>
