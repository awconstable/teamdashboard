# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|

  config.vm.box = "ubuntu/trusty64"

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

    sudo add-apt-repository -y ppa:webupd8team/java
    sudo apt-get update
    sudo apt-get -y upgrade
    echo debconf shared/accepted-oracle-license-v1-1 select true | sudo debconf-set-selections 
    echo debconf shared/accepted-oracle-license-v1-1 seen true | sudo debconf-set-selections
    sudo apt-get -y install oracle-java8-installer

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

    sudo apt-get -y install docker-ce

    sudo usermod -aG docker vagrant

    sudo apt-get -y autoremove

  SHELL

  config.vm.network "forwarded_port", guest: 8080, host: 8080, host_ip: "0.0.0.0", id: "spring_boot"
end
