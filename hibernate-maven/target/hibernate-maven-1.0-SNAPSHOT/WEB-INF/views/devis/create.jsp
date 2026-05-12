<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouveau Devis</title>
</head>
<body>
    <h1>Créer un devis</h1>

    <form action="${pageContext.request.contextPath}/devis/create" method="post" id="devisForm">
        <label for="demandeReference">Référence demande :</label>
        <input
            type="text"
            id="demandeReference"
            placeholder="Ex: DEM-20260512090100-ABC123"
            onblur="loadDemandeByReference()"
            required
        />
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
        <input type="text" name="observation" id="observation" maxlength="255">

        <button type="button" id="btnAfficherFormDetail">Ajouter un détail</button>

        <fieldset id="detailFormContainer" style="display: none; margin-top: 12px;">
            <legend>Nouveau détail</legend>

            <label for="designation">Désignation :</label>
            <input type="text" id="designation" />

            <label for="quantite">Quantité :</label>
            <input type="number" id="quantite" step="0.01" min="0.01" />

            <label for="unite">Unité :</label>
            <input type="text" id="unite" />

            <label for="prixUnitaire">Prix unitaire :</label>
            <input type="number" id="prixUnitaire" step="0.01" min="0.01" />

            <button type="button" id="btnCreerDetail">Créer le détail</button>
        </fieldset>

        <h3 style="margin-top: 14px;">Détails ajoutés</h3>
        <div id="detailsList">
            <p>Aucun détail pour le moment.</p>
        </div>

        <div id="detailsHiddenInputs"></div>

        <button type="submit">Créer le devis</button>
    </form>
    <a href="${pageContext.request.contextPath}/devis">Retour à la liste</a>

    <script>
        const contextPath = "${pageContext.request.contextPath}";

        const devisForm = document.getElementById("devisForm");
        const btnAfficherFormDetail = document.getElementById("btnAfficherFormDetail");
        const detailFormContainer = document.getElementById("detailFormContainer");
        const btnCreerDetail = document.getElementById("btnCreerDetail");
        const detailsList = document.getElementById("detailsList");
        const detailsHiddenInputs = document.getElementById("detailsHiddenInputs");

        const inputDemandeReference = document.getElementById("demandeReference");
        const inputDemandeId = document.getElementById("demandeId");
        const demandeStatus = document.getElementById("demandeStatus");
        const demandeClient = document.getElementById("demandeClient");
        const demandeDate = document.getElementById("demandeDate");
        const demandeLieu = document.getElementById("demandeLieu");

        const inputDesignation = document.getElementById("designation");
        const inputQuantite = document.getElementById("quantite");
        const inputUnite = document.getElementById("unite");
        const inputPrixUnitaire = document.getElementById("prixUnitaire");

        const details = [];

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
                resetDemandeInfo("Veuillez saisir une référence de demande.", "darkred");
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

        function renderDetails() {
            detailsList.innerHTML = "";
            detailsHiddenInputs.innerHTML = "";

            if (details.length === 0) {
                detailsList.innerHTML = "<p>Aucun détail pour le moment.</p>";
                return;
            }

            const table = document.createElement("table");
            table.border = "1";
            table.cellPadding = "4";
            table.cellSpacing = "0";

            const thead = document.createElement("thead");
            const headerRow = document.createElement("tr");
            const headers = ["#", "Désignation", "Quantité", "Unité", "Prix unitaire", "Montant", "Actions"];
            headers.forEach((label) => {
                const th = document.createElement("th");
                th.textContent = label;
                headerRow.appendChild(th);
            });
            thead.appendChild(headerRow);
            table.appendChild(thead);

            const tbody = document.createElement("tbody");

            details.forEach((detail, index) => {
                const row = document.createElement("tr");

                const indexCell = document.createElement("td");
                indexCell.textContent = String(index + 1);
                row.appendChild(indexCell);

                const designationCell = document.createElement("td");
                designationCell.textContent = detail.designation;
                row.appendChild(designationCell);

                const quantiteCell = document.createElement("td");
                quantiteCell.textContent = detail.quantite.toFixed(2);
                row.appendChild(quantiteCell);

                const uniteCell = document.createElement("td");
                uniteCell.textContent = detail.unite;
                row.appendChild(uniteCell);

                const prixCell = document.createElement("td");
                prixCell.textContent = detail.prixUnitaire.toFixed(2);
                row.appendChild(prixCell);

                const montantCell = document.createElement("td");
                montantCell.textContent = detail.montant.toFixed(2);
                row.appendChild(montantCell);

                const actionCell = document.createElement("td");
                const removeButton = document.createElement("button");
                removeButton.type = "button";
                removeButton.className = "btnSupprimerDetail";
                removeButton.setAttribute("data-index", String(index));
                removeButton.textContent = "Supprimer";
                actionCell.appendChild(removeButton);
                row.appendChild(actionCell);

                tbody.appendChild(row);

                const fields = [
                    ["designation", detail.designation],
                    ["quantite", detail.quantite],
                    ["unite", detail.unite],
                    ["prixUnitaire", detail.prixUnitaire]
                ];

                fields.forEach(([name, value]) => {
                    const hiddenInput = document.createElement("input");
                    hiddenInput.type = "hidden";
                    hiddenInput.name = "details[" + index + "]." + name;
                    hiddenInput.value = String(value);
                    detailsHiddenInputs.appendChild(hiddenInput);
                });
            });

            table.appendChild(tbody);
            detailsList.appendChild(table);
        }

        btnAfficherFormDetail.addEventListener("click", () => {
            detailFormContainer.style.display = "block";
            inputDesignation.focus();
        });

        inputDemandeReference.addEventListener("input", () => {
            resetDemandeInfo("Référence modifiée, vérifiez à nouveau en sortant du champ.", "darkorange");
        });

        btnCreerDetail.addEventListener("click", () => {
            const designation = inputDesignation.value.trim();
            const unite = inputUnite.value.trim();
            const quantite = parsePositiveNumber(inputQuantite.value);
            const prixUnitaire = parsePositiveNumber(inputPrixUnitaire.value);

            if (!designation || !unite || quantite === null || prixUnitaire === null) {
                alert("Veuillez remplir correctement tous les champs du détail.");
                return;
            }

            details.push({
                designation,
                unite,
                quantite,
                prixUnitaire,
                montant: quantite * prixUnitaire
            });

            renderDetails();

            inputDesignation.value = "";
            inputQuantite.value = "";
            inputUnite.value = "";
            inputPrixUnitaire.value = "";
            inputDesignation.focus();
        });

        detailsList.addEventListener("click", (event) => {
            const target = event.target;
            if (!target.classList.contains("btnSupprimerDetail")) {
                return;
            }

            const index = Number.parseInt(target.getAttribute("data-index"), 10);
            if (Number.isNaN(index)) {
                return;
            }

            details.splice(index, 1);
            renderDetails();
        });

        devisForm.addEventListener("submit", (event) => {
            if (!inputDemandeId.value) {
                event.preventDefault();
                resetDemandeInfo("Référence invalide. Impossible de créer le devis.", "darkred");
                inputDemandeReference.focus();
            }
        });

        window.loadDemandeByReference = loadDemandeByReference;
    </script>
</body>
</html>
