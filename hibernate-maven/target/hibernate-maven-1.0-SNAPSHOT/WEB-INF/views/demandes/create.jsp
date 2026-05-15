<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouvelle Demande</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/global.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
    <h1>Créer une demande</h1>

    <form action="${pageContext.request.contextPath}/demandes/create" method="post">
        <label for="reference">Référence :</label>
        <input type="text" id="reference" name="reference" value="${references}" readonly><br><br>

        <label for="clientId">Client :</label>
        <select name="clientId" id="clientId" required>
            <option value="">Sélectionnez un client</option>
            <c:forEach var="client" items="${clients}">
                <option value="${client.id}">${client.nom} ${client.prenom}</option>
            </c:forEach>
        </select>

        <label for="lieu">Lieu :</label>
        <input type="text" name="lieu" id="lieu">

        <label for="communeId">Commune :</label>
        <select name="communeId" id="communeId" required>
            <option value="">Sélectionnez une commune</option>
            <c:forEach var="commune" items="${communes}">
                <option value="${commune.id}">${commune.libelle}</option>
            </c:forEach>
        </select>

        <label for="dateDemande">Date de la demande :</label>
        <input type="datetime-local" name="dateDemande" id="dateDemande" value="${datetime}">
        
        <button type="submit">Creer une demande</button>
    </form>
    <a href="${pageContext.request.contextPath}/demandes">Retour à la liste</a>
</body>
</html>
