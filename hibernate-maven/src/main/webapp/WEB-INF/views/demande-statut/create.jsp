<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des demandes de statut</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/global.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/partials/navbar.jspf" %>

    <h1>Créer une demande de statut</h1>
    <form action="${pageContext.request.contextPath}/demande-statut/create" method="post">
        <label for="demandeReference">Référence demande :</label>
        <select id="demandeReference" required>
            <option value="">Sélectionnez une référence de demande</option>
            <c:forEach var="demande" items="${demandes}">
                <option value="${demande.reference}">${demande.reference}</option>
            </c:forEach>
        </select>
        <input type="hidden" name="demandeId" id="demandeId" required>
        <p id="demandeStatus" style="margin: 8px 0;"></p>

        <fieldset style="margin: 10px 0;">
            <legend>Informations demande</legend>
            <p><strong>Client :</strong> <span id="demandeClient">-</span></p>
            <p><strong>Date :</strong> <span id="demandeDate">-</span></p>
            <p><strong>Lieu :</strong> <span id="demandeLieu">-</span></p>
        </fieldset>

        <label for="dateDemande">Date de la demande :</label>
        <input type="datetime-local" name="dateDemande" id="dateDemande" value="${datetime}">

        <label for="statutId">Statut :</label>
        <select name="statutId" id="statutId" required>
            <option value="">Sélectionnez un statut</option>
            <c:forEach var="statut" items="${statuts}">
                <option value="${statut.id}">${statut.libelle}</option>
            </c:forEach>
        </select>
        
        <label for="observation">Observation :</label>
        <textarea name="observation" id="observation"></textarea>

        <button type="submit">Créer une demande de statut</button>
</body>
</html>