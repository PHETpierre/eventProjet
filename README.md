# eventProjet

# Warning
Ajout de objenesis dans les dépendances Maven.

# Command pour ajouter une partition au topic kafka
./kafka-topics.sh --alter --zookeeper localhost:2181 --topic step-chunk-send --partitions 2 <br>
./kafka-topics.sh --describe --zookeeper localhost:2181 --topic step-chunk-send

# API url
localhost:9623/lettre/getAll <br>
localhost:9623/lettre/addLettre <br>
localhost:7777/v1/jobcontroller/start <br>
