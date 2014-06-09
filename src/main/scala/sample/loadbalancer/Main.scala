package sample.loadbalancer

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * @author Anton Gnutov
 */
object Main {
  def main(args: Array[String]) {
    val config = ConfigFactory.load()

    val maxWorkers = config.getInt("maxWorkers")

    val system = ActorSystem("actor-system")
    val balancer = system.actorOf(Props(classOf[LoadBalancer], maxWorkers), "balancer")

    (1 to 10).foreach { _ =>
      balancer ! Work("12345678")
    }

    Thread.sleep(1000L)
    balancer ! Exit
  }
}
