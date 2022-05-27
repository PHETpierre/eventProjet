# eventProjet

#Warning
Ajout de objenesis dans les dépendances Maven.

#Command pour ajouter une partition au topic kafka
./kafka-topics.sh --alter --zookeeper localhost:2181 --topic step-chunk-send --partitions 2
./kafka-topics.sh --describe --zookeeper localhost:2181 --topic step-chunk-send
