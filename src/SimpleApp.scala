import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by sunning on 2017/3/30.
  */

object SimpleApp {
  def main(args: Array[String]) {
    val logFile = "/Users/sunning/README.txt" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    sc.stop()
  }
}