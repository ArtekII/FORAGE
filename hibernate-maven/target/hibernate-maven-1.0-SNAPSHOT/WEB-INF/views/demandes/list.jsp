<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des demandes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/global.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
    <h1>Liste des demandes</h1>
    <form method="get" action="${pageContext.request.contextPath}/demandes" class="filter-form">
        <div class="filter-form-header">
            <h2>Filtrer les demandes</h2>
            <p>Affinez la liste selon la référence, le client, le lieu, la commune ou la période.</p>
        </div>

        <div class="filter-grid">
            <div class="form-field">
                <label for="reference">Référence</label>
                <input type="text" name="reference" id="reference" value="${param.reference}">
            </div>

            <div class="form-field">
                <label for="clientId">Client</label>
                <select name="clientId" id="clientId">
                    <option value="">Tous les clients</option>
                    <c:forEach var="client" items="${clients}">
                        <option value="${client.id}" ${param.clientId == client.id ? 'selected' : ''}>
                            ${client.nom} ${client.prenom}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-field">
                <label for="lieu">Lieu</label>
                <input type="text" name="lieu" id="lieu" value="${param.lieu}">
            </div>

            <div class="form-field">
                <label for="communeId">Commune</label>
                <select name="communeId" id="communeId">
                    <option value="">Toutes les communes</option>
                    <c:forEach var="commune" items="${communes}">
                        <option value="${commune.id}" ${param.communeId == commune.id ? 'selected' : ''}>
                            ${commune.libelle}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-field">
                <label for="dateDebut">Date début</label>
                <input type="datetime-local" name="dateDebut" id="dateDebut" value="${param.dateDebut}">
            </div>

            <div class="form-field">
                <label for="dateFin">Date fin</label>
                <input type="datetime-local" name="dateFin" id="dateFin" value="${param.dateFin}">
            </div>
        </div>

        <div class="filter-actions">
            <button type="submit">Appliquer les filtres</button>
            <a class="btn-link subtle" href="${pageContext.request.contextPath}/demandes">Réinitialiser</a>
        </div>
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
