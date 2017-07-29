package com.kos.collections

import java.util
import java.util.concurrent.Executors

/**
  * Created by Kos on 07.05.2017.
  */
class ConveyorPool[T](val action: Seq[T] ⇒ Unit) {
	private[this] val tpe = Executors.newSingleThreadExecutor()

	private[this] val queue = new util.ArrayList[T](20)

	private[this] var lastId = 0L

	def put(item: T): Unit = {
		queue.synchronized {
			queue.add(item)
		}

		lastId += 1
		tpe.execute(new ConveyorRun(lastId, this))
	}

	def getLastId:Long = lastId

	def extractAll(): Seq[T] = {
		var a = Seq.newBuilder[T]
		queue.synchronized {
			//a = new Seq[T](queue.size())
			val i = queue.iterator()
			while (i.hasNext) {
				a += i.next()
			}
			queue.clear()
		}
		//a.asInstanceOf[Array[T]]
		a.result()
	}

	def shutdown(): Unit = {
		tpe.shutdownNow()
	}

	//	val runnable=new ConveyorRun(0,this)
}


private class ConveyorRun[T](val index: Long, val pool: ConveyorPool[T]) extends Runnable {

	override def run(): Unit = {
		if (index >= pool.getLastId) {
			val a = pool.extractAll()
			if (a.nonEmpty) {
				pool.action(a)
			}
		}
	}
}

