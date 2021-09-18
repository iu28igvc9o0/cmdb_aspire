<template>
    <!-- 表格(深色、自动滚动) -->
    <div class="yw-rank-table-wrap"
         ref="fixTable"
         :class="option && option.selfClass">
        <vue-scroll style="width:100%;height:100%;"
                    :ops="opsX">
            <!-- 表头 -->
            <el-table class="yw-rank-table tableTitle"
                      :data="[]"
                      :class="{ enClick: option && option['enClick'] }"
                      :row-class-name="tableRowClassName"
                      :show-header="(option && option.selfClass && option.selfClass.indexOf('no-header')>-1) ? false : true"
                      :highlight-current-row="option && option['highlight-current-row']"
                      :default-sort="(option && option.sort && option.sort.default) ? option.sort.default : {prop: '', order: ''}"
                      @row-click="clickTableRow"
                      @sort-change="sortTable">
                <el-table-column :prop="item.name"
                                 v-for="(item,index) in tableTitles"
                                 :label="item.title"
                                 :key="index"
                                 :sortable="item.style.sortable ? item.style.sortable : false"
                                 :width="item.style.width ? item.style.width : 'auto'"
                                 :align="item.style.align ? item.style.align : 'left'">
                    <template slot-scope="scope">
                        <div>
                            <el-tooltip effect="dark"
                                        popper-class="tooltip-width-250"
                                        placement="top-start">
                                <div slot="content">{{scope.row[item.name]}}</div>
                                <span class="text-ellipse"
                                      v-html="item.style.isHtml ? scope.row[item.name + 'Html']: scope.row[item.name]"></span>
                            </el-tooltip>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 表头 -->

            <!-- 表内容 -->
            <vue-scroll :style="{height: tableContentHeight}"
                        :ops="opsY">
                <vueSeamlessScroll :data="tableDatas"
                                   :class-option="defaultOption"
                                   class="seamless-warp">
                    <el-table :data="tableDatas"
                              :show-header="false"
                              class="yw-rank-table tableBody"
                              :row-class-name="tableRowClassName"
                              :class="{ enClick: option && option['enClick'] }"
                              :highlight-current-row="option && option['highlight-current-row']"
                              @row-click="clickTableRow">
                        <el-table-column :prop="item.name"
                                         v-for="(item,index) in tableTitles"
                                         :label="item.title"
                                         :key="index"
                                         :sortable="item.style.sortable ? item.style.sortable : false"
                                         :width="item.style.width ? item.style.width : 'auto'"
                                         :align="item.style.align ? item.style.align : 'left'">
                            <template slot-scope="scope">
                                <div>
                                    <el-tooltip effect="dark"
                                                popper-class="tooltip-width-250"
                                                placement="top-start">
                                        <div slot="content">
                                            <div v-if="item.style.isHtml"
                                                 v-html="scope.row[item.name + 'Tooltip'] ? scope.row[item.name + 'Tooltip']: scope.row[item.name + 'Html']">
                                            </div>
                                            <div v-else>{{scope.row[item.name]}}</div>
                                        </div>

                                        <span class="text-ellipse"
                                              v-if="item.style.isHtml"
                                              v-html="scope.row[item.name + 'Html']"></span>
                                        <span v-else
                                              class="text-ellipse">{{scope.row[item.name]}}</span>
                                    </el-tooltip>

                                    <!-- <el-popover placement="top-start"
                                                effect="dark"
                                                title=""
                                                width="200"
                                                @show="showTooltip"
                                                trigger="hover">
                                        <div v-html="scope.row[item.name + 'Tooltip'] ? scope.row[item.name + 'Tooltip']: scope.row[item.name]"></div>
                                        <span class="text-ellipse"
                                              slot="reference"
                                              v-html="item.style.isHtml ? scope.row[item.name + 'Html']: scope.row[item.name]"></span>
                                    </el-popover> -->
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </vueSeamlessScroll>
            </vue-scroll>
            <!-- 表内容 -->
        </vue-scroll>
    </div>

</template>

<script>
    import vueSeamlessScroll from 'vue-seamless-scroll'
    export default {
        // 表头、内容、表格配置
        props: ['tableTitles', 'tableDatas', 'option'],
        components: {
            vueSeamlessScroll
        },
        data() {
            return {
                // 表格配置
                // option: {
                //     'selfClass': '',//样式类
                //     'highlight-current-row': true,//高亮
                //     'enClick': true,//行可点击
                //     //排序类
                //     'sort': {
                //         type: 'dynamic', //动态排序（时时走接口,否则当前数据静态排序）
                //         order: 'descending' //只显示降序(否则升序和降序)
                //     },
                // },
                tableHeight: '',
                tableContentHeight: '',
                // 横向滚动条
                opsX: {
                    scrollPanel: {
                        scrollingY: false
                    }
                },
                // 垂直滚动条
                opsY: {
                    scrollPanel: {
                        scrollingX: false,
                    }
                },
                // 滚动自定义
                defaultOption: {
                    step: 1, // 数值越大速度滚动越快
                    limitMoveNum: 20, // 开始无缝滚动的数据量 this.dataList.length
                    hoverStop: true, // 是否开启鼠标悬停stop
                    direction: 1, // 0向下 1向上 2向左 3向右
                    openWatch: true, // 开启数据实时监控刷新dom
                    singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
                    singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
                    waitTime: 100 // 单步运动停止的时间(默认值1000ms)
                }

            }
        },
        methods: {
            rules(value1, value2) {
                let result = 0
                switch (value1 > value2) {
                    case true:
                        result = 1
                        break
                    case false:
                        result = -1
                        break
                    default:
                        result = 0
                        break
                }
                return result
            },
            compare(property, type = 'ascending') {
                return (a, b) => {
                    let value1 = a[property]
                    let value2 = b[property]
                    let sort = 0
                    if (type === 'ascending') {
                        // 升序
                        // sort = value1 - value2 //只有数字
                        sort = this.rules(value1, value2) // 数字、字符串
                    } else {
                        // 降序等
                        // sort = value2 - value1
                        sort = this.rules(value2, value1)
                    }
                    return sort
                }
            },
            // 自定义排序
            sortTable(column) {
                if (this.option && this.option.sort && this.option.sort.type === 'dynamic') {
                    // 动态数据排序(时时走接口,例如top5类的数据)
                    this.$emit('sortTable', column)
                } else {
                    let list = []
                    list = this.tableDatas.sort(this.compare(column.prop, column.order))
                    this.$emit('sortTable', list)
                }

            },
            // 行点击事件
            clickTableRow(row, column, event) {
                this.$emit('clickTableRow', row, column, event)
            },
            // 行class
            tableRowClassName({ row }) {
                if (row.tableRowClassName) {
                    return row.tableRowClassName
                }
                return ''
            },
            init() {
                this.$nextTick(() => {
                    if (!this.$refs.fixTable) {
                        return false
                    }
                    this.tableHeight = this.$refs.fixTable.offsetHeight
                    if (this.option && this.option.selfClass && this.option.selfClass.indexOf('multi-table') > -1) {
                        // 多级表结构
                        this.tableContentHeight = this.tableHeight - 60 * 2 + 'px'
                    } else if (this.option && this.option.selfClass && this.option.selfClass.indexOf('title-line-2') > -1) {
                        // 表头占据2行
                        this.tableContentHeight = this.tableHeight - 80 + 'px'

                    } else {
                        this.tableContentHeight = this.tableHeight - 60 + 'px'
                    }
                })
            }
        },
        created() {

        },
        mounted() {
            this.init()
        },
    }
</script>

<style lang="scss" scoped>
</style>
