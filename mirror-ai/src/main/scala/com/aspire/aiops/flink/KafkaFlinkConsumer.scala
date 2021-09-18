package com.aspire.aiops.flink

import java.sql.Connection
import java.util
import java.util.Map

import com.aspire.aiops.elasticsearch.PointDataQueryHelper
import com.aspire.aiops.flink.function.WindowFunction._
import com.aspire.aiops.kafka.KafkaConfig
import com.aspire.aiops.utils.{ResourceUtil, TimeUtil}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.assigners.{GlobalWindows, ProcessingTimeSessionWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import java.util.HashMap

import com.alibaba.fastjson.JSON
import com.aspire.aiops.dao.AlarmDao
import com.aspire.aiops.dao.impl.AlarmDaoImpl
import com.aspire.aiops.flink.function.MysqlFunction.MySqlSinkFunction
import com.aspire.aiops.flink.function.WindowFunction.MyAllWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
import org.slf4j.LoggerFactory
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition



/**
  * @author ：Vincent Hu
  * @date ：Created in 6/11/2019 14:58
  * @description ：$ {description}
  * @modified By：
  * @version: $version$
  */

object KafkaFlinkConsumer {

  val logger = LoggerFactory.getLogger(KafkaFlinkConsumer.getClass)
  val dao:AlarmDao = new AlarmDaoImpl()
  val esHelper:PointDataQueryHelper = new PointDataQueryHelper()
  val props = ResourceUtil.loadResource("jdbc.properties")
  val mode = ResourceUtil.loadResource("config.properties").getProperty("mode")
  var connection:Connection = null
  val timeInterval = ResourceUtil.loadResource("config.properties").getProperty("time.interval").toInt

  def run(): Unit ={

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = KafkaConfig.getKafkaProperties("kafka.properties")
    println(properties)
    val topic = properties.getProperty("topic")
    val consumer = new FlinkKafkaConsumer010[String](topic,
      new SimpleStringSchema(),
      properties)

    val offsets: Map[KafkaTopicPartition, java.lang.Long]  =  new util.HashMap[KafkaTopicPartition, java.lang.Long]()

    val stream = env.addSource(consumer)

    val kds = stream.map{s=>{
      val ss = JSON.parseObject(s)
      (ss.get("z_alert_Id").toString,
        ss.get("z_itemId").toString,
        ss.get("curMoniTime").toString//.replace(".","-"),
        )}}
      //.countWindowAll(1)  //for dev
      .timeWindowAll(Time.minutes(1))

    //
    val kds2 = kds.apply((TimeWindow, in, out:Collector[(Int,(String,String,String,String,String))])=>{
      val times = in.toArray.map(_._3)
      println(">>>"*15)
      for(i <- in.toArray){
        out.collect(1,(times.min, times.max, i._1, i._2, i._3))
      }
    })

    val kds22 = kds.apply(new KafkaOriginFunction2)

    //查询mysql
    val ds2 = kds.max(2)
      .map{s=>{
        //var times = dao.selectTime(s.curMoniTime)
        val endTime = TimeUtil.time2Seconds(s._3)
        var timesMap:Map[String,java.lang.Long] = new HashMap()
        timesMap.put("startTime",endTime-timeInterval)
        timesMap.put("endTime",endTime)
        var items = dao.selectItems(s._3)
        (timesMap,items)
        }
      }

    val ds22 = kds.max(2).map{s=>{
      //var times = dao.selectTime(s.curMoniTime)
      val endTime = TimeUtil.time2Seconds(s._3)
      var timesMap:Map[String,java.lang.Long] = new HashMap()
      timesMap.put("startTime",endTime-timeInterval)
      timesMap.put("endTime",endTime)
      var items = dao.selectItems2(s._3)
      items
    }}.flatMap(_.toArray()).map(s=>{
      val map = s.asInstanceOf[HashMap[String,Object]]
      (map.get("alertId").toString,
        map.get("itemId").toString,
        map.get("alertStartTime").toString,
        map.get("curMoniTime").toString)
    })

    /*val ds33 = ds2.timeWindowAll(Time.minutes(1))
      .apply(new EsAllWindowQueryFunction).flatMap(_.toArray())
      .map{s=>{
        val ss = s.asInstanceOf[util.Map[String, Object]]
        (1,ss.get("itemid").toString.toInt, ss.get("clock").toString.toInt, ss.get("value").toString.toDouble)
      }}
      //.keyBy(0)
      .timeWindowAll(Time.minutes(1))
      .apply(new MyAllWindowFunction)*/

    //查询es
    val ds3 = ds2.map{s=>{
        //logger.info("sssssssssssssssssssssssssssssssssssssssssss")
        val res = esHelper.query(s._1,s._2)
        try {
        } catch {
          case  e =>{e.printStackTrace()}
        }
        var ps: util.List[util.Map[String, Object]]= esHelper.responseParser(res)
        if(ps.isEmpty && mode.equals("dev")){
          logger.warn("==>"*15)
          logger.warn("es data is empty")
          ps = esHelper.responseParser(esHelper.querySample())
        }
        ps
      }}
      .flatMap(_.toArray())
      .map{s=>{
        val ss = s.asInstanceOf[util.Map[String, Object]]
        (1,ss.get("itemid").toString.toInt, ss.get("clock").toString.toInt, ss.get("value").toString.toDouble)
      }}
      .timeWindowAll(Time.minutes(1))
        .apply(new MyAllWindowFunction)

        val ds4 = ds3
      .timeWindowAll(Time.minutes(1))
        .apply(new MyAllWindowFunction22)

    //接入原数据
    val ds5 = kds22.join(ds4).where(_._1).equalTo(_._1)
      .window(ProcessingTimeSessionWindows.withGap(Time.minutes(1)))
      .apply(new JoinFun2)

    //计算相似度
    val ds6 = ds5.timeWindowAll(Time.minutes(1))
      .apply(new SimilarityFunction)

    val ds7 = ds6.map{s=>{
      (s._1._1,s._1._2,s._1._3,s._1._4,s._1._5,s._2.toInt,s._3.toDouble)
    }}

    //关联原数据
    //val ds8 = ds7.join(ds22).where(_._4).equalTo(_._2) //only for display
    val ds8 = ds7.join(ds22).where(_._6.toString).equalTo(_._2.toString)
      .window(ProcessingTimeSessionWindows.withGap(Time.minutes(1)))
        .apply((in1,in2,out:Collector[(String,String,String,String,String,Int,Double, String,String,String,String)])=>{
          out.collect((in1._1, in1._2, in1._3, in1._4, in1._5, in1._6, in1._7,
            in2._1, in2._2, in2._3, in2._4
          ))
        })

    val ds9 = ds8
      .keyBy(3).minBy(10)
      .timeWindowAll(Time.minutes(1))
        .apply(new MyAllWindowFunction4)

    ds9.addSink(new MySqlSinkFunction)
    ds9.print()

    /*TODO*/
    env.execute("aiops flink kafka streaming")
  }


  def main(args: Array[String]): Unit = {
    run()
  }
  
}
