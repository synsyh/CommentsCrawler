import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import java.net.URL
import java.net.HttpURLConnection

import scala.collection.JavaConversions._
import java.io.{ByteArrayOutputStream, FileOutputStream, PrintStream, PrintWriter}
import java.util.concurrent.CountDownLatch
import java.util.HashSet
import java.util.regex.Pattern

import JsonDemo.{coll, store}
import com.mongodb.casbah.Imports.MongoClient
import com.mongodb.casbah.commons.MongoDBObject

import scala.io.Source
import scala.util.parsing.json.{JSON, JSONObject}

/**
  * Created by sunning on 2017/4/14.
  */
object CrawlerStudy {
  val mongoClient = MongoClient()
  val db = mongoClient("db")
  val coll = db("crawler")

  def main(args: Array[String]): Unit = {
    for (pageNum <- 0 to 9) {
      val url = "https://club.jd.com/comment/productPageComments.action?callback=fetchJSON_comment98vv15915&productId=3133853&score=0&sortType=5&page=" + pageNum + "&pageSize=10&isShadowSku=0"
      var htmlText = getPage(url, "gbk")
      htmlText = htmlText.substring(27, htmlText.length() - 2)
      matchComments(htmlText)
      println("success in for page" + pageNum)
    }
  }

  def getPage(url: String, charset: String): String = {
    val uri = new URL(url)
    val conn = uri.openConnection().asInstanceOf[HttpURLConnection]
    conn.setConnectTimeout(100000)
    conn.setReadTimeout(1000000)
    val stream = conn.getInputStream()
    println("success in geting stream")
    val buf = Array.fill[Byte](1024)(0)
    var len = stream.read(buf)
    val out = new ByteArrayOutputStream
    while (len > -1) {
      out.write(buf, 0, len)
      len = stream.read(buf)
    }
    println("success reading")
    val data = out.toByteArray()
    var htmlText: String = new String(data, charset)
    println("success changing the code")
    htmlText
  }

  def matchComments(htmlText: String) = {
    val allData = JSON.parseFull(htmlText)
    allData match {
      case Some(map: Map[String, Any]) => {
        val comments = map.get("comments")
        comments match {
          case Some(list: List[Map[String, Any]]) =>
            list.foreach(map => store(map.get("content").get.toString))
          case None => println("Parsing failed")
          case other => println("Unknown data structure: " + other)
        }
      }
      case None => println("Parsing failed")
      case other => println("Unknown data structure: " + other)
    }
  }

  def store(comment: String): Unit = {
    val MGDBO = MongoDBObject("comment" -> comment)
    coll.insert(MGDBO)
  }
}
