# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|

  config.vm.box = "ubuntu/bionic64"

  config.vm.provider "virtualbox" do |vb|
     vb.memory = "4096"
  end
  
  config.vm.provision "shell", privileged: true, path: 'provision-root-priviledged.sh'
  config.vm.provision "shell", privileged: false, path: 'provision-user-priviledged.sh'

  config.vm.provision "Copy user's git config", type:'file', source: '~/.gitconfig', destination: '.gitconfig'


  config.vm.network "forwarded_port", guest: 8080, host: 8085, host_ip: "0.0.0.0", id: "spring_boot"
  config.vm.network "forwarded_port", guest: 8081, host: 8086, host_ip: "0.0.0.0", id: "spring_boot_2"
  config.vm.network "forwarded_port", guest: 8082, host: 8087, host_ip: "0.0.0.0", id: "spring_boot_3"
  config.vm.network "forwarded_port", guest: 27017, host: 27018, host_ip: "0.0.0.0", id: "mongo_db"
end
