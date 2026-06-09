<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails du devis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/global.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
    <h1>Détails du devis #${devis.id}</h1>

    <p><strong>Client :</strong> ${devis.client.nom} ${devis.client.prenom}</p>
    <p><strong>Date d'émission :</strong> ${devis.formattedDateEmission}</p>
    <p><strong>Montant total :</strong> <fmt:formatNumber value="${devis.montantTotal}" minFractionDigits="2" maxFractionDigits="2" /></p>

    <h2>Lignes de détail</h2>
    <c:choose>
        <c:when test="${empty devis.details}">
            <p>Aucune ligne de détail pour ce devis.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <tr>
                    <th>#</th>
                    <th>Désignation</th>
                    <th>Quantité</th>
                    <th>Unité</th>
                    <th>Prix unitaire</th>
                    <th>Montant ligne</th>
                </tr>
                <c:forEach var="det" items="${devis.details}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${det.designation}</td>
                        <td><fmt:formatNumber value="${det.quantite}" minFractionDigits="2" maxFractionDigits="2" /></td>
                        <td>${det.unite}</td>
                        <td><fmt:formatNumber value="${det.prixUnitaire}" minFractionDigits="2" maxFractionDigits="2" /></td>
                        <td><fmt:formatNumber value="${det.montantParLigne}" minFractionDigits="2" maxFractionDigits="2" /></td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

    <p><a href="${pageContext.request.contextPath}/devis">Retour à la liste des devis</a></p>
</body>
</html>
