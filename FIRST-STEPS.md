### Descarga kafka
Para descargar apache kafka debes acceder a la siguiente url : 

https://kafka.apache.org/downloads  

Asegurate de descargar la versión que dice binary downloads.


### Inicia kafka

Para iniciar tu servidor de kafka deberás ejecutar los siguientes comandos:

```
bin/zookeeper-server-start.sh config/zookeeper.properties
```

```
bin/kafka-server-start.sh config/server.properties
```

Esto iniciará tanto zookeeper como kafka.



### Creando un topic

Los mensajes se procesan en topics, para crear uno deberás ejecutar el siguiente comando:

```
bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic devs4j-topic --partitions 5 -- replication-factor 1
```

Este comando recibe los siguientes parámetros:

- bootstrap-server = Kafka server
- topic = Nombre del topic a crear
- partitions = Número de particiones
- replication-factor = Número de réplicas por broker

### Listando topics
Puedes listar los topics disponibles ejecutando:
```
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

Salida de ejemplo:
    *devs4j-topic*

### Ver definición de un topic
Si deseas consultar como se definió un topic puedes describirlo con el siguiente comando:

```
bin/kafka-topics.sh --describe --topic devs4j-topic --bootstrap-server localhost:9092
```


### Crear un producer
Para iniciar un producer ejecutaremos el siguiente comando:

```
bin/kafka-console-producer.sh --topic devs4j-topic --bootstrap-server localhost:9092
```

### Crear un consumer
Para iniciar un consumer ejecutaremos el siguiente comando:
```
bin/kafka-console-consumer.sh --topic devs4j-topic --from-beginning --bootstrap-server localhost:9092 --property print.key=true --property key.separator="-"
```

El parámetro ```--from-beginning``` permite especificar si queremos recibir solo los mensajes nuevos o queremos leer todos desde el inicio.

### Modificando un topic
Para modificar un topic existente ejecutaremos el siguiente comando:
```
bin/kafka-topics.sh --bootstrap-server localhost:9092 --alter --topic devs4j-topic --partitions 40
```
- Nota: Es importante saber que los topicos se pueden subir el numero de particiones pero no se pueden disminuir en numero

### Agregando configuraciones
Para modificar las configuraciones de un topic existente ejecutaremos el siguiente comando:
```
bin/kafka-configs.sh --bootstrap-server localhost:9092 --entity-type topics --entity-name devs4j-topic --alter --add-config x=y
```

### Quitando configuraciones
Para remover las configuraciones de un topic existente ejecutaremos el siguiente comando:

Configuraciones >> https://kafka.apache.org/documentation/#topicconfigs
```
bin/kafka-configs.sh --bootstrap-server localhost:9092 --entity-type topics --entity-name devs4j-topic --alter --delete-config x
```

### Borrando un topic
Para borrar un topic existente ejecutaremos el siguiente comando:
```
bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic devs4j-topic
```
Kafka no permite reducir el número de particiones por lo cual es necesario borrarlo totalmente.


### Borrar todo el ambiente de pruebas
Para limpiar el ambiente de los ejemplos de prueba terminaremos los
procesos en el siguiente orden:
- Control + C en los consumers y producers
- Control + C en los brokers de kafka
- Control + C en el servidor de zookeeper
  Si se desea borrar la información, ejecutar:
```
  rm -rf /tmp/kafka-logs/tmp/zookeeper
```

