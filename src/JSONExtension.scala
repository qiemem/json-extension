package org.nlogo.extensions.json

import org.nlogo.api._
import Syntax._
import ScalaConversions._

import scala.util.parsing.json.JSON

class JSONExtension extends DefaultClassManager {
  def load(primitiveManager: PrimitiveManager) {
    primitiveManager.addPrimitive("parse", JSONParser)
    primitiveManager.addPrimitive("get", MapGet)
  }
}

object JSONParser extends DefaultReporter {
  override def getSyntax = reporterSyntax(Array(StringType), WildcardType)

  def convert: PartialFunction[Any, AnyRef] = {
    case l: List[_] => (l map(convert)).toLogoList
    case m: Map[_, _] => m mapValues(convert)
    case x => x.toString
  }

  def report(args: Array[Argument], context: Context): AnyRef = {
    try {
      JSON.parseFull(args(0).getString) map(convert) getOrElse (throw new ExtensionException("Invalid JSON string"))
    } catch {
      case e: LogoException =>
        throw new ExtensionException(e.getMessage)
    }
  }
}

object MapGet extends DefaultReporter {
  override def getSyntax =
    reporterSyntax(Array(WildcardType, StringType | NumberType), StringType)
  def report(args: Array[Argument], context: Context): AnyRef =
    args(0).get.asInstanceOf[Map[String,String]] getOrElse(args(1).getString, "hi")
}
