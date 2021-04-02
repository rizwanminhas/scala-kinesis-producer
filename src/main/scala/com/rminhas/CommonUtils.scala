package com.rminhas

import com.amazonaws.auth.{BasicAWSCredentials, AWSStaticCredentialsProvider}
import com.amazonaws.services.kinesis.producer.{
  KinesisProducerConfiguration,
  KinesisProducer,
  UserRecordResult,
  Attempt
}

object CommonUtils {

  private val basicCreds =
    new BasicAWSCredentials("access_key_id", "secret_key_id")
  private val awsCreds = new AWSStaticCredentialsProvider(basicCreds)

  private val config = new KinesisProducerConfiguration()
    .setRegion("us-east-1")
    .setKinesisEndpoint("localhost")
    .setKinesisPort(4568)
    .setVerifyCertificate(false)
    .setCredentialsProvider(awsCreds)

  val STREAM_NAME = "rizwan.test.stream"

  val kinesisProducer = new KinesisProducer(config)

  def printSuccess(result: UserRecordResult): Unit = {
    println(s"\n\n***Successfully published record***")
    println(s"ShardId: ${result.getShardId}")
    println(s"SequenceNumber: ${result.getSequenceNumber}")
  }

  def printError(attempt: Attempt): Unit = {
    println(s"\n\n***Error***")
    println(s"Delay: ${attempt.getDelay}")
    println(s"Duration: ${attempt.getDuration}")
    println(s"ErrorCode: ${attempt.getErrorCode}")
    println(s"ErrorMessage: ${attempt.getErrorMessage}")
  }

  def cleanup(): Unit = {
    kinesisProducer.flushSync()
    println("\n\nshutting down kinesis producer...")
    kinesisProducer.destroy()
    println("shutdown complete.")
  }

}
