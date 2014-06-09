package sample.loadbalancer

import akka.actor.{ActorRef, Actor, ActorLogging}

/**
 * @author Anton Gnutov
 */
class Worker(val balancer: ActorRef) extends Actor with ActorLogging {

  override def preStart() {
    balancer ! Ready
  }

  override def receive = {
    case Work(message) =>
      log.info("Working on '{}' ...", message)
      message.permutations.size
      sender() ! Ready
    case Exit =>
      log.info("Exit received, terminating...")
      context.stop(self)
  }
}
