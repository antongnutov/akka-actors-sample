package sample.loadbalancer

/**
 * @author Anton Gnutov
 */
abstract sealed class Message {}
case object Exit extends Message
case object Ready extends Message
case class Work(message: String) extends Message with Comparable[Work] {
  override def compareTo(o: Work): Int = message.compareTo(o.message)
}