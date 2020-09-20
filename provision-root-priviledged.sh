#!/bin/sh

apt-get install language-pack-en
locale-gen en_GB.UTF-8

apt-get update
apt-get -y upgrade

apt-get install -y git openjdk-11-jdk

apt-get -y install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -

add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
    $(lsb_release -cs) \
    stable"

apt-get update

apt-get -y install docker-ce docker-compose

usermod -aG docker vagrant

apt-get -y autoremove
