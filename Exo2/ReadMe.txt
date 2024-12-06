
#compilation

javac *.java



#test sur une meme machine avec 4 noeuds/utilisateurs

java NoeudTchat 5001 toto

java NoeudTchat 5002 bidule 127.0.0.1:5001

java NoeudTchat 5003 machin 127.0.0.1:5001,127.0.0.1:5002

java NoeudTchat 5004 truc 127.0.0.1:5001,127.0.0.1:5002,127.0.0.1:5003


