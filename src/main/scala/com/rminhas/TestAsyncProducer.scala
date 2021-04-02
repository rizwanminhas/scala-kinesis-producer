package com.rminhas

import com.amazonaws.services.kinesis.producer.UserRecordResult
import java.nio.ByteBuffer
import com.google.common.util.concurrent.{FutureCallback, Futures}
import java.util.concurrent.Executors
import CommonUtils.{STREAM_NAME, kinesisProducer, printSuccess, cleanup}

object TestAsyncProducer extends App {

  val myCallback = new FutureCallback[UserRecordResult]() {
    override def onFailure(t: Throwable): Unit = t.printStackTrace()

    override def onSuccess(result: UserRecordResult): Unit = printSuccess(
      result
    )
  }

  val executor = Executors.newSingleThreadExecutor()

  (0 until 5).foreach(i => {
    val payload = s"rizwan-minhas-${i}-${System.currentTimeMillis}"
    val data = ByteBuffer.wrap(payload.getBytes("UTF-8"))
    val future =
      kinesisProducer.addUserRecord(STREAM_NAME, "myPartitionKey", data)
    Futures.addCallback(future, myCallback, executor)
  })

  cleanup()
}
