import com.mongodb.casbah.Imports.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.jsoup.Jsoup

/**
  * Created by sunning on 2017/5/5.
  */
object ZCrawler {
  val mongoClient = MongoClient()
  val db = mongoClient("db")
  val coll = db("z_crawler")

  def main(args: Array[String]): Unit = {
    for (pageNum <- 0 to 9) {
      val doc = Jsoup.connect("https://www.amazon.cn/product-reviews/B01M031TE3/ref=cm_cr_arp_d_paging_btm_2?pageNumber=" + pageNum).get
      val comments = doc.select(("span.review-text"))
      for {
        i <- 0 until comments.size()
        comment = comments.get(i)
      } yield store(comment.text())
      println("success in page" + pageNum)
    }
  }

  def store(comment: String): Unit = {
    val MGDBO = MongoDBObject("comment" -> comment)
    coll.insert(MGDBO)
  }
}
