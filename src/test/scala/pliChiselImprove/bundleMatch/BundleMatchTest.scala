package pliChiselImprove.bundleMatch

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import pliChiselImprove._

class BundleEQTrueTest(c: BundleCompare) extends PeekPokeTester(c) {

  for (i <- 0 until 5) {
    poke(c.io.in1s, 0)
    poke(c.io.in1n, 0)
    poke(c.io.in2s, 0)
    poke(c.io.in2n, 0)
    expect(c.io.status, true)
    step(1)
  }

}

class BundleEQFalseTest(c: BundleCompare) extends PeekPokeTester(c) {

  for (i <- 0 until 5) {
    poke(c.io.in1s, 0)
    poke(c.io.in1n, 0)
    poke(c.io.in2s, 1)
    poke(c.io.in2n, 0)
    expect(c.io.status, false)
    step(1)
  }

}

class BundleNEQTrueTest(c: BundleNEQCompare) extends PeekPokeTester(c) {

  for (i <- 0 until 5) {
    poke(c.io.in1s, 0)
    poke(c.io.in1n, 1)
    poke(c.io.in2s, 0)
    poke(c.io.in2n, 0)
    expect(c.io.status, true)
    step(1)
  }

}

class BundleNEQFalseTest(c: BundleNEQCompare) extends PeekPokeTester(c) {

  for (i <- 0 until 5) {
    poke(c.io.in1s, 0)
    poke(c.io.in1n, 0)
    poke(c.io.in2s, 0)
    poke(c.io.in2n, 0)
    expect(c.io.status, false)
    step(1)
  }

}

class  BundleRecursiveTrueTest(c: BundleRecursiveCompare) extends PeekPokeTester(c) {

  for (i <- 0 until 5) {
    poke(c.io.in1s, 0)
    poke(c.io.in1n, 1)
    poke(c.io.in1int8, 2)
    poke(c.io.in2s, 0)
    poke(c.io.in2n, 1)
    poke(c.io.in2int8, 2)
    expect(c.io.status, true)
    step(1)
  }

}

class  BundleRecursiveFalseTest(c: BundleRecursiveCompare) extends PeekPokeTester(c) {

  for (i <- 0 until 5) {
    poke(c.io.in1s, 0)
    poke(c.io.in1n, 1)
    poke(c.io.in1int8, 2)
    poke(c.io.in2s, 0)
    poke(c.io.in2n, 1)
    poke(c.io.in2int8, 3)
    expect(c.io.status, false)
    step(1)
  }

  for (i <- 0 until 5) {
    poke(c.io.in1s, 0)
    poke(c.io.in1n, 1)
    poke(c.io.in1int8, 2)
    poke(c.io.in2s, 1)
    poke(c.io.in2n, 1)
    poke(c.io.in2int8, 2)
    expect(c.io.status, false)
    step(1)
  }

}

class  BundleRecursiveNEQTrueTest(c: BundleRecursiveNEQCompare) extends PeekPokeTester(c) {

  for (i <- 0 until 5) {
    poke(c.io.in1s, 0)
    poke(c.io.in1n, 1)
    poke(c.io.in1int8, 2)
    poke(c.io.in2s, 0)
    poke(c.io.in2n, 1)
    poke(c.io.in2int8, 2)
    expect(c.io.status, false)
    step(1)
  }

}

class  BundleRecursiveNEQFalseTest(c: BundleRecursiveNEQCompare) extends PeekPokeTester(c) {

  for (i <- 0 until 5) {
    poke(c.io.in1s, 0)
    poke(c.io.in1n, 1)
    poke(c.io.in1int8, 2)
    poke(c.io.in2s, 0)
    poke(c.io.in2n, 1)
    poke(c.io.in2int8, 3)
    expect(c.io.status, true)
    step(1)
  }

  for (i <- 0 until 5) {
    poke(c.io.in1s, 0)
    poke(c.io.in1n, 1)
    poke(c.io.in1int8, 2)
    poke(c.io.in2s, 1)
    poke(c.io.in2n, 1)
    poke(c.io.in2int8, 2)
    expect(c.io.status, true)
    step(1)
  }

}