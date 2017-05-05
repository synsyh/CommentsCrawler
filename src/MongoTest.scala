import com.mongodb.casbah.Imports.{MongoClient, MongoDBObject}

/**
  * Created by sunning on 2017/5/4.
  */
object MongoTest {
  def main(args: Array[String]): Unit = {
    val mongoClient=MongoClient("localhost",27017)
    val db=mongoClient("test")
    val coll=db("casbah")
    val a=MongoDBObject("x"->1)
    val b=MongoDBObject("x"->2)
    coll.insert(a)
    coll.insert(b)
    val allDocs=coll.find()
  }
}
