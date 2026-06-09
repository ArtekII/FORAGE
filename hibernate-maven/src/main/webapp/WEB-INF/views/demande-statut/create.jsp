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

        <label for="dateChangement">Date d'ajout de statut de la demande :</label>
        <input type="datetime-local" name="dateChangement" id="dateChangement" value="${datetime}">

        <label for="statutId">Statut :</label>
        <select name="statutId" id="statutId" required>
            <option value="">Sélectionnez un statut</option>
            <c:forEach var="statut" items="${statuts}">
                <option value="${statut.id}">${statut.libelle}</option>
            </c:forEach>
        </select>
        
        <label for="observation">Observation :</label>
        <textarea name="observation" id="observation"></textarea>

        <br>
        <button type="submit">Créer une demande de statut</button>
    </form>

    <script>
        const contextPath = "${pageContext.request.contextPath}";

        const demandeReference = document.getElementById("demandeReference");
        const demandeIdInput = document.getElementById("demandeId");
        const demandeStatus = document.getElementById("demandeStatus");
        const demandeClient = document.getElementById("demandeClient");
        const demandeDate = document.getElementById("demandeDate");
        const demandeLieu = document.getElementById("demandeLieu");
        const demandeForm = document.querySelector("form");

        function resetDemandeInfo(message, color) {
            demandeIdInput.value = "";
            demandeClient.textContent = "-";
            demandeDate.textContent = "-";
            demandeLieu.textContent = "-";
            demandeStatus.textContent = message || "";
            demandeStatus.style.color = color || "black";
        }

        async function loadDemandeInfo() {
            const reference = demandeReference.value.trim();

            if (!reference) {
                resetDemandeInfo("Veuillez sélectionner une référence de demande.", "darkred");
                return;
            }

            demandeStatus.textContent = "Chargement des informations...";
            demandeStatus.style.color = "black";

            try {
                const response = await fetch(
                    contextPath + "/devis/demande-by-reference?reference=" + encodeURIComponent(reference),
                    { method: "GET", headers: { "Accept": "application/json" } }
                );

                if (response.status === 404) {
                    resetDemandeInfo("Référence introuvable.", "darkred");
                    return;
                }

                if (!response.ok) {
                    resetDemandeInfo("Erreur serveur pendant la recherche de la demande.", "darkred");
                    return;
                }

                const data = await response.json();
                demandeIdInput.value = data.id || "";
                demandeClient.textContent = data.client || "-";
                demandeDate.textContent = data.dateDemande || "-";
                demandeLieu.textContent = data.lieu || "-";
                demandeStatus.textContent = "Demande chargée.";
                demandeStatus.style.color = "green";

            } catch (error) {
                resetDemandeInfo("Erreur réseau pendant la vérification.", "darkred");
            }
        }

        demandeReference.addEventListener("change", loadDemandeInfo);
        demandeReference.addEventListener("blur", loadDemandeInfo);

        demandeForm.addEventListener("submit", function (event) {
            if (!demandeIdInput.value) {
                event.preventDefault();
                resetDemandeInfo("Veuillez sélectionner une demande valide avant de soumettre.", "darkred");
                demandeReference.focus();
            }
        });
    </script>
</body>
</html>