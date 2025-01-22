#Exclusion mutuelle sur un anneau a jeton


# Compilation 

javac = *.java


#Lancement dispositif
#parametres : numero_port_ecoute taille_max_message

java  Dispositif 5000 100


#Lancement de 3 sites (ici, le site 1 cree le jeton)
#parametres : 
# adresse_IP_dispositif
# numero_port_dispositif
# numero_port_serveur_local
# adresse_IP_successeur
# numero_port_successeur
# booleen_creation_jeton

java ProgrammeSite 127.0.0.1 5000 5001 127.0.0.1 5002 true

java ProgrammeSite 127.0.0.1 5000 5002 127.0.0.1 5003 false

java ProgrammeSite 127.0.0.1 5000 5003 127.0.0.1 5001 false

# ici test en local, mais les programmes (dispositif et les sites) peuvent tous etre executes sur des machines differentes




