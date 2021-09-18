
<template>
    <!-- 告警首页 -->
    <div class="components-container container-view">
        <!-- <div class="component-edit-button"
         @click="goEdit">
      <i class="el-icon-edit-outline"></i>
    </div> -->
        <!-- <div class="component-condition clearfix">
            <el-radio-group class="yw-separate-button-wrap"
                            v-model="conditionParams.poolActive"
                            @change="changeTab">
                <el-radio-button :label="item.value"
                                 v-for="(item,index) in conditionParams.poolList"
                                 :key="index">{{item.name}}</el-radio-button>
            </el-radio-group>
            <el-date-picker class="fr"
                            v-model="conditionParams.dateRange"
                            @change="changeTab"
                            value-format="yyyy-MM-dd"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
            </el-date-picker>
        </div> -->
        <div class="component-list">
            <!-- <section class="component-section"
               style="width:24%">
        <template v-if="resetComponent">
          <component :is="item.componentName.default"
                     class="component-item"
                     :conditionParams="conditionParams"
                     v-for="(item,index) in [componentsRequire[0],componentsRequire[1],componentsRequire[2]]"
                     :key="index"></component>
        </template>
      </section>
      <section class="component-section"
               style="width:50%">
        <template v-if="resetComponent">
          <component :is="item.componentName.default"
                     class="component-item"
                     :conditionParams="conditionParams"
                     v-for="(item,index) in [componentsRequire[3]]"
                     :key="index"></component>
        </template>
      </section>
      <section class="component-section"
               style="width:24%">
        <template v-if="resetComponent">
          <component :is="item.componentName.default"
                     class="component-item"
                     :conditionParams="conditionParams"
                     v-for="(item,index) in [componentsRequire[4],componentsRequire[5],componentsRequire[6]]"
                     :key="index"></component>
        </template>
      </section>
      <template v-if="resetComponent">
        <component :is="item.componentName.default"
                   class="component-item"
                   :conditionParams="conditionParams"
                   v-for="(item,index) in componentsRequire.slice(7)"
                   :key="index"></component>
      </template> -->
            <template v-if="resetComponent">
                <component :is="item.componentName.default"
                           class="component-item"
                           :conditionParams="conditionParams"
                           v-for="(item,index) in componentsRequire"
                           :key="index"></component>
            </template>
        </div>
    </div>
</template>
 
<script>
    import updateComponent from 'src/utils/updateComponent.js'
    export default {
        mixins: [updateComponent],
        components: {

        },
        data() {
            return {
                // 查询条件
                conditionParams: {
                    // 日期范围
                    dateRange: [],

                    // 资源
                    poolActive: '',
                    poolList: [{ value: '', name: '全部' }]
                },

                // 组件数据
                componentsData: [],
            }
        },

        computed: {
            // 动态引入组件
            componentsRequire() {
                let componentsRequire = []
                this.componentsData.forEach(item => {
                    componentsRequire.push({ componentName: require(`src/pages/settings/customization/components/${item.componentName}.vue`) })
                })
                return componentsRequire
            }
        },


        methods: {
            // 获得数据
            getDatas() {
                this.getParams()
                this.getComponents()
            },

            // 获得查询条件
            getParams() {
                let params = {
                    type: 'idcType',
                    pid: '',
                    pValue: '',
                    pType: '',
                }
                this.$api.queryPool(params).then((res) => {
                    this.conditionParams.poolList = res
                    this.conditionParams.poolList.unshift({ name: '全部', value: '' })
                    this.conditionParams.poolActive = ''
                })
            },

            // 获得模块数据
            getComponents() {

                // let params = {
                //   'moduleCode': 'alert',
                // };
                // this.$api.queryModuleInfo(params).then((res) => {
                //   this.componentsData = JSON.parse(res.data).componentsData;
                // })

                this.componentsData = [
                    { componentName: 'IT-alert-overview' },
                    { componentName: 'IT-alert-hot' },
                    { componentName: 'alert-solveTime-serious' },
                    { componentName: 'alert-bar-pool' },
                    { componentName: 'alert-solveTime-high' },
                    { componentName: 'alert-bar-devieClass' },
                    { componentName: 'alert-solveTime-middle' },
                    //  { componentName: 'alert-chart-trend' },
                    //   { componentName: 'alert-chart-number' },
                ]

            },

            // 编辑页
            goEdit() {
                this.$store.commit('setComponent', { componentsDir: 'alert', componentsData: this.componentsData })
                this.$router.push({ path: '/settings/customization/edits' })
            },

            // 条件查询
            changeTab() {
                // 更新组件
                this.updateComponent()
            }
        },
        created() {
            this.getDatas()
        },
    }
</script>
 
<style  lang="scss" scoped>
    //自己样式
    /deep/.component-list {
        display: grid;
        grid-template-columns: 24.2% 50% 24.2%;
        justify-content: space-between;
        align-content: start;
        width: 100%;
        .component-item {
            width: 100% !important;
            &:nth-child(2) {
                grid-row: span 3;
            }
            &:nth-child(8),
            &:nth-child(9) {
                grid-column: span 3;
            }
        }
    }
    /deep/.component-section {
        .component-item {
            width: 100% !important;
        }
    }
</style>
