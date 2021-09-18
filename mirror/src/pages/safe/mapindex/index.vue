
<template>
    <!-- 安全首页 -->
    <div class="components-container container-view">
        <!-- <div class="component-edit-button"
         @click="goEdit">
      <i class="el-icon-edit-outline"></i>
    </div> -->
        <div class="component-condition clearfix">
            <el-date-picker class="yw-date-editor-big"
                            v-model="conditionParams.dateRange"
                            style="width:338px"
                            @change="changeTab"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd"
                            type="daterange"
                            :clearable="false"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
            </el-date-picker>
        </div>
        <div class="component-list">
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
    import { formatDate2 } from 'src/assets/js/utility/rb-filters.js'

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
            // 获得查询条件
            getParams() {
                let now = new Date()
                let before = now.getTime() - 1000 * 60 * 60 * 24 * 30// 30天
                this.conditionParams.dateRange = [formatDate2(before), formatDate2(now)]
                // let params = {
                //   type: 'idcType',
                //   pid: '',
                //   pValue: '',
                //   pType: '',
                // };
                // this.$api.queryPool(params).then((res) => {
                //   this.conditionParams.poolList = res;
                //   this.conditionParams.poolList.unshift({ name: '全部', value: '' });
                //   this.conditionParams.poolActive = '';
                // })
            },

            // 获得模块数据
            getComponents() {

                this.componentsData = [
                    { 'componentName': 'safe-scan-overview' },
                    { 'componentName': 'safe-repair-overview' },
                    { 'componentName': 'safe-bug-trend' },
                    { 'componentName': 'safe-scan-details' },

                    { 'componentName': 'safe-bug-statistics' },
                    { 'componentName': 'safe-bug-level' },
                    { 'componentName': 'safe-scan-list' },

                    { 'componentName': 'safe-attack-notice' },
                    { 'componentName': 'safe-event-notice' },
                    { 'componentName': 'safe-patch-notice' },
                    { 'componentName': 'safe-scan-task' },
                ]

                // let params = {
                //   'moduleCode': 'home',
                // };
                // this.$api.queryModuleInfo(params).then((res) => {
                //   this.componentsData = JSON.parse(res.data).componentsData;
                // })
            },

            // 编辑页
            goEdit() {
                this.$store.commit('setComponent', { componentsDir: 'home', componentsData: this.componentsData })
                this.$router.push({ path: '/settings/customization/edits' })
            },

            // 条件查询
            changeTab() {
                // 更新组件
                this.updateComponent()
            }
        },
        created() {
            this.getParams()
            this.getComponents()
        },
    }
</script>
 
<style  lang="scss" scoped>
    //自己样式
    /deep/ .yw-date-editor-big {
        &.el-input__inner {
            .el-icon-date {
                &:before {
                    display: inline-block;
                    content: "扫描时间范围";
                    word-break: keep-all;
                }
            }
        }
    }
</style>
