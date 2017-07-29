package com.kos.collections

object Main {

	class Data(val a: Int)

	def main(args: Array[String]): Unit = {
		println("start")
		var b=0
		val pool = new ConveyorPool[Data]({ x ⇒
			b+=1
			println(x.map(_.a).mkString(">", " ", "<"))

		})

		pool.put(new Data(1))
		pool.put(new Data(2))
		pool.put(new Data(3))


		pool.put(new Data(10))
		pool.put(new Data(20))
		for (i ← 1 to 100) {
			pool.put(new Data(30+i))
		}
		Thread.sleep(1000)
		pool.put(new Data(4))
		pool.put(new Data(5))
		Thread.sleep(1000)
		for (i ← 1 to 100000) {
			pool.put(new Data(30+i))
			if (i%100==0){
				Thread.sleep(1)
			}
		}
		pool.put(new Data(6))
		pool.put(new Data(7))
		Thread.sleep(1000)

		println(b)
		pool.shutdown()
	}


}