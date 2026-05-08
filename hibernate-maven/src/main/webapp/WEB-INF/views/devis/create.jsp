<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouvelle Devis</title>
</head>
<body>
    <h1>Créer une devis</h1>

    <form action="${pageContext.request.contextPath}/devis/create" method="post" id="devisForm">

        <label for="clientId">Client :</label>
        <select name="clientId" id="clientId" required>
            <option value="">Sélectionnez un client</option>
            <c:forEach var="client" items="${clients}">
                <option value="${client.id}">${client.nom} ${client.prenom}</option>
            </c:forEach>
        </select>

        <label for="dateEmission">Date du devis :</label>
        <input type="datetime-local" name="dateEmission" id="dateEmission" value="${datetime}">

        <button type="button" id="btnAfficherFormDetail">Ajouter un detail</button>

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

        <button type="submit">Creer une devis</button>
    </form>
    <a href="${pageContext.request.contextPath}/devis">Retour à la liste</a>

    <script>
        const btnAfficherFormDetail = document.getElementById("btnAfficherFormDetail");
        const detailFormContainer = document.getElementById("detailFormContainer");
        const btnCreerDetail = document.getElementById("btnCreerDetail");
        const detailsList = document.getElementById("detailsList");
        const detailsHiddenInputs = document.getElementById("detailsHiddenInputs");

        const inputDesignation = document.getElementById("designation");
        const inputQuantite = document.getElementById("quantite");
        const inputUnite = document.getElementById("unite");
        const inputPrixUnitaire = document.getElementById("prixUnitaire");

        const details = [];

        function renderDetails() {
            if (details.length === 0) {
                detailsList.innerHTML = "<p>Aucun détail pour le moment.</p>";
                detailsHiddenInputs.innerHTML = "";
                return;
            }

            let html = "<table border='1' cellpadding='4' cellspacing='0'><thead><tr><th>#</th><th>Désignation</th><th>Quantité</th><th>Unité</th><th>Prix unitaire</th><th>Montant</th><th>Actions</th></tr></thead><tbody>";
            let hidden = "";

            details.forEach((detail, index) => {
                html += "<tr>" +
                    "<td>" + (index + 1) + "</td>" +
                    "<td>" + detail.designation + "</td>" +
                    "<td>" + detail.quantite.toFixed(2) + "</td>" +
                    "<td>" + detail.unite + "</td>" +
                    "<td>" + detail.prixUnitaire.toFixed(2) + "</td>" +
                    "<td>" + detail.montant.toFixed(2) + "</td>" +
                    "<td><button type='button' class='btnSupprimerDetail' data-index='" + index + "'>Supprimer</button></td>" +
                    "</tr>";

                hidden += "<input type='hidden' name='details[" + index + "].designation' value='" + detail.designation + "'>";
                hidden += "<input type='hidden' name='details[" + index + "].quantite' value='" + detail.quantite + "'>";
                hidden += "<input type='hidden' name='details[" + index + "].unite' value='" + detail.unite + "'>";
                hidden += "<input type='hidden' name='details[" + index + "].prixUnitaire' value='" + detail.prixUnitaire + "'>";
            });

            html += "</tbody></table>";
            detailsList.innerHTML = html;
            detailsHiddenInputs.innerHTML = hidden;
        }

        btnAfficherFormDetail.addEventListener("click", () => {
            detailFormContainer.style.display = "block";
            inputDesignation.focus();
        });

        btnCreerDetail.addEventListener("click", () => {
            const designation = inputDesignation.value.trim();
            const unite = inputUnite.value.trim();
            const quantite = parseFloat(inputQuantite.value);
            const prixUnitaire = parseFloat(inputPrixUnitaire.value);

            if (!designation || !unite || Number.isNaN(quantite) || Number.isNaN(prixUnitaire) || quantite <= 0 || prixUnitaire <= 0) {
                alert("Veuillez remplir correctement tous les champs du détail.");
                return;
            }

            const detail = {
                designation,
                unite,
                quantite,
                prixUnitaire,
                montant: quantite * prixUnitaire
            };

            details.push(detail);
            renderDetails();

            inputDesignation.value = "";
            inputQuantite.value = "";
            inputUnite.value = "";
            inputPrixUnitaire.value = "";
            inputDesignation.focus();
        });

        detailsList.addEventListener("click", (event) => {
            if (!event.target.classList.contains("btnSupprimerDetail")) {
                return;
            }

            const index = parseInt(event.target.getAttribute("data-index"), 10);
            if (Number.isNaN(index)) {
                return;
            }

            details.splice(index, 1);
            renderDetails();
        });
    </script>
</body>
</html>
