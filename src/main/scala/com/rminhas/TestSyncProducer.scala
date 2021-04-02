package com.rminhas

import java.nio.ByteBuffer
import scala.jdk.CollectionConverters._
import CommonUtils.{
  STREAM_NAME,
  kinesisProducer,
  printSuccess,
  printError,
  cleanup
}

object TestSyncProducer extends App {

  val putFutures = (0 until 5).map(i => {
    val payload = s"rizwan-minhas-${i}-${System.currentTimeMillis}"
    val data = ByteBuffer.wrap(payload.getBytes("UTF-8"))
    kinesisProducer.addUserRecord(STREAM_NAME, "myPartitionKey", data)
  })

  putFutures.foreach(future => {
    val result = future.get // blocking code
    if (result.isSuccessful)
      printSuccess(result)
    else
      result.getAttempts.asScala.foreach(printError)
  })

  cleanup()
}
