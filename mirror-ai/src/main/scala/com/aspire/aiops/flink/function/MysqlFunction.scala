package com.aspire.aiops.flink.function


import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.DriverManager

import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.configuration.Configuration
import com.aspire.aiops.utils.ResourceUtil
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

/**
  * @author ：Vincent Hu
  * @date ：Created in 6/27/2019 14:06
  * @description ：$ {description}
  * @modified By：
  * @version: $version$
  */
object MysqlFunction {

  val props = ResourceUtil.loadResource("jdbc.properties")
  var connection:Connection = null
  var preparedStatement:PreparedStatement = null

  class MySqlSinkFunction extends RichSinkFunction[(String,String,String,String,String,Int,Double,String,String,String,String)]{

    val sql = "insert into alert_root_cause(time1,time2,cur_alert_id,cur_item_id,cur_moni_time,his_alert_item_id," +
      "similarity,root_cause_alert_id,root_cause_start_time" +
      ") values(?,?,?,?,?,?,?,?,?);"

    override def open(parameters: Configuration): Unit = {
      super.open(parameters)
      Class.forName(props.getProperty("driverClassName"))
      if(connection == null){
        try {
          connection = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"))
          preparedStatement = connection.prepareStatement(sql)
        } catch {
          case e => e.printStackTrace()
        }
      }
    }

    override def close(): Unit = {
      super.close()
      if(preparedStatement != null){
        preparedStatement.close()
      }
      if(connection != null){
        connection.close()
      }
      super.close()
    }

    override def invoke(value: (String,String,String,String,String,Int,Double,String,String,String,String), context: SinkFunction.Context[_]): Unit = {
      try {
        preparedStatement.setString(1, value._1)
        preparedStatement.setString(2, value._2)
        preparedStatement.setString(3, value._3)
        preparedStatement.setString(4, value._4)
        preparedStatement.setString(5, value._5)
        preparedStatement.setString(6, value._6.toString)
        preparedStatement.setString(7, value._7.toString)
        preparedStatement.setString(8, value._8)
        preparedStatement.setString(9, value._10)
        preparedStatement.executeUpdate()
      } catch {
        case e => e.printStackTrace()
      }
    }

  }

  class TimeSourceFunction extends RichSourceFunction[Int]{

    var startTime="0"
    var endTime="0"
    var sql = "SELECT DISTINCT item_id FROM alert_alerts_detail "+
      " WHERE item_id IS NOT NULL AND " +
      " cur_moni_time > FROM_UNIXTIME(UNIX_TIMESTAMP(?), '%Y-%m-%d %H:%i:%S') AND " +
      " cur_moni_time < FROM_UNIXTIME(UNIX_TIMESTAMP(?), '%Y-%m-%d %H:%i:%S')"

    def this(startTime:String, endTime:String){
      this()
      this.startTime=startTime
      this.endTime = endTime
    }

    override def open(parameters: Configuration): Unit = {
      super.open(parameters)
      Class.forName(props.getProperty("driverClassName"))
      if(connection == null){
        try {
          connection = DriverManager.getConnection(props.getProperty("url"),
            props.getProperty("username"),
            props.getProperty("password"))
          preparedStatement = connection.prepareStatement(sql)
        } catch {
          case e => e.printStackTrace()
        }
      }
    }

    override def run(sourceContext: SourceFunction.SourceContext[(Int)]): Unit = {

      if(!startTime.equals("0")){
        preparedStatement.setString(1,startTime)
        preparedStatement.setString(2,endTime)
      }

      val resultSet = preparedStatement.executeQuery()
      while (resultSet.next()) {
        //val name = resultSet.getString("item_id")
        val id = resultSet.getInt("item_id")
        sourceContext.collect(id);
      }
    }

    override def cancel(): Unit = {
      try {
        super.close()
        if (connection != null) {
          connection.close()
        }
        if (preparedStatement != null) {
          preparedStatement.close()
        }
      } catch  {
        case e => e.printStackTrace()
      }
    }
  }


  /*class TableSinkFunction extends RichSinkFunction[RC]{

    val sql = "insert into alert_root_cause(time1,time2,cur_alert_id,cur_item_id,cur_moni_time,his_alert_item_id," +
      "similarity,root_cause_alert_id,root_cause_start_time" +
      ") values(?,?,?,?,?,?,?,?,?);"

    override def open(parameters: Configuration): Unit = {
      super.open(parameters)
      Class.forName(props.getProperty("driverClassName"))
      if(connection == null){
        try {
          connection = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"))
          preparedStatement = connection.prepareStatement(sql)
        } catch {
          case e => e.printStackTrace()
        }
      }
    }

    override def close(): Unit = {
      super.close()
      if(preparedStatement != null){
        preparedStatement.close()
      }
      if(connection != null){
        connection.close()
      }
      super.close()
    }

    override def invoke(value: RC, context: SinkFunction.Context[_]): Unit = {
      try {
        preparedStatement.setString(1, value.time1)
        preparedStatement.setString(2, value.time2)
        preparedStatement.setString(3, value.cur_alert_id)
        preparedStatement.setString(4, value.cur_item_id)
        preparedStatement.setString(5, value.cur_moni_time)
        preparedStatement.setString(6, value.his_alert_item_id.toString)
        preparedStatement.setString(7, value.similarity.toString)
        preparedStatement.setString(8, value.root_cause_alert_id)
        preparedStatement.setString(9, value.root_cause_start_time)
        preparedStatement.executeUpdate()
      } catch {
        case e => e.printStackTrace()
      }
    }

  }*/



}
