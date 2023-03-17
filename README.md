
1. Run Zookeeper: `bin/zookeeper-server-start.sh config/zookeeper.properties`

2. Create file per broker in `config` dir:
    * at first, we need to modify the `.properties` files to specify
        * **id:** broker.id=1
        * **host:** listeners=PLAINTEXT://localhost:9093
        * **log file:** log.dirs= /tmp/kafkainaction/kafka-logs-1
   *  then run broker/server:
     * `bin/kafka-server-start.sh config/server1.properties`

3. Create topic: `bin/kafka-topics.sh --create --bootstrap-server localhost:9094 --topic kinaction_schematest --partitions 3 --replication-factor 3`

4. Producer/Consumer can be created in the terminal without our app. Or we can use, i.e. Java aplication

To install confluent:
1. `brew tap confluentinc/homebrew-confluent-hub-client`
2. `brew install --cask confluent-hub-client`
3. https://stackoverflow.com/a/72869137/16543524