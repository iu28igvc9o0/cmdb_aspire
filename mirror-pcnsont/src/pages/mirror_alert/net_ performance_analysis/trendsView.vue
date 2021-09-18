<template>
    <el-dialog class="yw-dialog"
               :width="dialogWidth"
               :title="title"
               :close-on-click-modal="false"
               :visible.sync="dialogMsg.dialogVisible">
        <el-form
                class="components-condition yw-form yw-chart-form is-required"
                label-width="70px"
                :inline="true"
                :model="item"
        >
            <el-form-item
                    prop="item.report_name"
                    label="资源池"

            >
             <div style="min-width:84px;"><span >{{dialogMsg.data.idcType}}</span></div>
            </el-form-item>
            <el-form-item
                    prop="item.time_value"
                    label="设备ip"

            >
                <span>{{dialogMsg.data.ip}}</span>
            </el-form-item>
            <el-form-item label="开始时间">
                <el-date-picker
                        v-model="dateRange1"
                        style="width:280px"
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="时间粒度">
                <el-select style="width:80px" v-model="granularity" placeholder="请选择" >
                    <el-option label="日" value="day"></el-option>
                    <el-option label="周" value="week"></el-option>
                    <el-option label="月" value="month"></el-option>
                </el-select>

            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary" size="mini" @click="queryType">查询</el-button>
                <el-button size="mini" @click="exportData">导出</el-button>
            </section>
        </el-form>
        <div
                id="trendsId"
                style="width: calc(100% - 20px);overflow:hidden;min-height:300px;height:100%;
                box-sizing:border-box;padding:5px;float:left;border:1px solid #46bafe;"
        >
            <div  v-if="showFlag" style="width: 100%;height:100%;text-align: center;line-height: 300px;"
            >
                没有数据
            </div>
        </div>
    </el-dialog>

</template>
<script>
export default {
  props: ['dialogMsg'],

  data () {
    return {
      showFlag:true,
      granularity: '',
      dateRange1: [],
      title:'利用率数据详情',
      //dialog
      dialogWidth: '850px',

      //搜索
      currentType: {},

      //查询参数
      queryParams: {},

      //结果
      result: [],
      addResult: [],//临时数据
      submitResult: [],


    }
  },
  mounted () {
    if(this.dialogMsg.data.item === 'cpu'){
      this.title = 'cpu' + this.title
    }else{
      this.title = '内存' + this.title
    }
    this.dateRange1 =  this.dialogMsg.data.dateRange
    this.granularity = this.dialogMsg.data.granularity
    this.queryType();
  },
  methods: {
    getQueryParams(){
      let data = this.dialogMsg.data
      let paramsData = {
        ip: data.ip,
        granularity: this.granularity,
        sendTimeStart: this.dateRange1[0],
        sendTimeEnd: this.dateRange1[1],
        idcType: data.idcType,
        monitorFlag: data.item
      }
      this. queryParams = paramsData
    },
    //查询分类
    queryType () {
      let flagTime = this.checkTime()
      if(!flagTime){
        return
      }
      this.getQueryParams()
      this.rbHttp
        .sendRequest({
          method: "GET",
          url: `/v1/alerts/netPerformanceAnalysis/trends`,
          params:this.queryParams

        })
        .then(res => {
            this.paint(res)
        })

    },

    paint(res){
      let time = []
      let series = {}
      if(null == res || '' == res){
        this.showFlag = true
        return;
      }
      if(null !== res){
        time = res.xAxis
        series = res.series
      }
      if(time.length === 0){
        this.showFlag = true
        return;
      }
      var myChart = echarts.init(document.getElementById('trendsId'), 'macarons')
      this.showFlag = false
      let option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data:['最大利用率','平均利用率','最小利用率']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: time
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name:'最大利用率',
            type:'line',
            stack: '总量',
            data:series.max
          },
          {
            name:'平均利用率',
            type:'line',
            stack: '总量',
            data:series.avg
          },
          {
            name:'最小利用率',
            type:'line',
            stack: '总量',
            data:series.min
          }
        ]
      };
      myChart.setOption(option)
    },

    exportData(){
      let flagTime = this.checkTime()
      if(!flagTime){
        return
      }
      this.$emit("showLoading", "正在导出数据,由于数据量较大请耐心等待...");
      this.getQueryParams()
      this.rbHttp
        .sendRequest({
          method: "POST",
          url: `/v1/alerts/netPerformanceAnalysis/exportTrends`,
          params:this.queryParams,
          binary: true,
        })
        .then(res => {
          let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        let objectUrl = URL.createObjectURL(blob)
        // window.location.href = objectUrl
        let downLoadElement = document.createElement('a')
        downLoadElement.href = objectUrl
        downLoadElement.download = this.dialogMsg.data.ip + '_'+this.title  + '.xlsx'
        document.body.appendChild(downLoadElement)
        downLoadElement.click()
        document.body.removeChild(downLoadElement)
        URL.revokeObjectURL(objectUrl)
      }) .finally(() => {
        this.$emit("hideLoading");
      });

    },

    checkTime(){
      if(null == this.dateRange1 || null == this.dateRange1[0] || '' == this.dateRange1[0]){
        this.$message.warning('请选择时间，不能为空！')
      }
      let now1 = new Date(moment(this.dateRange1[1]).valueOf())
      now1.setDate(0);
      let num = now1.getDate() -1 ;
      let monthTime = 1000 * 60 * 60 * 24 * num
      if((moment(this.dateRange1[1]).valueOf() - moment(this.dateRange1[0]).valueOf()) > monthTime){
        this.$message.warning('时间范围请不要超过一个月!')
        return false
      }
      return true
    },

    //关闭弹窗
    closeDialog (val) {
      this.$emit('closeDialog', val);
    },

    setData(val){
      this.$emit('setData', val);
    }

  }
}

</script>
<style lang="scss" scoped>
.search-left {
  display: inline-block;
  vertical-align: top;
  width: 150px;
  max-height: 345px;
  margin-right: 15px;
  overflow: auto;
  .search-list {
    .search-item {
      height: 30px;
      line-height: 30px;
      padding: 0 10px;
      border: 1px solid transparent;
      margin-bottom: 2px;
      cursor: pointer;
      &:hover,
      &.active {
        background: #f4f4f4;
        border: 1px solid rgba(229, 232, 237, 1);
      }
    }
  }
}
</style>
