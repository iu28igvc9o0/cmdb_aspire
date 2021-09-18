<template>
    <!-- 进度条 -->
    <div>
        <!-- 单行进度条（标签在外面） -->
        <section class="yw-progress"
                 v-if="option.type==='single'">
            <p class="progress-text"
               v-if="option.title && option.title.show"
               :class="[option.title.position,rankColor(datas.index)]">
                {{datas.name}}
            </p>
            <el-progress :percentage="datas.percentage"
                         v-if="datas.percentage || datas.percentage === 0"
                         :show-text="option.label.percentage.show"
                         :stroke-width="12"
                         :class="rankBgColor(datas.index)"
                         :style="option.style"></el-progress>
            <span class="progress-num text-ellipse"
                  v-if="option.label.num && option.label.num.show"
                  :class="rankColor(datas.index)">{{datas.num}}</span>
        </section>

        <!-- 多行进度条（标签在里面） -->
        <section class="yw-progress"
                 style="margin-bottom:7px"
                 v-for="(subItem,subIndex) in datas.list"
                 :key="subIndex">
            <p class="progress-text clearfix"
               :class="datas.status">
                <el-tooltip class="item"
                            effect="dark"
                            :disabled="!subItem.tip"
                            :content="subItem.tip"
                            placement="top-start">
                    <span>{{subItem.name}}</span>
                </el-tooltip>
            </p>
            <el-progress :percentage="subItem.rate"
                         class="text-inside"
                         :class="datas.status"
                         :stroke-width="22"
                         :show-text="false"></el-progress>
            <p class="progress-inner-text text-ellipse"><span v-if="projectName!='集中网管'">已分配{{subItem.num}}/总</span>{{subItem.total}}</p>
        </section>
    </div>

</template>
<script>
    export default {
        props: {
            // 数据
            datas: {
                type: Object,
                default: {
                    percentage: 50,
                    index: 0
                }
            },
            // 配置
            option: {
                type: Object,
                default: function () {
                    return {
                        // 类型
                        type: 'multiple',// ['single','multiple']
                        // 标题
                        title: {
                            position: 'up',// ['up','down','left','right']
                        },
                        // 样式
                        style: {
                            width: '100%'
                        },
                        // 标签
                        label: {
                            // 位置
                            position: 'outer',// ['outer','inner']
                            // 百分比
                            percentage: {
                                show: true
                            },
                            // 本身数据
                            num: {
                                show: false
                            }
                        }
                    }
                }
            }
        },
        data() {
            return {
                // datas: [{ name: '服务器', list: [], status: 'blue' }]
                projectName: window.projectName
            }
        },
        computed: {
            // 排行榜字体颜色设置
            rankColor() {
                return function (index) {
                    let obj = 'color-base'
                    switch (index) {
                        case 1:
                            obj = 'color-red'
                            break
                        case 2:
                            obj = 'color-orange'
                            break
                        case 3:
                            obj = 'color-yellow'
                            break
                        // case 4:
                        //   obj = 'color-blue';
                        //   break;
                        default:
                            obj = 'color-base'
                            break
                    }
                    return obj
                }
            },
            // 进度条背景色颜色设置
            rankBgColor() {
                return function (index) {
                    let obj = ''

                    switch (index) {
                        case 1:
                            obj = 'red'
                            break
                        case 2:
                            obj = 'orange'
                            break
                        case 3:
                            obj = 'yellow'
                            break
                        default:
                            obj = 'blue'
                            break
                    }
                    return obj
                }
            },
        },

        methods: {

        },
        mounted() {

        }

    }
</script>
<style lang="scss" scoped>
</style>
