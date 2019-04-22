package pliChiselImprove
import pliChiselImprove._
import pliChiselImprove.mismatchCond._
import pliChiselImprove.bundleMatch._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class CounterTester extends ChiselFlatSpec {
  behavior of "standard chisel counter"
  backends foreach {backend =>
    it should s"correctly count randomly generated numbers in $backend" in {
      Driver(() => new Counter, backend)(c => new CounterTest(c)) should be (true)
    }
  }
}

class WidthMismatchTester extends ChiselFlatSpec {
  behavior of "UInt-UInt width dismatch counter"
  backends foreach {backend =>
    it should s"throw CompareWidthMismatchException" in {
      var res:Boolean = false
      try {
        Driver(() => new CounterUUMismatch, backend)(c => new CounterUUMismatchTest(c))
        res = false
      } catch {
        case e : CompareWidthMismatchException => res = true 
        case _: Throwable => res = false
      }
      res should be (true)
    }
  }

  behavior of "UInt-UInt width match counter"
  backends foreach {backend =>
    it should s"compile and run correctly in $backend" in {
      var res : Boolean = true
      try {
        Driver(() => new CounterUUMatch, backend)(c => new CounterUUMatchTest(c))
        res = true
      } catch { 
        case _: Throwable => res = false
      }
      res should be (true)
    }
  }

  behavior of "UInt-Reg width dismatch module in standard chisel"
  backends foreach {backend =>
    it should s"compile and run correctly in $backend" in {
      var res:Boolean = false
      Driver(() => new CounterURegMismatchNostrict, backend)(c => new CounterURegMismatchNostrictTest(c)) should be (true)
    }
  }

  behavior of "UInt-Reg width match module in standard chisel"
  backends foreach {backend =>
    it should s"compile and run correctly in $backend" in {
      var res:Boolean = false
      Driver(() => new CounterURegMatchNostrict, backend)(c => new CounterURegMatchNostrictTest(c)) should be (true)
    }
  }

  behavior of "UInt-Reg width dismatch module in pliChiselImprove"
  backends foreach {backend =>
    it should s"throw CompareWidthMismatchException" in {
      var res : Boolean = false
      try {
        Driver(() => new CounterURegMismatchStrict, backend)(c => new CounterURegMismatchStrictTest(c)) 
        res = false
      } catch { 
        case _: Throwable => res = true
      }
      res should be (true)
    }
  }

  behavior of "UInt-Reg width match module in in pliChiselImprove"
  backends foreach {backend =>
    it should s"throw CompareWidthMismatchException" in {
      var res : Boolean = false
      try {
        Driver(() => new CounterURegMatchStrict, backend)(c => new CounterURegMatchStrictTest(c))
        res = false
      } catch { 
        case _: Throwable => res = true
      }
      res should be (true)
    }
  }

  behavior of "SInt-SInt width dismatch module in standard chisel"
  backends foreach {backend =>
    it should s"compile and run correctly in $backend" in {
      Driver(() => new CounterSSMismatch, backend)(c => new CounterSSMismatchTest(c)) should be (true)
    }
  }

  behavior of "SInt-SInt width match module in standard chisel"
  backends foreach {backend =>
    it should s"compile and run correctly in $backend" in {
      Driver(() => new CounterSSMatch, backend)(c => new CounterSSMatchTest(c)) should be (true)
    }
  }
}


class BundleMatchTester extends ChiselFlatSpec {
  behavior of "bundle match circuit: EQ"
  backends foreach {backend =>
    it should s"always output true when match in $backend" in {
      Driver(() => new BundleCompare, backend)(c => new BundleEQTrueTest(c)) should be (true)
    }
  }

  backends foreach {backend =>
    it should s"always output flase when not match in $backend" in {
      Driver(() => new BundleCompare, backend)(c => new BundleEQFalseTest(c)) should be (true)
    }
  }

  behavior of "bundle match circuit: NEQ"
  backends foreach {backend =>
    it should s"always output true when not match in $backend" in {
      Driver(() => new BundleNEQCompare, backend)(c => new BundleNEQTrueTest(c)) should be (true)
    }
  }

  backends foreach {backend =>
    it should s"always output false when match in $backend" in {
      Driver(() => new BundleNEQCompare, backend)(c => new BundleNEQFalseTest(c)) should be (true)
    }
  }

  behavior of "bundle match circuit: recursive EQ"
  backends foreach {backend =>
    it should s"always output true when match in $backend" in {
      Driver(() => new BundleRecursiveCompare, backend)(c => new BundleRecursiveTrueTest(c)) should be (true)
    }
  }

  backends foreach {backend =>
    it should s"always output flase when not match in $backend" in {
      Driver(() => new BundleRecursiveCompare, backend)(c => new BundleRecursiveFalseTest(c)) should be (true)
    }
  }

  behavior of "bundle match circuit: recursive NEQ"
  backends foreach {backend =>
    it should s"always output true when match in $backend" in {
      Driver(() => new BundleRecursiveNEQCompare, backend)(c => new BundleRecursiveNEQTrueTest(c)) should be (true)
    }
  }

  backends foreach {backend =>
    it should s"always output flase when not match in $backend" in {
      Driver(() => new BundleRecursiveNEQCompare, backend)(c => new BundleRecursiveNEQFalseTest(c)) should be (true)
    }
  }
}
