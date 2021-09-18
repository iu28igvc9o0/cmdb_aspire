<template>
    <!-- 表格(常用) -->
    <div class="yw-el-table-wrap">
        <el-table :data="tableDatas"
                  class="yw-el-table"
                  :max-height="(option && option['max-height']) ? option['max-height'] : 'auto'"
                  stripe
                  border
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
                <template slot="header">
                    <span v-html="item.titleHtml ? item.titleHtml : item.title"
                          style="outline:none;"></span>
                </template>
                <template slot-scope="scope">
                    <div>
                        <!-- <el-tooltip effect="dark"
                                    popper-class="tooltip-width-250"
                                    placement="top-start">
                            <div slot="content"
                                 v-html="scope.row[item.name + 'Tooltip'] ? scope.row[item.name + 'Tooltip']: scope.row[item.name]"></div>
                            <span class="text-ellipse"
                                  v-html="item.style.isHtml ? scope.row[item.name + 'Html']: scope.row[item.name]"></span>
                        </el-tooltip> -->
                        <span class="text-ellipse"
                              :title="scope.row[item.name + 'Tooltip'] ? scope.row[item.name + 'Tooltip']: scope.row[item.name]"
                              v-html="item.style.isHtml ? scope.row[item.name + 'Html']: scope.row[item.name]"></span>
                    </div>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
    export default {
        // 表头、内容、表格配置
        props: ['tableTitles', 'tableDatas', 'option'],
        components: {

        },
        data() {
            return {

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

        },
        created() {

        },
        mounted() {

        },
    }
</script>

<style lang="scss" scoped>
</style>
