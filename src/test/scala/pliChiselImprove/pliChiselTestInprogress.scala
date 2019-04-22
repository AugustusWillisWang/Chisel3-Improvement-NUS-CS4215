package pliChiselImprove
import pliChiselImprove._
import pliChiselImprove.mismatchCond._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class CurrentTest extends ChiselFlatSpec {
  behavior of "UInt-Reg width dismatch module in pliChiselImprove"
  backends foreach {backend =>
    it should s"compile and run correctly in $backend" in {
      var res:Boolean = false
      Driver(() => new CounterURegMismatchStrict, backend)(c => new CounterURegMismatchStrictTest(c)) should be (true)
    }
  }

  behavior of "UInt-Reg width match module in in pliChiselImprove"
  backends foreach {backend =>
    it should s"compile and run correctly in $backend" in {
      var res:Boolean = false
      Driver(() => new CounterURegMatchStrict, backend)(c => new CounterURegMatchStrictTest(c)) should be (true)
    }
  }
}
