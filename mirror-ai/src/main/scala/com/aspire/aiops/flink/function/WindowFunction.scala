package com.aspire.aiops.flink.function

import java.{lang, util}
import java.util.{Comparator, HashMap, Map}
import com.aspire.aiops.elasticsearch.PointDataQueryHelper
import com.aspire.aiops.utils.TimeUtil
import org.apache.flink.api.common.functions.{JoinFunction}
import org.apache.flink.ml.math.SparseVector
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.slf4j.LoggerFactory

import scala.collection.{Set, mutable}
import scala.collection.mutable.HashSet

/**
  * @author ：Vincent Hu
  * @date ：Created in 6/27/2019 21:23
  * @description ：$ {description}
  * @modified By：
  * @version: $version$
  */
object WindowFunction {

  val logger = LoggerFactory.getLogger(WindowFunction.getClass)
  val esHelper:PointDataQueryHelper = new PointDataQueryHelper()

  import org.apache.flink.streaming.api.scala.function.{AllWindowFunction,WindowFunction}
  class MyAllWindowFunction extends AllWindowFunction[
    (Int,Int,Int,Double),
    (Int,Int,Array[Int], Array[Double]),
    TimeWindow]{
    override def apply(window: TimeWindow,
                       input: Iterable[(Int, Int, Int, Double)],
                       out: Collector[(Int, Int, Array[Int],Array[Double])]): Unit = {
      //val list:util.ArrayList[(Int, Int, Double)] = new util.ArrayList()
      logger.info("========MyAllWindowFunction=======")
      val arr = input.toArray
      //println("arr: "+arr.toList)
      val map:Map[Int,(Array[Int],Array[Double])] = new util.HashMap[Int,(Array[Int],Array[Double])]()
      //println("arr size:"+arr.size)
      for(i<-arr){
        //list.add((i._2,i._3,i._4))
        //out.collect((i._2,i._3,i._4))
        //获取数组值
        if(map.containsKey(i._2)){
          val mapValue = map.get(i._2)
          val items = (mapValue._1):+i._3
          val values = (mapValue._2):+i._4
          map.put(i._2,(items,values))
        }else{
          map.put(i._2,(Array(i._3), Array(i._4)))
        }
      }
      //println(map.values().toArray.toString)

      //获取最大长度
      var itemLen:Set[Int] = Set()
      var clocks:Set[Int] = Set()
      val keys = map.keySet().toArray
      for(k0<-keys){
        val k:Int = k0.toString.toInt
        val value = map.get(k)
        val len = value._1.size
        clocks = clocks ++ value._1.toSet
        itemLen += len
      }

      val maxLen = itemLen.max
      val maxClock = clocks.max
      //println("maxLen: "+ maxLen)

      //添加最大长度
      var list = Array[(Int, Int, Array[Int], Array[Double])]()
      for(k0<-keys){
        //println("k: " + k0)
        val k:Int = k0.toString.toInt
        val value = map.get(k)

        //val len = value._1.size
        //itemLen.+(len)
        val value1 = value._1.map(maxClock-_)
        //println("value1: "+value._1.toList.size)
        //println("value2: "+value._2.toList.size)
        /*println("value1: "+value._1.toList)
        println("value2: "+value._2.toList)*/
        //out.collect((maxLen, k, value._1, value._2))
        list = list:+(maxLen, k, value1, value._2)
        out.collect((maxLen, k, value1, value._2))
      }

      /*val in = list
      for(i<- 0 to in.size-2){
        val vector1 = SparseVector(in(i)._1,in(i)._3,in(i)._4)
        for(j<-i+1 to in.size-1){
          val vector2 = SparseVector(in(j)._1,in(j)._3,in(j)._4)
          //out.collect(1, in(i)._2, in(j)._2, null)
        }
      }*/
    }
  }

  class MyAllWindowFunction22 extends AllWindowFunction[(Int,Int,Array[Int],Array[Double]),
    (Int,HashMap[Int,SparseVector]), //out
    TimeWindow]{
    override def apply(window: TimeWindow,
                       input: Iterable[(Int, Int, Array[Int], Array[Double])],
                       out: Collector[(Int,HashMap[Int,SparseVector])]
                      //out:Collector[(Int,Int,SparseVector)]
                      ): Unit = {
      logger.info("========MyAllWindowFunction22=======")
      val in = input.toArray
      logger.info("MyAllWindowFunction22 input size:"+in.size)
      val map:HashMap[Int,SparseVector] = new HashMap[Int,SparseVector]()
      for(i<- 0 to in.size-1){
        //logger.info("i: "+i)
        val vector1 = SparseVector(in(i)._1,in(i)._3,in(i)._4)
        val item1 = in(i)._2
        map.put(item1,vector1)
        //out.collect(1,item1,vector1)
      }
      out.collect(1,map)

      /*if(!map.isEmpty){
        val map2:HashMap[HashSet[Int],Int] = new HashMap[HashSet[Int],Int]()
        /*combine*/
        val values = map.values().toArray()
        var i = 1
        for(v<-values){
          map2.put(v.asInstanceOf[HashSet[Int]],i)
          i+=1
        }

        //println(values)
        for(i <- 0 to values.size-2){
          for(j<-i+1 to values.size-1){
            //println(i)
            val pre = values(i).asInstanceOf[HashSet[Int]]
            val pos = values(j).asInstanceOf[HashSet[Int]]
            val inter = pre.intersect(pos)
            val jsim = (pre & pos).size.toFloat/(pre | pos).size.toFloat
            if(jsim>0.6){
              /*println("="*30)
              println("j: ", jsim)
              println("pre: ", pre)
              println("pos: ", pos)
              println("inter: ", inter)*/
              map2.put(pos,map2.get(pre))
            }
          }
        }

        println(">>"*30)
        println(map2)
        /*add to out*/
        //out.collect(map)

        val map3:HashMap[Int,HashSet[Int]] = new HashMap[Int,HashSet[Int]]()
        val keys3 = map2.keySet().toArray()
        for(k <- keys3){
          val i = k.asInstanceOf[HashSet[Int]]
          if(map3.containsKey(map2.get(i))){
            val newSet = map3.get(map2.get(i)) union i
            map3.put(map2.get(i),newSet)
          }else{
            map3.put(map2.get(i),i)
          }
        }

        println("map3<<<<<<<<<<<<<<<<<<<<<<<<<")
        println(map3)

        //artifact data
        map3.put(2,map3.get(1))
        map3.put(3,map3.get(1))
        map3.put(4,map3.get(1))
        map3.put(5,map3.get(1))

        out.collect(map3)

        /*for(k<-map3.keySet().toArray){
          val k1 = k.asInstanceOf[Int]
          val v1 = map3.get(k1).toList
          out.collect((k1,v1))
        }*/
      }*/
    }
  }

  import org.apache.flink.ml.metrics.distances.CosineDistanceMetric
  val cdm = CosineDistanceMetric()
  class MyAllWindowFunction2 extends AllWindowFunction[(Int,Int,Array[Int],Array[Double]),
    Map[Int,HashSet[Int]], //out
    TimeWindow]{
    override def apply(window: TimeWindow, input: Iterable[(Int, Int, Array[Int], Array[Double])],
                       out: Collector[Map[Int,HashSet[Int]]]
                      ): Unit = {
      val in = input.toArray
      val map:HashMap[Int,HashSet[Int]] = new HashMap[Int,HashSet[Int]]()
      for(i<- 0 to in.size-2){
        val vector1 = SparseVector(in(i)._1,in(i)._3,in(i)._4)
        val item1 = in(i)._2
        for(j<-i+1 to in.size-1){
          val item2 = in(j)._2
          val vector2 = SparseVector(in(j)._1,in(j)._3,in(j)._4)
          val sim = cdm.distance(vector1,vector2)
          if(sim>=0.6||sim<=(-0.6)){
            if(map.containsKey(item1)){
              val newValue = map.get(item1) += item2
              map.put(item1, newValue)
            }else{
              map.put(item1,HashSet[Int](item1,item2))
            }
            //out.collect("123456", in(i)._2, in(j)._2, sim)
          }
        }
      }

      if(!map.isEmpty){
        val map2:HashMap[HashSet[Int],Int] = new HashMap[HashSet[Int],Int]()
        /*combine*/
        val values = map.values().toArray()
        var i = 1
        for(v<-values){
          map2.put(v.asInstanceOf[HashSet[Int]],i)
          i+=1
        }

        //println(values)
        for(i <- 0 to values.size-2){
          for(j<-i+1 to values.size-1){
            //println(i)
            val pre = values(i).asInstanceOf[HashSet[Int]]
            val pos = values(j).asInstanceOf[HashSet[Int]]
            val inter = pre.intersect(pos)
            val jsim = (pre & pos).size.toFloat/(pre | pos).size.toFloat
            if(jsim>0.6){
              /*println("="*30)
              println("j: ", jsim)
              println("pre: ", pre)
              println("pos: ", pos)
              println("inter: ", inter)*/
              map2.put(pos,map2.get(pre))
            }
          }
        }

        println(">>"*30)
        println(map2)
        /*add to out*/
        //out.collect(map)

        val map3:HashMap[Int,HashSet[Int]] = new HashMap[Int,HashSet[Int]]()
        val keys3 = map2.keySet().toArray()
        for(k <- keys3){
          val i = k.asInstanceOf[HashSet[Int]]
          if(map3.containsKey(map2.get(i))){
            val newSet = map3.get(map2.get(i)) union i
            map3.put(map2.get(i),newSet)
          }else{
            map3.put(map2.get(i),i)
          }
        }

        println("map3<<<<<<<<<<<<<<<<<<<<<<<<<")
        println(map3)

        //artifact data
        map3.put(2,map3.get(1))
        map3.put(3,map3.get(1))
        map3.put(4,map3.get(1))
        map3.put(5,map3.get(1))

        out.collect(map3)

        /*for(k<-map3.keySet().toArray){
          val k1 = k.asInstanceOf[Int]
          val v1 = map3.get(k1).toList
          out.collect((k1,v1))
        }*/
      }
    }
  }

  class MyAllWindowFunction3 extends AllWindowFunction[
    Map[Int,HashSet[Int]],
    (Int,Int),
    TimeWindow
    ]{
    override def apply(window: TimeWindow, input: Iterable[Map[Int, mutable.HashSet[Int]]], out: Collector[(Int, Int)]): Unit = {
      val in = input.toArray
      for(i<-in){
        val keys = i.keySet().toArray
        for(k <- keys){
          val key = k.asInstanceOf[Int]
          val value = i.get(key)
          for(v<-value){
            out.collect((key, v))
          }
        }
      }
    }
  }

  /*class MysqlQueryWindowFunction extends AllWindowFunction[(HashMap[String,Long],util.List[String]),Collector[Any],TimeWindow]{
    override def apply(window: TimeWindow, input: Iterable[(HashMap[String,Long],util.List[String])], out: Collector[Collector[Any]]): Unit = {
      val in = input.toArray.take(1)


    }
  }*/

  /*class ResultFunction extends CoFlatMapFunction[
    (Int,(String,String,String,String,String)),
    (Int,util.HashMap[Int,SparseVector]),
    (String,Int,Double)]{

    override def flatMap1(in1: (Int, (String, String, String, String, String)), collector: Collector[(String, Int, Double)]): Unit = {

    }
  }*/


  class JoinFun extends JoinFunction[(Int,(String,String,String,String,String)),
    (Int,HashMap[Int,SparseVector]),
    (String, String, String, String, String,HashMap[Int,SparseVector])]{
    override def join(in1: (Int, (String, String, String, String, String)),
                      in2: (Int,HashMap[Int,SparseVector])): (String, String, String, String, String,HashMap[Int,SparseVector]) = {
      //logger.info("========JoinFunction========")
      (in1._2._1,in1._2._2,in1._2._3,in1._2._4,in1._2._5,in2._2)
    }
  }

  class JoinFun2 extends JoinFunction[
    (Int,util.List[(String, String, String, String, String)]),
    (Int,HashMap[Int,SparseVector]),
    (util.List[(String, String, String, String, String)],HashMap[Int,SparseVector])]{
    override def join(in1: (Int,util.List[(String, String, String, String, String)]),
                      in2: (Int,HashMap[Int,SparseVector])): (util.List[(String, String, String, String, String)],HashMap[Int,SparseVector]) = {
      //logger.info("========JoinFunction========")
      //(in1._2._1,in1._2._2,in1._2._3,in1._2._4,in1._2._5,in2._2)
      (in1._2,in2._2)

    }
  }

  class JoinFun3 extends JoinFunction[
    (Int,util.List[(String, String, String, String, String)]),
    (Int,HashMap[Int,SparseVector]),
    (util.List[(String, String, String, String, String)],HashMap[Int,SparseVector])]{
    override def join(in1: (Int,util.List[(String, String, String, String, String)]),
                      in2: (Int,HashMap[Int,SparseVector])):
    (util.List[(String, String, String, String, String)],HashMap[Int,SparseVector]) = {
      //logger.info("========JoinFunction========")
      //(in1._2._1,in1._2._2,in1._2._3,in1._2._4,in1._2._5,in2._2)
      //sim
      (in1._2,in2._2)

    }
  }

  class KafkaOriginFunction extends AllWindowFunction[
    (String,String,String),
    (Int,(String,String,String,String,String)),
    TimeWindow]{
    override def apply(
                        window: TimeWindow,
                        input: Iterable[(String,String,String)],
                        out: Collector[(Int, (String, String, String, String, String))]):
    Unit = {
      val times = input.toArray.map(_._2)
      val in = input.toArray
      //val list:util.List[(Int, (String, String, String, String, String))]=new util.ArrayList()
      for(i <- in){
        out.collect(1,(times.min, times.max, i._3, i._1, i._2))
        //list.add((1,(times.min, times.max, i.alertId, i.itemid, i.curMoniTime)))
      }
      //out.collect(list)
    }
  }

  class KafkaOriginFunction2 extends AllWindowFunction[
    (String,String,String),
    (Int,util.List[(String, String, String, String, String)]),
    TimeWindow]{
    override def apply(
                        window: TimeWindow,
                        input: Iterable[(String,String,String)],
                        out: Collector[(Int,util.List[(String, String, String, String, String)])]
                      ):Unit = {
      val times = input.toArray.map(_._3)
      val in = input.toArray
      val list:util.List[(String, String, String, String, String)]=new util.ArrayList()
      for(i <- in){
        //out.collect(1,(times.min, times.max, i.alertId, i.itemid, i.curMoniTime))
        list.add((times.min, times.max, i._1, i._2, i._3))
      }
      out.collect((1,list))
    }
  }

  class EsAllWindowQueryFunction extends AllWindowFunction[
    (Map[String,java.lang.Long],util.List[Integer]),
      util.List[util.Map[String, Object]],
    TimeWindow
    ]{
    override def apply(window: TimeWindow, input: Iterable[(util.Map[String, lang.Long], util.List[Integer])],
                       out: Collector[util.List[util.Map[String, Object]]]): Unit = {
      val inputs = input.toArray.take(1)(0)
      val res = esHelper.query(inputs._1,inputs._2)
      esHelper.responseParser(res)
    }

  }

  class SimilarityFunction extends AllWindowFunction[(util.List[(String, String, String, String, String)],HashMap[Int,SparseVector]),
    ((String, String, String, String, String),Int,Double),
    TimeWindow]{
    override def apply(window: TimeWindow,
                       input: Iterable[(util.List[(String, String, String, String, String)], util.HashMap[Int, SparseVector])],
                       out: Collector[((String, String, String, String, String),Int,Double)]): Unit = {
      println("===============SimilarityFunction start========================")
      for (in <- input.toArray){
        var items = in._1.toArray()//.asInstanceOf[Array[(String, String, String, String, String)]]
        val vecs = in._2.asInstanceOf[util.HashMap[Int, SparseVector]]
        for(item_ <- items){
          val item = item_.asInstanceOf[(String, String, String, String, String)]
          val itemid = item._4.toInt
          //println("itemid: "+itemid)
          var itemVec:SparseVector = null
          if(vecs.containsKey(itemid)){
            itemVec = vecs.get(itemid)
          }else{
            val values = vecs.values().toArray
            itemVec = values(1).asInstanceOf[SparseVector]
          }
          val keys = vecs.keySet
          for(k <- keys.toArray){
            try {
              if(vecs.containsKey(k)){
                val vec = vecs.get(k.toString.toInt)
                val sim = cdm.distance(itemVec,vec)
                if(sim>0.6 ||sim< -0.6){
                  out.collect((item,k.toString.toInt,sim))
                }
              }else{
                out.collect((item,k.toString.toInt,-999))
                logger.warn("collect -999")
              }
            } catch {
              case e => e.printStackTrace()
            }
          }
        }
      }
      println("===============SimilarityFunction finished========================")
    }
  }


  import scala.collection.JavaConversions._
  class TopNFunction2 extends ProcessWindowFunction[
    (String,String,String,String,String,Int,Double, String,String,String,String),
    (String,String,String,String,String,Int,Double, String,String,String,String),
    String,
    TimeWindow]{

    override def process(key: String,
                         context: ProcessWindowFunction[(String, String, String, String, String, Int, Double, String, String, String, String), (String, String, String, String, String, Int, Double, String, String, String, String),
                           String,
                           TimeWindow]#Context,
                         input: lang.Iterable[(String, String, String, String, String, Int, Double, String, String, String, String)],
                         collector: Collector[(String, String, String, String, String, Int, Double, String, String, String, String)]): Unit = {

      val treemap = new util.TreeMap[Long, (String, String, String, String, String, Int, Double, String, String, String, String)](new Comparator[Long]() {
        override def compare(y: Long, x: Long): Int = { // TODO Auto-generated method stub
          if (x < y) -1
          else 1
        }
      })



      for (element <- input) {
        treemap.put(TimeUtil.time2Seconds(element._10), element)
        if (treemap.size > 1) treemap.pollLastEntry
      }

      //import scala.collection.JavaConversions._
      for (entry <- treemap.entrySet) {
        collector.collect(entry.getValue)
      }


    }

  }

  class MyAllWindowFunction4 extends AllWindowFunction[
    (String,String,String,String,String,Int,Double, String,String,String,String),
    (String,String,String,String,String,Int,Double, String,String,String,String),
    TimeWindow]{
    override def apply(window: TimeWindow, input: Iterable[(String, String, String, String, String, Int, Double, String, String, String, String)], out: Collector[(String, String, String, String, String, Int, Double, String, String, String, String)]): Unit = {
      val map = new util.HashMap[(String, String, String, String, String, Int, Double, String, String, String, String),Int]()
      for(in <- input){
        if(!map.containsKey(in)){
          map.put(in,1)
          out.collect(in)
        }
      }
    }
  }

}
