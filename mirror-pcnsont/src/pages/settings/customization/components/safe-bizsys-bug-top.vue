<template>

  <div class="content-chart"
       style="width: 24.2%;">
    <section class="chart-title-wrap clearfix">
      <i class="chart-icon icon-rate"></i>
      <span class="chart-title">{{chartData.name}}</span>
    </section>

    <!-- 多图表 -->
    <section class="chart-section">
      <div class="chart-box-wrap">
        <div class="chart-box-item"
             style="width:100%"
             v-for="(subItem,subIndex) in chartData.chartList"
             :key="subIndex"
             :id="subItem.id"></div>
      </div>
    </section>
    <!-- 多图表 -->
  </div>
</template>

<script>
import echarts from 'echarts'
import ChartOption from 'src/utils/chartOption.js'
import DrawChart from 'src/utils/drawCharts.js'

export default {
  mixins: [DrawChart],
  data () {
    return {
      chartData: {
        name: '业务系统漏洞数Top5',
        filter: [{ name: '物理机', label: '物理机' }, { name: '虚拟机', label: '虚拟机' }],
        activeFilter: '物理机',
        chartList: [{ id: 'safe-bizsys-bug-top-1', chartOption: 'bar-option', chartDatas: '' },
        ],
      }
    }
  },
  methods: {
    //获得数据
    getDatas () {

      // let params = {
      //   'idcType': this.conditionParams.poolActive,
      //   'startDate': this.conditionParams.dateRange[0],
      //   'endDate': this.conditionParams.dateRange[1],
      //   'deviceType': this.chartData.activeFilter,
      //   'sourceType': 'cpu',
      // };
      // this.$api.queryPoolRate(params).then((res) => {
      //   this.chartData.chartList[0].chartDatas = res;//对象
      //   this.drawCharts();
      // })
      this.chartData.chartList[0].chartDatas = [
        {
          name: '成都产业研究院',
          high: 20,
          mid: 10,
          low: 30

        }, {
          name: '成都产业研究院',
          high: 20,
          mid: 10,
          low: 30
        }
      ];
      this.drawCharts();

    },

    //绘制图表
    drawCharts () {
      this.$nextTick(() => {
        this.chartData.chartList.forEach((subItem, subIndex) => {
          let myChart = echarts.init(document.getElementById(subItem.id));
          myChart.clear();
          //数据格式处理
          let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]));
          let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas));

          option.series[0] = JSON.parse(JSON.stringify(option.series[0]));
          option.series[1] = JSON.parse(JSON.stringify(option.series[0]));
          option.series[2] = JSON.parse(JSON.stringify(option.series[0]));

          option.series[0].name = '高危';
          option.series[1].name = '中危';
          option.series[2].name = '低危';

          option.legend.data = [option.series[0].name, option.series[1].name, option.series[2].name];
          option.series[0].itemStyle.color.colorStops = option.redColors[0];
          option.series[1].itemStyle.color.colorStops = option.redColors[1];
          option.series[2].itemStyle.color.colorStops = option.redColors[2];

          option.series[0].stack = option.series[1].stack = option.series[2].stack = 'chart-danger';
          option.series[0].itemStyle.barBorderRadius = option.series[1].itemStyle.barBorderRadius = 0;
          option.series[2].itemStyle.barBorderRadius = [4, 4, 0, 0];

          option.xAxis.axisLabel.rotate = 20;

          if (chartDatas && chartDatas.length > 0) {
            option.series[0].data = chartDatas.map((item) => {
              return item.high
            });
            option.series[1].data = chartDatas.map((item) => {
              return item.mid
            });
            option.series[2].data = chartDatas.map((item) => {
              return item.low
            });

            option.xAxis.data = chartDatas.map((item) => {
              return item.name
            });
          } else {
            option.series[0].data = [0];
            option.series[1].data = [0];
            option.series[2].data = [0];
            option.xAxis.data = ['暂无']
          }


          myChart.setOption(option);

          //resize自适应
          this.setResizeFun(myChart);
        })

      })
    },
    changeTab (val) {
      this.getDatas();
    },
  },
  mounted () {
    this.getDatas();
  }

}
</script>

<style lang="scss" scoped>
</style>

