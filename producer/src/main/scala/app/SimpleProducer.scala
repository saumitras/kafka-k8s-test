package app

import java.util.{Date, Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object SimpleProducer extends App {
  val SERVER = "kafka-ip:9092"

  val TOPIC_NAME = "k8s-test2"

  println(s"Connecting to kafka $SERVER on topic $TOPIC_NAME")
  val props = new Properties()
  props.setProperty("bootstrap.servers", SERVER)
  props.put("client.id", "KafkaTestProducer")
  props.setProperty("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer")
  props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[Int, String](props)

  for(i <- 1 to 5) {
    println(s"Record $i")
    val data = new ProducerRecord[Int, String](TOPIC_NAME, i, s"This is record $i ${System.nanoTime()}")
    producer.send(data).get()
    println(data)
    Thread.sleep(100)
  }

  producer.close()
  println("Sleeping for some time to flush")
  Thread.sleep(30000)

}
