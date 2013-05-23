package org.nlogo.extensions.json

import org.nlogo.api._
import Syntax._
import ScalaConversions._

import scala.util.parsing.json.JSON
import scala.collection.immutable.HashMap
import scala.collection.generic._

class JSONExtension extends DefaultClassManager {
  def load(primitiveManager: PrimitiveManager) {
    primitiveManager.addPrimitive("parse", JSONParser)
    primitiveManager.addPrimitive("item", MapItem)
    primitiveManager.addPrimitive("nested-item", MapNestedItem)
  }
}

object JSONParser extends DefaultReporter {
  override def getSyntax = reporterSyntax(Array(StringType), WildcardType)

  def convert: PartialFunction[Any, AnyRef] = {
    case l: List[_] => {
      // toLogoList tries to convert everything via toLogoObject, but we don't
      // want Maps converted, so we create a LogoList directly
      LogoList.fromIterator((l map(convert)).iterator)
    }
    case m: Map[_, _] => m mapValues(convert)
    case x: Double => x: java.lang.Double
    case s => s.toString
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

object MapItem extends DefaultReporter {
  override def getSyntax =
    reporterSyntax(Array(StringType | NumberType, WildcardType), WildcardType)
  def report(args: Array[Argument], context: Context): AnyRef = get(args(1).get, args(0).get)

  def get: PartialFunction[(AnyRef, AnyRef), AnyRef] = {
    case (m: Map[_,_], k: String) =>
      m.asInstanceOf[Map[String, AnyRef]]
        .getOrElse(k, throw new ExtensionException("Key " + k + " not found in " + m.toString))
    case (l: LogoList, i: java.lang.Double) => l.get(i.intValue)
    case (a: Any, b: Any) => throw new ExtensionException("Invalid arguments: " + a.toString + " " + b.toString)
  }
}

object MapNestedItem extends DefaultReporter {
  override def getSyntax =
    reporterSyntax(Array(WildcardType, RepeatableType | StringType | NumberType), WildcardType)

  def report(args: Array[Argument], context: Context): AnyRef =
    args.tail.foldLeft(args(0).get)((m: AnyRef, k: Argument) => MapItem.get(m, k.get))
}
/*
object JSONMap extends MapFactory[JSONMap] {
  implicit def canBuildFrom[String, AnyRef]: CanBuildFrom[Coll, (String, AnyRef), JSONMap] = new MapCanBuildFrom[String, AnyRef]
  def empty: JSONMap = new JSONMap()
}
class JSONMap extends HashMap[String, AnyRef] with ExtensionObject {
  override def dump(readable: Boolean, exporting: Boolean, reference: Boolean) =
    toString
  override def getExtensionName = "json"
  override def getNLTypeName = ""
  override def recursivelyEqual(obj: AnyRef): Boolean = obj match {
    case m: JSONMap => true
    case _ => false
  }
}
*/