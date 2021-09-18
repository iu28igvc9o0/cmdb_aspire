<template>
  <div class="components-container">
    <el-tabs class="yw-tabs"
             v-model="activeNames"
             @tab-click="handleClick">
      <el-tab-pane v-for="(item, index) in resourcePoors" :key="index" :label="item.name"
                   :name="item.value">
        <viewNetwork v-if="activeNames === item.value" :idc="item.description" ></viewNetwork>
      </el-tab-pane>
      <!--      <el-tab-pane label="信息港池内"-->
      <!--                   name="xinxigangchinei">-->
      <!--        <viewNetwork v-if="activeNames === 'xinxigangchinei'" idc="/xinxigangchineiNet" ></viewNetwork>-->
      <!--      </el-tab-pane>-->
      <!--      <el-tab-pane label="信息港非池"-->
      <!--                   name="xinxigangfeichi">-->
      <!--        <viewNetwork v-if="activeNames === 'xinxigangfeichi'" idc="/xinxigangfeichiNet" ></viewNetwork>-->
      <!--      </el-tab-pane>-->
      <!--      <el-tab-pane label="哈尔滨资源池"-->
      <!--                   name="hachi">-->
      <!--      <viewNetwork v-if="activeNames === 'hachi'" idc="/hachiNet"></viewNetwork>-->
      <!--    </el-tab-pane>-->
      <!--      <el-tab-pane label="呼和浩特资源池"-->
      <!--                   name="huchi">-->
      <!--        <viewNetwork v-if="activeNames === 'huchi'" idc="/huchiNet"></viewNetwork>-->
      <!--      </el-tab-pane>-->
      <!--      <el-tab-pane label="南基池内"-->
      <!--                   name="nanjichinei">-->
      <!--        <viewNetwork v-if="activeNames === 'nanjichinei'" idc="/nanjichineiNet"></viewNetwork>-->
      <!--      </el-tab-pane>-->
      <!--      <el-tab-pane label="南基池外"-->
      <!--                   name="nanjichiwai">-->
      <!--        <viewNetwork v-if="activeNames === 'nanjichiwai'" idc="/nanjichiwaiNet"></viewNetwork>-->
      <!--      </el-tab-pane>-->
      <!--      <el-tab-pane label="深基"-->
      <!--                   name="shenji">-->
      <!--        <viewNetwork v-if="activeNames === 'shenji'" idc="/shenjiNet"></viewNetwork>-->
      <!--      </el-tab-pane>-->
    </el-tabs>

  </div>
</template>
<script>
  import viewNetwork from "./view-network.vue";
  import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
  export default {
    name: '/resource/iframe/resourceManagement',
    components: {
      viewNetwork
    },
    data() {
      return {
        activeNames: 'first',
        resourcePoors: [],
      }
    },
    mounted: function() {
      this.getResourcePoor()
    },
    watch: {
    },
    methods: {
      // 获取资源池
      getResourcePoor () {
        let obj = {
          'type': 'hongdaNet',
          'pid': ''
        }
        rbProjectDataServiceFactory.getConfigDictByType(obj).then((res) => {
          if (res) {
            let url = window.location.href
            url = url.split('://')[1]
            url = url.split('/')[0]
            url = url.split(':')[0]
            this.resourcePoors = []
            for (let i in res) {
              let pool = res[i]
              let description = pool.description
              if (description) {
                description = description.replace('ipAddr', url)
                pool.description = description
              }
              this.resourcePoors.push(pool)
            }
          }
        })
      },
      handleClick() {
      }
    }
  }
</script>
<style lang="stylus" scoped>

</style>
