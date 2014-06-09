package sample.loadbalancer

import akka.actor.{Props, Actor, ActorLogging}
import java.util.PriorityQueue

/**
 * @author Anton Gnutov
 */
class LoadBalancer(val maxWorkers: Int) extends Actor with ActorLogging {

  val tasks = new PriorityQueue[Work]()
  var workers = 0
  var number = 0

  override def preStart(): Unit = {
    log.debug("Starting Load Balancer with 'maxWorkers' = {} ...", maxWorkers)
  }

  override def receive = {
    case w: Work =>
      tasks.add(w)
      if (workers < maxWorkers) {
        log.debug("Creating new worker ...")
        workers += 1

        context.system.actorOf(Props(classOf[Worker], self), s"worker$number")
        number += 1
      } else {
        log.debug("All workers are busy")
      }
    case Ready =>
      if (tasks.isEmpty) {
        log.debug("There are no tasks, removing idle worker ...")
        workers -= 1
        sender() ! Exit
      } else {
        sender() ! tasks.poll()
      }
    case Exit =>
      log.info("Exit received, stopping actor system ...")
      context.system.shutdown()
    case message => log.error("Unsupported message: {}", message)
  }
}
