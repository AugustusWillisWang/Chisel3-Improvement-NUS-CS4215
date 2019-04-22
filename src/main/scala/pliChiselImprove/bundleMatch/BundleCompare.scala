// See LICENSE.txt for license details.
package pliChiselImprove.bundleMatch

import chisel3._
import pliChiselImprove.pliSupportLib._


class MySignedInt() extends Bundle{
  val sign = UInt(1.W)
  val num  = UInt((31).W)
}

class ComplexBundle() extends Bundle{
  val int = new MySignedInt()
  val int8 = UInt(8.W)
}

class BundleCompare extends Module {
  val io = IO(new Bundle {
    val in1s = Input(UInt(1.W))
    val in1n = Input(UInt(31.W))
    val in2s = Input(UInt(1.W))
    val in2n = Input(UInt(31.W))
    val status = Output(Bool())
  })
  val in1 = Wire(new MySignedInt)
  val in2 = Wire(new MySignedInt)
  in1.sign := io.in1s
  in1.num := io.in1n
  in2.sign := io.in2s
  in2.num := io.in2n
  io.status := StrictCompare(in1, "===", in2)
  // io.status := StrictCompare(255.S, ">=", reg)
}

class BundleNEQCompare extends Module {
  val io = IO(new Bundle {
    val in1s = Input(UInt(1.W))
    val in1n = Input(UInt(31.W))
    val in2s = Input(UInt(1.W))
    val in2n = Input(UInt(31.W))
    val status = Output(Bool())
  })
  val in1 = Wire(new MySignedInt)
  val in2 = Wire(new MySignedInt)
  in1.sign := io.in1s
  in1.num := io.in1n
  in2.sign := io.in2s
  in2.num := io.in2n
  io.status := StrictCompare(in1, "=/=", in2)
  // io.status := StrictCompare(255.S, ">=", reg)
}


class BundleRecursiveCompare extends Module {
  val io = IO(new Bundle {
    val in1s = Input(UInt(1.W))
    val in1n = Input(UInt(31.W))
    val in1int8 = Input(UInt(8.W))
    val in2s = Input(UInt(1.W))
    val in2n = Input(UInt(31.W))
    val in2int8 = Input(UInt(8.W))
    val status = Output(Bool())
  })
  val in1 = Wire(new ComplexBundle)
  val in2 = Wire(new ComplexBundle)
  val int1 = Wire(new MySignedInt)
  val int2 = Wire(new MySignedInt)
  
  int1.sign := io.in1s
  int1.num := io.in1n
  int2.sign := io.in2s
  int2.num := io.in2n
  in1.int8 := io.in1int8
  in2.int8 := io.in2int8

  in1.int := int1
  in2.int := int2
  
  io.status := StrictCompare(in1, "===", in2)
  // io.status := StrictCompare(255.S, ">=", reg)
}

class BundleRecursiveNEQCompare extends Module {
  val io = IO(new Bundle {
    val in1s = Input(UInt(1.W))
    val in1n = Input(UInt(31.W))
    val in1int8 = Input(UInt(8.W))
    val in2s = Input(UInt(1.W))
    val in2n = Input(UInt(31.W))
    val in2int8 = Input(UInt(8.W))
    val status = Output(Bool())
  })
  val in1 = Wire(new ComplexBundle)
  val in2 = Wire(new ComplexBundle)
  val int1 = Wire(new MySignedInt)
  val int2 = Wire(new MySignedInt)
  
  int1.sign := io.in1s
  int1.num := io.in1n
  int2.sign := io.in2s
  int2.num := io.in2n
  in1.int8 := io.in1int8
  in2.int8 := io.in2int8

  in1.int := int1
  in2.int := int2
  
  io.status := StrictCompare(in1, "=/=", in2)
  // io.status := StrictCompare(255.S, ">=", reg)
}
