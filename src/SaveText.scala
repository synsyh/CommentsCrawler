import java.io.{File, PrintWriter}

import com.mongodb.casbah.Imports.MongoClient

/**
  * Created by sunning on 2017/5/4.
  */
object SaveText {
  val mongoClient = MongoClient()
  val db = mongoClient("db")
  val coll = db("crawler")

  def main(args: Array[String]): Unit = {
    val writer = new PrintWriter(new File("test.txt"))
    coll.find()
    for {
      x <- coll
      y=x.get("comment").toString.replaceAll("&mdash;","~").replaceAll("&hellip;","……")
    } yield println(y)
    writer.close()

  }
}
