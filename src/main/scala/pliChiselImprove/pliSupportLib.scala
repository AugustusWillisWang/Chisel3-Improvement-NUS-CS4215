package pliChiselImprove

import chisel3._
import scala.collection.immutable.ListMap

case class CompareWidthMismatchException(str:String) extends Exception
case class CompareTypeMismatchException(str:String) extends Exception
case class UndefinedStrictCompareException(str:String) extends Exception

object pliSupportLib{

def StrictCompare(input1: Data, operator: String, input2: Data): Bool = {

    def widthHelper(input1: Data, operator: String, input2: Data): Bool = {

        val a = input1.getWidth
        val b = input2.getWidth

        // For the same type:
        // when width not match, throw exception:
        // match can not be used here because of some design problems
        if (input1.isInstanceOf[UInt] && input2.isInstanceOf[UInt]){
            //......
            if(a==b){
                return chiselParser(input1, operator, input2)   
            }else{
                print("[warn] StrictCompare: UInt width mismatch:")
                print(input1, input2)
                println()

                throw new CompareWidthMismatchException("UInt width mismatch")
                return chiselParser(input1, operator, input2)   
                // return Bool(false)
            }
        }

        if (input1.isInstanceOf[SInt] && input2.isInstanceOf[SInt]){
            //......
            if(a==b){
                return chiselParser(input1, operator, input2)   
            }else{
                print("[warn] StrictCompare: UInt width mismatch:")
                print(input1, input2)
                println()
                throw new CompareWidthMismatchException("SInt width mismatch")
                return chiselParser(input1, operator, input2)   
            }
        }

        // For different type:

        if (input1.isInstanceOf[UInt] && input2.isInstanceOf[SInt]){
            println("[warn] StrictCompare: Comparing UInt with SInt")
            if(a<=(b+1)){
                return chiselParser(input1, operator, input2)   
            }else{
                print("[warn] StrictCompare: The positive range of SInt is less than the UInt's range:")
                print(input1, input2)
                println()
                return chiselParser(input1, operator, input2)   
                // throw new CompareWidthMismatchException("UInt width mismatch")
            }
        }

        if (input1.isInstanceOf[SInt] && input2.isInstanceOf[UInt]){
            println("[warn] StrictCompare: Comparing UInt with SInt")
            if((a+1)>=b){
                return chiselParser(input1, operator, input2)   
            }else{
                print("[warn] StrictCompare: The positive range of SInt is less than the UInt's range:")
                print(input1, input2)
                println()
                return chiselParser(input1, operator, input2)   
                // throw new CompareWidthMismatchException("UInt width mismatch")
            }
        }

        // Src Const test
        // TBD

        // println(input1)
        // println(input2)
        throw new UndefinedStrictCompareException("")
        return Bool(false)

        //For SInt and UInt
    }

    def chiselParser(input1: Data, operator: String, input2: Data): Bool ={
        // parse the right compare to chisel expression
        if(input1.isInstanceOf[UInt]){
            operator match {
                case "<" => return input1.asInstanceOf[UInt] < input2.asInstanceOf[UInt]
                case "<=" => return input1.asInstanceOf[UInt] <= input2.asInstanceOf[UInt]
                case ">" => return input1.asInstanceOf[UInt] > input2.asInstanceOf[UInt]
                case ">=" => return input1.asInstanceOf[UInt] >= input2.asInstanceOf[UInt]
                case "===" => return input1.asInstanceOf[UInt] === input2.asInstanceOf[UInt]
                case "=/=" => return input1.asInstanceOf[UInt] =/= input2.asInstanceOf[UInt]

                // .......
                case _ =>{
                    throw new UndefinedStrictCompareException("")
                    return Bool(false)
                } 
            }
        }

        if(input1.isInstanceOf[SInt]){
            operator match {
                case "<" => return input1.asInstanceOf[SInt] < input2.asInstanceOf[SInt]
                case "<=" => return input1.asInstanceOf[SInt] <= input2.asInstanceOf[SInt]
                case ">" => return input1.asInstanceOf[SInt] > input2.asInstanceOf[SInt]
                case ">=" => return input1.asInstanceOf[SInt] >= input2.asInstanceOf[SInt]
                case "===" => return input1.asInstanceOf[SInt] === input2.asInstanceOf[SInt]
                case "=/=" => return input1.asInstanceOf[SInt] =/= input2.asInstanceOf[SInt]
                // .......
                case _ =>{
                    throw new UndefinedStrictCompareException("")
                    return Bool(false)
                } 
            }
        }

        // Bundle equalty
        if(input1.isInstanceOf[Bundle]){
            // if(input1.isInstanceOf[input2.getClass]){
            // }else{
                // throw new CompareTypeMismatchException
            // }
            operator match {
                case "==="=>{
                    val b1 = getBundleElement(input1.asInstanceOf[Bundle])
                    val b2 = getBundleElement(input2.asInstanceOf[Bundle])
                    return compareBundleList(b1,b2)
                }
                case "=/="=>{
                    val b1 = getBundleElement(input1.asInstanceOf[Bundle])
                    val b2 = getBundleElement(input2.asInstanceOf[Bundle])
                    return ~ compareBundleList(b1,b2)
                }
                case _=>{}
            }
            // print("[warn] StrictCompare: Bundle inequality not defined")
            // throw new UndefinedStrictCompareException("")
            // return Bool(false)
        }
        throw new UndefinedStrictCompareException("")
        return Bool(false)
    }

    // def equalParser(input1: Data, operator: String, input2: Data): Bool ={
    //     // parse the right compare to chisel expression
    //     operator match {
    //         case "<" => input1.asInstanceOf[UInt] < input2.asInstanceOf[UInt]
    //         // .......
    //         case _ => Bool(false)
    //     }
    // }

    def getBundleElement(bundle : Bundle): ListMap[String, Data]={
        // get all elements in Bundle, using java reflection
        bundle.elements
    }

    def compareBundleList(b1: ListMap[String, Data],b2: ListMap[String, Data]): Bool={
        // compare and match two Data list.
        // TBD
        // Inprogress, see Inprogress for the newest version
        def existInAnotherBundle(o: ListMap[String, Data])=(in:(String, Data))=>{
                in match {
                    case (string: String, data: Data)=>{
                        o.contains(string)
                    }
                    case _ => {
                        throw new UndefinedStrictCompareException("")
                    }
                }
            }

        def compareWithAnotherBundle(o: ListMap[String, Data])=(result:Bool, in:(String, Data))=>{
                in match {
                    case (string: String, data: UInt)=>{
                        result & (data.asInstanceOf[UInt] === o(string).asInstanceOf[UInt])
                    }
                    case (string: String, data: SInt)=>{
                        result & (data.asInstanceOf[SInt] === o(string).asInstanceOf[SInt])
                    }
                    case (string: String, data: Bundle)=>{
                        result & (StrictCompare(data.asInstanceOf[Bundle], "===",  o(string).asInstanceOf[Bundle]))
                    }
                    //TODO finish all the types
                    case _ => {
                        throw new UndefinedStrictCompareException("")
                    }
                }
            }

        // def existInAnotherBundle1(string : String, data: Data):Boolean={
        //     b1.contains(string)
        // }
        // def existInAnotherBundle2(string : String, data: Data):Boolean={
        //     b2.contains(string)
        // }

        if(!b1.forall(existInAnotherBundle(b2))){
            print("[warn] StrictCompare: Bundle type mismatch")
            return Bool(false)
        }

        if(!b2.forall(existInAnotherBundle(b1))){
            print("[warn] StrictCompare: Bundle type mismatch")
            return Bool(false)
        }

        // (result:Bool, (name: String, datain:Data))=>(result & compareWithAnotherBundle(b2)((name, datain)))

        //fold left
        b1.foldLeft(Bool(true))(compareWithAnotherBundle(b2))
        // need to deal with the circumstance that datain is a Bundle
        //TODO
    }

    try{
        input1.getWidth
        input2.getWidth
    } catch {
        case e : Throwable => {
            println("[warn] StrictCompare: Reg included in compare. Use fix-width wire instead.")
            throw CompareWidthMismatchException("reg included in compare")
            // return chiselParser(input1, operator, input2)
        }
    }

    if(input1.isInstanceOf[Bundle]&&input2.isInstanceOf[Bundle]){
        return chiselParser(input1, operator, input2)
    }

    operator match {
        case "<" => widthHelper(input1, operator, input2)
        case "<=" => widthHelper(input1, operator, input2)
        case ">" => widthHelper(input1, operator, input2)
        case ">=" => widthHelper(input1, operator, input2)
        case "===" => widthHelper(input1, operator, input2)
        case "=/=" => widthHelper(input1, operator, input2)
        case _ => chiselParser(input1, operator, input2)// error?
    }
}
}

// class Foo extends Bundle{
//     val sign        = Bool()
//     val exponent    = UInt(8.W)
//     val significand = UInt(23.W)
// }
// val x = new Foo()
// println(x.getClass.getName) 


// TODO
// 
// test
// UU pass
// UU not pass
// SS pass
// SS not pass
// SU pass
// SU not pass
// BB pass
// BB not pass
//
// doc
// what we did
// lookup for source
// reflect
// bundle match
// usage: as a package
// test cases