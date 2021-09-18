<template>
    <!-- 自定义穿梭框（多级分类） -->
    <div class="yw-transfer">
        <!-- 待选项 -->
        <section class="select-box no-select">
            <div class="header-wrap clearfix">
                <span class="num fr">{{total.noSelectActive}}/{{total.noSelectTotal}}</span>
                {{activeOptions.titles[0]}}
            </div>
            <div class="main-wrap">
                <YwSearch :searchParams="searchParams"
                          style="width:180px;margin: 20px 0 20px 8px;"
                          @changeSearch="changeSearch"></YwSearch>
                <el-checkbox-group v-model="noSelectDatas">
                    <div class="sort-item"
                         v-for="(item,index) in datas"
                         :key="index">
                        <p class="sort-title">{{item[options.fieldMatch.title]}}</p>
                        <!-- <p class="sort-title"
               v-if="item.list && item.list.length>0">{{item.title}}</p> -->
                        <p v-for="(subItem,subIndex) in item[options.fieldMatch.list]"
                           :key="subIndex">
                            <el-checkbox :label="subItem"
                                         v-if="subItem[options.fieldMatch.name].indexOf(searchParams.keyword.trim())>-1 && subItem.codeSetting[options.fieldMatch.display]">{{subItem[options.fieldMatch.name]}}</el-checkbox>
                        </p>

                    </div>
                </el-checkbox-group>
            </div>
        </section>
        <!-- 待选项 -->
        <!-- 按钮 -->
        <section class="select-button">
            <el-button type="primary"
                       icon="el-icon-arrow-left"
                       @click="selectTransfer({type:'noSelect',data:selectDatas})"
                       :disabled="total.selectActive<1"></el-button>
            <el-button type="primary"
                       icon="el-icon-arrow-right"
                       @click="selectTransfer({type:'select',data:noSelectDatas})"
                       :disabled="total.noSelectActive<1"></el-button>
        </section>
        <!-- 按钮 -->
        <!-- 已选项 -->
        <section class="select-box has-select">
            <div class="header-wrap clearfix">
                <span class="num fr">{{total.selectActive}}/{{total.selectTotal}}</span>
                {{activeOptions.titles[1]}}
            </div>
            <div class="main-wrap">
                <YwSearch :searchParams="searchParams"
                          style="width:180px;margin: 20px 0 20px 8px;"
                          @changeSearch="changeSearch"></YwSearch>
                <el-checkbox-group v-model="selectDatas">
                    <div class="sort-item"
                         v-for="(item,index) in datas"
                         :key="index">
                        <p class="sort-title">{{item[options.fieldMatch.title]}}</p>
                        <!-- <p class="sort-title"
               v-if="item.list && item.list.length>0">{{item.title}}</p> -->
                        <p v-for="(subItem,subIndex) in item[options.fieldMatch.list]"
                           :key="subIndex">
                            <el-checkbox :label="subItem"
                                         v-if="subItem[options.fieldMatch.name].indexOf(searchParams.keyword.trim())>-1 && !subItem.codeSetting[options.fieldMatch.display]">{{subItem[options.fieldMatch.name]}}</el-checkbox>
                        </p>

                    </div>
                </el-checkbox-group>
            </div>
        </section>
        <!-- 已选项 -->
    </div>
</template>

<script>

    export default {
        props: ['datas', 'options'],
        components: {
            YwSearch: () => import('src/components/common/yw-search.vue'),
        },
        data() {
            return {
                // 待选值
                noSelectDatas: [],
                // 选中值
                selectDatas: [],
                // 配置项
                defaultOptions: {
                    // 标题
                    titles: ['列表1', '列表2']
                },
                // 搜索框参数
                searchParams: {
                    keyword: '',
                    desc: {
                        placeholder: '请输入搜索内容',
                        // bgcolor: '',
                        borderRadius: '20px',
                        // borderColor: '',
                        // color: '#88AAB4',
                        // iconColor: '#BCC0C6'
                    },
                    event: {// 事件
                        input: true
                    }
                },
            }
        },
        computed: {
            // 配置项
            activeOptions() {
                let temp = this.defaultOptions
                temp.titles = this.options && this.options.titles ? this.options.titles : temp.titles
                return temp
            },
            // 字段总数
            total() {
                let that = this
                let total = {
                    noSelectTotal: 0,// 待选列表总数
                    selectTotal: 0,// 已选列表总数
                    noSelectActive: 0,// 待选列表选中个数
                    selectActive: 0,// 已选列表选中个数
                }

                let noSelectActive = this.noSelectDatas.map((item) => { return item[this.options.fieldMatch.name] })
                let selectActive = this.selectDatas.map((item) => { return item[this.options.fieldMatch.name] })

                this.datas.forEach((item) => {

                    item[that.options.fieldMatch.list].forEach((item2) => {
                        if (item2.codeSetting[that.options.fieldMatch.display]) {
                            total.noSelectTotal = total.noSelectTotal + 1
                            if (noSelectActive.indexOf(item2[that.options.fieldMatch.name]) > -1) {
                                total.noSelectActive = total.noSelectActive + 1
                            }
                        } else {
                            total.selectTotal = total.selectTotal + 1
                            if (selectActive.indexOf(item2[that.options.fieldMatch.name]) > -1) {
                                total.selectActive = total.selectActive + 1
                            }
                        }
                    })
                })
                return total
            }
        },
        methods: {
            // 搜索框查询
            changeSearch(val) {
                this.searchParams.keyword = val
            },

            // 搜索
            filterMethod(query, item) {
                return item[this.activeOptions.props.label].indexOf(query) > -1
            },

            // 选择
            selectTransfer(val) {

                switch (val.type) {
                    // 选择
                    case 'select':
                        val.data.forEach((item) => {
                            this.$set(item.codeSetting, this.options.fieldMatch.display, 0)
                        })
                        break
                    // 取消选择
                    case 'noSelect':
                        val.data.forEach((item) => {
                            this.$set(item.codeSetting, this.options.fieldMatch.display, 1)
                        })
                        break
                }

                this.$emit('selectTransfer', this.datas)

            },
        },
        mounted() {

        }

    }
</script>
<style lang="scss" scoped>
    .yw-transfer {
        height: 100%;
        .select-box {
            position: relative;
            display: inline-block;
            vertical-align: middle;
            width: 250px;
            height: 100%;
            border: 1px solid #ebeef5;
            border-radius: 4px;
            overflow: auto;
            background: #fff;
            .header-wrap {
                height: 40px;
                line-height: 40px;
                background: #f5f7fa;
                margin: 0;
                padding-left: 15px;
                border-bottom: 1px solid #ebeef5;
                box-sizing: border-box;
                color: #000;
                .num {
                    margin-right: 15px;
                    color: #909399;
                }
            }
            .main-wrap {
                height: calc(100% - 40px);
                .el-checkbox-group {
                    font-size: unset;
                    height: calc(100% - 70px);
                    overflow: auto;
                    .el-checkbox {
                        display: block !important;
                        height: 30px;
                        line-height: 30px;
                        padding-left: 15px;
                    }
                }
                .sort-item {
                    padding: 0 15px;
                    white-space: nowrap;
                }
            }
        }
        .select-button {
            display: inline-block;
            vertical-align: middle;
            padding: 0 30px;
        }
    }
</style>
