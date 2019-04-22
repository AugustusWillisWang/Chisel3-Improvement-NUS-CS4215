// See LICENSE.txt for license details.
package pliChiselImprove.mismatchCond

import chisel3._
import pliChiselImprove.pliSupportLib._

// import Counter._

// Problem:
//
// Counter should be incremented by the 'amt'
// every clock if 'en' is asserted
//


object Counter {

  def wrapAround(n: UInt, max: UInt) = 
    Mux(n > max, 0.U, n)

  def counter(max: UInt, en: Bool, amt: UInt): UInt = {
    val x = RegInit(0.U(max.getWidth.W))
    println("Rex x inited")
    println("Reg x width = ", x.getWidth)
    printf(p"Reg x = $x\n")
    when (en) { x := wrapAround(x + amt, max) }
    x
  }

}

class Counter extends Module {
  val io = IO(new Bundle {
    val inc = Input(Bool())
    val amt = Input(UInt(4.W))
    val tot = Output(UInt(8.W))
    val moreThan512 = Output(Bool())
  })

  val tmp = Counter.counter(255.U, io.inc, io.amt)
  println("counter output width", tmp.getWidth)
  io.tot := tmp
  println("iotot width", io.tot.getWidth)
  println("512.U", 512.U.getWidth)
  // io.moreThan512 := io.tot > 512.U
  io.moreThan512 := 512.U < io.tot
}

class CounterUUMismatch extends Module {
  val io = IO(new Bundle {
    val inc = Input(Bool())
    val amt = Input(UInt(4.W))
    val tot = Output(UInt(8.W))
    val moreThan512 = Output(Bool())
  })

  val tmp = Counter.counter(255.U, io.inc, io.amt)
  println("counter output width", tmp.getWidth)
  io.tot := tmp
  println("iotot width", io.tot.getWidth)
  println("512.U", 512.U.getWidth)
  // io.moreThan512 := io.tot > 512.U
  io.moreThan512 := StrictCompare(512.U, "<", io.tot)
}

class CounterUUMatch extends Module {
  val io = IO(new Bundle {
    val inc = Input(Bool())
    val amt = Input(UInt(4.W))
    val tot = Output(UInt(8.W))
    val moreThan512 = Output(Bool())
  })

  val tmp = Counter.counter(255.U, io.inc, io.amt)
  io.tot := tmp
  // io.moreThan512 := io.tot > 512.U
  io.moreThan512 := StrictCompare(255.U, ">=", io.tot)
}

class CounterURegMismatchNostrict extends Module {
  val io = IO(new Bundle {
    val status = Output(Bool())
  })
  val reg = RegInit(42.U)
  io.status := 512.U < reg
  // io.status := StrictCompare(512.S, "<", reg)
}

class CounterURegMatchNostrict extends Module {
  val io = IO(new Bundle {
    val status = Output(Bool())
  })

  val reg = RegInit(254.U)
  io.status := 255.U >=reg
  // io.status := StrictCompare(255.S, ">=", reg)
}

class CounterURegMismatchStrict extends Module {
  val io = IO(new Bundle {
    val status = Output(Bool())
  })
  val reg = RegInit(42.U)
  // io.status := 512.U < reg
  io.status := StrictCompare(512.U, "<", reg)
}

class CounterURegMatchStrict extends Module {
  val io = IO(new Bundle {
    val status = Output(Bool())
  })

  val reg = RegInit(254.U)
  println(reg)
  println(254.U)
  // io.status := 255.U >=reg
  io.status := StrictCompare(255.U, ">=", reg)
}

class CounterSSMismatch extends Module {
  val io = IO(new Bundle {
    val status = Output(Bool())
  })
  val reg = RegInit(42.S)
  io.status := 512.S < reg
  // io.status := StrictCompare(512.S, "<", reg)
}

class CounterSSMatch extends Module {
  val io = IO(new Bundle {
    val status = Output(Bool())
  })

  val reg = RegInit(254.S)
  io.status := 255.S >=reg
  // io.status := StrictCompare(255.S, ">=", reg)
}
