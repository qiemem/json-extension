package org.nlogo.extensions.json

import org.nlogo.api._
import Syntax._
import ScalaConversions._

import scala.util.parsing.json.JSON

class JSONExtension extends DefaultClassManager {
  def load(primitiveManager: PrimitiveManager) {
    primitiveManager.addPrimitive("parse", JSONParser)
  }
}

object JSONParser extends DefaultReporter {
  override def getSyntax = reporterSyntax(Array(StringType), ListType)
  def report(args: Array[Argument], context: Context): AnyRef = {
    try {
      JSON.parseFull(args(0).getString) map { case l: List[_] => (l map (_.toString)).toLogoList} getOrElse (throw new ExtensionException("Invalid JSON string"))
    } catch {
      case e: LogoException =>
        throw new ExtensionException(e.getMessage)
    }
  }
}