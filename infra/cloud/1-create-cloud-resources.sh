#!/bin/bash
echo "Acest script crează un proiect GCP și o mașină virtuală"
echo "Terminați această mașină virtuală în momentul în care nu mai aveți nevoie de ea,"
echo "  pentru a nu avea cheltuieli nedorite!"
echo ""
echo "Reamintire: dacă nu ați activat Cloud SDK, executați 'gcloud auth login' și reluați scriptul."
echo "Notă: Acest script folosește variabila \$RANDOM conform implementării din interpretorul bash"
echo "PROJECT_ID este stocat în fișierul ~/l-PROJECT_ID"
echo ""

PROJECT_NAME="Proof of concept"
INSTANCE_NAME=sdlc-vm

OS_FAMILY=ubuntu-os-cloud
OS_IMAGE=ubuntu-2004-focal-v20210510

PROJECT_ID_MARK=~/l-PROJECT_ID

if [ -f ~/l-PROJECT_ID ]; then
  echo "Se utilizează ID-ul de proiect salvat"
  PROJECT_ID=`cat ${PROJECT_ID_MARK}`
else
  echo "Se generează ID de proiect nou, va fi salvat în ${PROJECCT_ID_MARK}"
  PROJECT_ID=l-$RANDOM-$RANDOM-$RANDOM
  echo ${PROJECT_ID} > ${PROJECT_ID_MARK}
fi

export CLOUDSDK_CORE_PROJECT=${PROJECT_ID}

echo "Verific existența proiectului:"
if [ -z "`gcloud projects list | awk '{ print $1 }' | grep "${PROJECT_ID}"`" ]; then
  echo "Proiectul nu există."
  echo "Se crează proiectul ${PROJECT_NAME}, cu ID=${PROJECT_ID}"
  gcloud projects create ${PROJECT_ID} --name "${PROJECT_NAME}" --set-as-default
  echo "Se activează billing pentru proiect"
  BILLING_ACCOUNT_ID=`gcloud alpha billing accounts list | tail -1 | awk '{ print $1 }'`
  gcloud alpha billing projects link ${PROJECT_ID}  --billing-account ${BILLING_ACCOUNT_ID}
  echo "Se activează capabilitatea Compute Engine în proiect"
  gcloud services enable compute.googleapis.com
else
  echo "Proiectul există deja."
fi

echo "Se crează mașina virtuală..."
export INSTANCE_NAME=sdlc-vm
gcloud beta compute --project=${PROJECT_ID} instances create ${INSTANCE_NAME} \
  --zone=europe-central2-a --machine-type=n1-standard-2 \
  --boot-disk-size=20GB --boot-disk-type=pd-ssd --boot-disk-device-name=${INSTANCE_NAME}-boot-device \
  --image-project=${OS_FAMILY} --image=${OS_IMAGE} \
  --metadata-from-file startup-script=z-metadata-startup-script.sh \
  --subnet=default --network-tier=PREMIUM --tags=http-server,https-server 

# Reguli firewall - permisionare trafic HTTP și HTTPS
echo "Se crează regulile de firewall (http, https)"
gcloud compute --project=${PROJECT_ID} firewall-rules create default-allow-http \
  --direction=INGRESS --priority=1000 --network=default \
  --action=ALLOW --rules=tcp:80 --source-ranges=0.0.0.0/0 --target-tags=http-server
gcloud compute --project=${PROJECT_ID} firewall-rules create default-allow-https \
  --direction=INGRESS --priority=1000 --network=default \
  --action=ALLOW --rules=tcp:443 --source-ranges=0.0.0.0/0 --target-tags=https-server

echo "Pentru a vizualiza informații despre instanța VM (adresa IP), execută:"
echo "  gcloud compute instances list"
