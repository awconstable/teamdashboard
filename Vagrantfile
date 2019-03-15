# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|

  config.vm.box = "ubuntu/cosmic64"

  config.vm.provider "virtualbox" do |vb|
     vb.gui = true
  
     vb.memory = "4096"
  end
  
  config.vm.provision "shell", inline: <<-SHELL
    
    #sudo echo "LANG=en_GB.UTF-8" >> /etc/environment
    #sudo echo "LANGUAGE=en_GB.UTF-8" >> /etc/environment
    #sudo echo "LC_ALL=en_GB.UTF-8" >> /etc/environment
    #sudo echo "LC_CTYPE=en_GB.UTF-8" >> /etc/environment

    sudo apt-get install language-pack-en
    sudo locale-gen en_GB.UTF-8

    sudo apt-get update
    sudo apt-get -y upgrade
    sudo apt-get -y install openjdk-11-jdk

    sudo apt-get install -y maven git

    sudo apt-get -y install \
        apt-transport-https \
        ca-certificates \
        curl \
        software-properties-common

    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

    sudo add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
       $(lsb_release -cs) \
       stable"

    sudo apt-get update

    sudo apt-get -y install docker-ce dpcker-compose

    sudo usermod -aG docker vagrant

    sudo apt-get -y autoremove

  SHELL

  config.vm.network "forwarded_port", guest: 80, host: 80, host_ip: "0.0.0.0", id: "nginx"
  config.vm.network "forwarded_port", guest: 8080, host: 8080, host_ip: "0.0.0.0", id: "spring_boot"
  config.vm.network "forwarded_port", guest: 8081, host: 8081, host_ip: "0.0.0.0", id: "spring_boot_2"
  config.vm.network "forwarded_port", guest: 8082, host: 8082, host_ip: "0.0.0.0", id: "spring_boot_3"
  config.vm.network "forwarded_port", guest: 27017, host: 27017, host_ip: "0.0.0.0", id: "mongo_db"
end
