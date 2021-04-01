import com.amazonaws.services.kinesis.producer.{KinesisProducer, KinesisProducerConfiguration, UserRecordResult}
import com.google.common.util.concurrent.ListenableFuture
import com.amazonaws.SDKGlobalConfiguration._
import java.nio.ByteBuffer
import java.util
import scala.concurrent.Future
import scala.jdk.FutureConverters._
import scala.jdk.CollectionConverters._
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider

object TestSyncProducer extends App {

  val STREAM_NAME = "rizwan.test.stream"

  val basicCreds = new BasicAWSCredentials("access_key_id", "secret_key_id")
  val awsCreds = new AWSStaticCredentialsProvider(basicCreds)

  val config = new KinesisProducerConfiguration()
    .setRegion("us-east-1")
    .setKinesisEndpoint("localhost")
    .setKinesisPort(4568)
    .setVerifyCertificate(false)
    .setCredentialsProvider(awsCreds)

  val kinesisProducer = new KinesisProducer(config)

  var putFutures = List[ListenableFuture[UserRecordResult]]()
  
  for (i <- 0 until 5) {
    val payload = s"rizwan-minhas-${i}-${System.currentTimeMillis}"
    val data = ByteBuffer.wrap(payload.getBytes("UTF-8"))
    putFutures = putFutures :+ (kinesisProducer.addUserRecord(STREAM_NAME, "myPartitionKey", data))
  }

  for (f <- putFutures) {
    val result = f.get

    if (result.isSuccessful) {
      println(s"\n\n***Successfully published record***")
      println(s"ShardId: ${result.getShardId}")
      println(s"SequenceNumber: ${result.getSequenceNumber}")
    }
    else {
      for (attempt <- result.getAttempts.asScala) {
        println(s"***Error***")
        println(s"Delay: ${attempt.getDelay}")
        println(s"Duration: ${attempt.getDuration}")
        println(s"ErrorCode: ${attempt.getErrorCode}")
        println(s"ErrorMessage: ${attempt.getErrorMessage}")
      }
    }
  }

  println("\n\nshutting down kinesis producer...")
  kinesisProducer.flushSync()
  kinesisProducer.destroy()
  println("shutdown complete.")
}
