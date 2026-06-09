#!/usr/bin/env bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/hibernate-maven" && pwd)"
WAR_NAME="hibernate-maven-1.0-SNAPSHOT.war"
WAR_PATH="$PROJECT_DIR/target/$WAR_NAME"
DEPLOY_DIR="/home/itu/tomcat/tomcat/webapps"
DEPLOY_NAME="Gestion_Forage.war"
BUILD_BEFORE_DEPLOY="true"

if [[ "$BUILD_BEFORE_DEPLOY" == "true" ]]; then
  echo "[INFO] Build du WAR..."
  mvn -q -DskipTests package -f "$PROJECT_DIR/pom.xml"
fi

if [[ ! -f "$WAR_PATH" ]]; then
  echo "[ERREUR] WAR introuvable: $WAR_PATH"
  exit 1
fi

if [[ ! -d "$DEPLOY_DIR" ]]; then
  echo "[ERREUR] Dossier de deploiement introuvable: $DEPLOY_DIR"
  exit 1
fi

echo "[INFO] Copie du WAR vers $DEPLOY_DIR/$DEPLOY_NAME"
cp "$WAR_PATH" "$DEPLOY_DIR/$DEPLOY_NAME"

echo "[OK] Deploiement termine."
