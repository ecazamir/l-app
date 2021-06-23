#!/bin/sh
DOCKER_COMPOSE_VER=1.29.2
APP_REPOSITORY=https://github.com/ecazamir/l-app.git

sudo apt-add-repository ppa:ansible/ansible
sudo apt update
sudo apt install -y --no-install-recommends git ansible python3-pip
sudo pip3 install docker
sudo ansible-galaxy collection install community.general
# Creare cheie SSH locala
su - ubuntu -c 'SSH_KEY=~/.ssh/id_rsa; umask 077; if [ ! -d ~/.ssh -o ! -f ${SSH_KEY} ]; then ssh-keygen -b 2048 -t rsa -f ${SSH_KEY} -q -N ""; cat ${SSH_KEY}.pub > ~/.ssh/authorized_keys ; fi'
# Adauga cheie SSH pentru contul administrativ
su - ubuntu -c "echo 'ecdsa-sha2-nistp256 AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBNJ/uojfTXvAo3OwZHy0pWPXKx0pXwgH+7EZ25JP8KUqQB4SzXaFLsPhxuDka/EZ3W1BwbByOSwi5cygPZ2Y3l8= ecdsa 256-051621' >> ~/.ssh/authorized_keys"
# Copie cheia gazdei in lista de chei cunoscute 
su - ubuntu -c 'ssh-keyscan -t ecdsa localhost 2>/dev/null 1>~/.ssh/known_hosts'
sudo mkdir /app; chown ubuntu:ubuntu /app
# Clonare repository
su - ubuntu -c "cd /app; git clone ${APP_REPOSITORY}"
# Lansare Ansible
mkdir -p /app/logs
for PLAYBOOK in `cd /app/l-app/infra/ansible; ls playbook-*.yaml | sed -e s/playbook-// -e s/.yaml\$// | sort`; do
  (su - ubuntu -c "cd /app/l-app/infra/ansible; ansible-playbook playbook-${PLAYBOOK}.yaml") > /app/logs/playbook-${PLAYBOOK}.log
done
