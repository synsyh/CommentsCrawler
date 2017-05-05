import com.mongodb.casbah.Imports.MongoClient
import com.mongodb.casbah.commons.MongoDBObject

import scala.io.Source
import scala.util.parsing.json.JSON

/**
  * Created by sunning on 2017/4/17.
  */
object JsonDemo {
  val mongoClient=MongoClient()
  val db=mongoClient("db")
  val coll=db("crawler")

  def main(args: Array[String]): Unit = {
    val jsonString=Source.fromFile("/Users/sunning/IdeaProjects/ScalaTest/src/jdcommentsdata.txt").mkString
    val json = JSON.parseFull(jsonString)
    println("success get the json file")
    json match {
      case Some(map: Map[String, Any]) => {
        val comments=map.get("comments")
        comments match {
          case Some(list:List[Map[String,Any]]) =>
            list.foreach(map=>store(map.get("content").get.toString))
          case None => println("Parsing failed")
          case other => println("Unknown data structure: " + other)
        }
      }
      case None => println("Parsing failed")
      case other => println("Unknown data structure: " + other)
    }
    println("success in all")
  }
  def store(comment:String): Unit ={
    val MGDBO=MongoDBObject("comment"->comment)
    coll.insert(MGDBO)
  }
}
