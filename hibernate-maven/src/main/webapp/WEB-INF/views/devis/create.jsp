<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouveau Devis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/global.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/partials/navbar.jspf" %>
    <h1>Créer un devis</h1>

    <form action="${pageContext.request.contextPath}/devis/create" method="post" id="devisForm">
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

        <label for="typeId">Type de devis :</label>
        <select name="typeId" id="typeId" required>
            <option value="">Sélectionnez un type</option>
            <c:forEach var="type" items="${types}">
                <option value="${type.id}">${type.libelle}</option>
            </c:forEach>
        </select>

        <label for="dateEmission">Date du devis :</label>
        <input type="datetime-local" name="dateEmission" id="dateEmission" value="${datetime}">

        <label for="observation">Observation :</label>
        <input type="textarea" name="observation" id="observation" maxlength="255">

        <button type="button" id="btnAjouterDetail">Ajouter un détail</button>

        <fieldset id="detailFormContainer" style="margin-top: 12px;">
            <legend>Détails du devis</legend>
            <table border="1" cellpadding="4" cellspacing="0" style="width: 100%;">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Désignation</th>
                        <th>Quantité</th>
                        <th>Unité</th>
                        <th>Prix unitaire</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody id="detailsRows"></tbody>
            </table>
            <p id="detailsEmptyMessage" style="margin: 8px 0;">Aucun détail pour le moment.</p>
        </fieldset>

        <div id="detailsHiddenInputs"></div>

        <button type="submit">Créer le devis</button>
    </form>
    <a href="${pageContext.request.contextPath}/devis">Retour à la liste</a>

    <script>
        const contextPath = "${pageContext.request.contextPath}";

        const devisForm = document.getElementById("devisForm");
        const btnAjouterDetail = document.getElementById("btnAjouterDetail");
        const detailsRows = document.getElementById("detailsRows");
        const detailsEmptyMessage = document.getElementById("detailsEmptyMessage");
        const detailsHiddenInputs = document.getElementById("detailsHiddenInputs");

        const inputDemandeReference = document.getElementById("demandeReference");
        const inputDemandeId = document.getElementById("demandeId");
        const demandeStatus = document.getElementById("demandeStatus");
        const demandeClient = document.getElementById("demandeClient");
        const demandeDate = document.getElementById("demandeDate");
        const demandeLieu = document.getElementById("demandeLieu");

        function resetDemandeInfo(message, color) {
            inputDemandeId.value = "";
            demandeClient.textContent = "-";
            demandeDate.textContent = "-";
            demandeLieu.textContent = "-";
            demandeStatus.textContent = message || "";
            demandeStatus.style.color = color || "black";
        }

        async function loadDemandeByReference() {
            const reference = inputDemandeReference.value.trim();

            if (!reference) {
                resetDemandeInfo("Veuillez sélectionner une référence de demande.", "darkred");
                return;
            }

            demandeStatus.textContent = "Vérification en cours...";
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
                    resetDemandeInfo("Erreur serveur pendant la recherche de la référence.", "darkred");
                    return;
                }

                const data = await response.json();
                inputDemandeId.value = data.id || "";
                demandeClient.textContent = data.client || "-";
                demandeDate.textContent = data.dateDemande || "-";
                demandeLieu.textContent = data.lieu || "-";
                demandeStatus.textContent = "Demande trouvée et chargée.";
                demandeStatus.style.color = "green";
            } catch (error) {
                resetDemandeInfo("Erreur réseau pendant la vérification.", "darkred");
            }
        }

        function parsePositiveNumber(value) {
            const parsed = Number(value);
            if (!Number.isFinite(parsed) || parsed <= 0) {
                return null;
            }
            return parsed;
        }

        function refreshDetailIndexes() {
            const rows = detailsRows.querySelectorAll("tr");
            rows.forEach((row, index) => {
                const indexCell = row.querySelector(".detailIndex");
                if (indexCell) {
                    indexCell.textContent = String(index + 1);
                }
            });

            detailsEmptyMessage.style.display = rows.length === 0 ? "block" : "none";
        }

        function createDetailRow() {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td class="detailIndex"></td>
                <td><input type="text" class="detailDesignation" /></td>
                <td><input type="number" class="detailQuantite" step="0.01" min="0.01" /></td>
                <td><input type="text" class="detailUnite" /></td>
                <td><input type="number" class="detailPrixUnitaire" step="0.01" min="0.01" /></td>
                <td><button type="button" class="btnSupprimerDetail">Supprimer</button></td>
            `;
            detailsRows.appendChild(row);
            refreshDetailIndexes();
            return row;
        }

        inputDemandeReference.addEventListener("change", loadDemandeByReference);

        btnAjouterDetail.addEventListener("click", () => {
            const newRow = createDetailRow();
            const firstInput = newRow.querySelector(".detailDesignation");
            if (firstInput) {
                firstInput.focus();
            }
        });

        detailsRows.addEventListener("click", (event) => {
            const target = event.target;
            if (!target.classList.contains("btnSupprimerDetail")) {
                return;
            }

            const row = target.closest("tr");
            if (row) {
                row.remove();
            }
            refreshDetailIndexes();
        });

        devisForm.addEventListener("submit", (event) => {
            if (!inputDemandeId.value) {
                event.preventDefault();
                resetDemandeInfo("Référence invalide ou non chargée. Impossible de créer le devis.", "darkred");
                inputDemandeReference.focus();
                return;
            }

            detailsHiddenInputs.innerHTML = "";

            const rows = detailsRows.querySelectorAll("tr");
            if (rows.length === 0) {
                event.preventDefault();
                alert("Veuillez ajouter au moins un détail.");
                return;
            }

            for (let index = 0; index < rows.length; index++) {
                const row = rows[index];
                const designation = row.querySelector(".detailDesignation").value.trim();
                const unite = row.querySelector(".detailUnite").value.trim();
                const quantite = parsePositiveNumber(row.querySelector(".detailQuantite").value);
                const prixUnitaire = parsePositiveNumber(row.querySelector(".detailPrixUnitaire").value);

                if (!designation || !unite || quantite === null || prixUnitaire === null) {
                    event.preventDefault();
                    alert("Veuillez remplir correctement tous les champs du détail n°" + (index + 1) + ".");
                    return;
                }

                const fields = [
                    ["designation", designation],
                    ["quantite", quantite],
                    ["unite", unite],
                    ["prixUnitaire", prixUnitaire]
                ];

                fields.forEach(([name, value]) => {
                    const hiddenInput = document.createElement("input");
                    hiddenInput.type = "hidden";
                    hiddenInput.name = "details[" + index + "]." + name;
                    hiddenInput.value = String(value);
                    detailsHiddenInputs.appendChild(hiddenInput);
                });
            }
        });

        window.loadDemandeByReference = loadDemandeByReference;
    </script>
</body>
</html>
