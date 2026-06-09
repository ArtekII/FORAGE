<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alertes demandes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/global.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
    <h1>Alertes demandes</h1>

    <h2>Demandes en alerte</h2>
    <c:choose>
        <c:when test="${empty demandesAlertes}">
            <p>Aucune demande en alerte.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="demandeAlerte" items="${demandesAlertes}">
                <h3>Demande ${demandeAlerte.demande.reference}</h3>

                <ul class="demande-details">
                    <li><strong>Reference :</strong> ${demandeAlerte.demande.reference}</li>
                    <li><strong>Client :</strong> ${demandeAlerte.demande.client.nom} ${demandeAlerte.demande.client.prenom}</li>
                    <li><strong>Lieu :</strong> ${demandeAlerte.demande.lieu}</li>
                    <li><strong>Commune :</strong> ${demandeAlerte.demande.commune.libelle}</li>
                    <li><strong>Date demande :</strong> ${demandeAlerte.demande.formattedDateDemande}</li>
                </ul>

                <table border="1" cellpadding="6">
                    <tr>
                        <th>Niveau</th>
                        <th>Statut départ</th>
                        <th>Statut arrivée</th>
                        <th>Durée (min)</th>
                        <th>Intervalle (min)</th>
                    </tr>
                    <c:forEach var="alerte" items="${demandeAlerte.alertes}">
                        <tr>
                            <td>
                                <span class="alerte-niveau alerte-niveau-${alerte.niveau}">
                                    ${alerte.niveau}
                                </span>
                            </td>
                            <td>${alerte.statutDepartLibelle}</td>
                            <td>${alerte.statutArriveeLibelle}</td>
                            <td>${alerte.dureeMinutes}</td>
                            <td>${alerte.intervalleMinutes1} - ${alerte.intervalleMinutes2}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:forEach>
        </c:otherwise>
    </c:choose>

    <h2>Paramètres actifs</h2>
    <c:choose>
        <c:when test="${empty parametresAlertes}">
            <p>Aucun paramètre d'alerte défini.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="6">
                <tr>
                    <th>ID</th>
                    <th>Statut départ</th>
                    <th>Statut arrivée</th>
                    <th>Intervalle (min)</th>
                    <th>Niveau</th>
                </tr>
                <c:forEach var="parametre" items="${parametresAlertes}">
                    <tr>
                        <td>${parametre.id}</td>
                        <td>${parametre.statutDepart.libelle}</td>
                        <td>${parametre.statutArrivee.libelle}</td>
                        <td>${parametre.intervalleMinutes1} - ${parametre.intervalleMinutes2}</td>
                        <td>
                            <span class="alerte-niveau alerte-niveau-${parametre.niveau}">
                                ${parametre.niveau}
                            </span>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>
