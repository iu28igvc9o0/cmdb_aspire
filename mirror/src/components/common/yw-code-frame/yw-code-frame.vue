<template>
    <!-- 码表：数据框(级联下拉框、输入框等) -->
    <div class="control-wrap">
        <!-- 下拉框 -->
        <el-select v-model="selectValue"
                   :style="activeStyle"
                   :loading="loading"
                   value-key="id"
                   @clear="clear"
                   @change="change"
                   v-if="selectTypes.includes(frameOptions.type)"
                   :disabled="Object.keys(this.frameOptions).includes('disabled') ? frameOptions.disabled : false"
                   :clearable="Object.keys(this.frameOptions).includes('clearable') ? frameOptions.clearable : true"
                   :filterable="Object.keys(this.frameOptions).includes('filterable') ? frameOptions.filterable : true"
                   placeholder="请选择">
            <el-option v-for="item in selectList"
                       :key="item.id"
                       :label="item.name || item.key"
                       :value="item">
            </el-option>
        </el-select>

        <!-- 时间框 -->
        <el-date-picker v-else-if="dateTypes.includes(frameOptions.type)"
                        format="yyyy-MM-dd HH:mm:ss"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        v-model="selectValue"
                        type="datetimerange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        style="width:100%"
                        @change="change">
        </el-date-picker>
        <!-- 输入框 -->
        <el-input v-model="selectValue"
                  v-else
                  :clearable="true"
                  :style="activeStyle"
                  @change="change"
                  placeholder=""></el-input>
    </div>
</template>
<script>
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import rbCodeService from 'src/services/cmdb/rb-cmdb-code-services.factory.js'
    import CommonOption from 'src/utils/commonOption.js'
    export default {
        mixins: [CommonOption],
        props: {
            // key
            filedCode: '',
            // 数据框
            frameDatas: {

            },
            // 数据框配置
            frameOptions: {
                type: Object,
                default: function () {
                    return {
                        disabled: false,// 可置灰
                        clearable: true,// 可清除
                        filterable: true,// 可搜索
                    }
                }
            },
        },
        watch: {
            'frameDatasWatch': {
                handler(val, oldVal) {
                    // 刷新当前被级联的数据
                    if (val.codeObj && val.parentSelect && val.parentSelect.id) {
                        if (val.codeObj.filedCode === oldVal.codeObj.filedCode && val.parentSelect.id !== oldVal.parentSelect.id) {
                            this.init()
                        }
                    }

                },
                immediate: true,
                deep: true // true 深度监听
            }
        },
        data() {
            return {
                // 当前code对象
                codeObj: {},
                // 当前选中值
                selectValue: '',

                // 下拉框数据
                // selectList: [{ 'id': '科技资讯部', 'value': '科技资讯部', 'key': '科技资讯部' }],
                selectList: [],
                // 数据框类型
                selectTypes: ['cascader', 'select', 'listSel'],
                inputTypes: ['ip', 'singleRowText', 'int', 'input'],
                dateTypes: ['dateTime']
            }
        },
        computed: {
            // 样式设置
            activeStyle() {
                let style = {}
                if (this.frameOptions.style) {
                    for (let key in this.frameOptions.style) {
                        style[key] = this.frameOptions.style[key]
                    }
                }
                return style
            },
            frameDatasWatch() {
                return JSON.parse(JSON.stringify(this.frameDatas))
            }
        },
        methods: {
            // 查询当前code对象
            getCodeBy(code) {
                let params = {
                    codeId: code.codeId,
                    filedCode: code.filedCode,
                    moduleCatalogId: this.queryModuleCatalog()
                }
                return rbCodeService.getCodeObj(params).then((res) => {
                    return res
                })
            },
            // 数据字典
            getDictByType(item = {}) {
                this.showLoading()
                let params = {
                    type: item.codeBindSource.dictSource
                }
                return rbCmdbServiceFactory.getDictsByType(params).then((res) => {
                    return res
                }).finally(() => {
                    this.closeLoading()
                })
            },
            // 数据表
            getDictBySql(sql = '') {
                this.showLoading()
                let params = {
                    sql: sql
                }
                return rbCmdbServiceFactory.getDictsBySql(params).then((res) => {
                    return res
                }).finally(() => {
                    this.closeLoading()
                })
            },
            // 引用模型
            getRefModuleDict(item) {
                this.showLoading()
                let params = {
                    codeId: item.codeId
                }

                return rbCmdbModuleService.getRefModuleDict({ params: params }).then((res) => {
                    return res
                }).finally(() => {
                    this.closeLoading()
                })
            },
            // 获得下拉框数据
            async getSelectList(item) {
                // 来自父级级联
                if (this.frameDatas.parentCode) {
                    let currentCascader = ''
                    this.frameDatas.parentCode.cascadeList.some((cascadeItem) => {
                        if (cascadeItem.subFiledCode === this.codeObj.filedCode) {
                            currentCascader = cascadeItem
                            return true
                        } else {
                            return false
                        }
                    })

                    let sqlString = currentCascader.sqlString.replace(/\?/g, this.frameDatas.parentSelect.id)
                    this.selectList = await this.getDictBySql(sqlString)
                    return true
                }

                // 来自本身配置数据
                if (item.codeBindSource) {
                    switch (item.codeBindSource.bindSourceType) {
                        case '数据字典':
                            this.selectList = await this.getDictByType(item)
                            break
                        case '数据表':
                            this.selectList = await this.getDictBySql(item.codeBindSource.tableSql)
                            break
                        case '引用模型':
                            this.selectList = await this.getRefModuleDict(item)
                            break
                    }
                }


                // 默认值重新赋值
                if (typeof this.selectValue !== 'object') {
                    let selectItem = {}
                    this.selectList.some((item) => {
                        if (item.id === this.selectValue || item.name === this.selectValue || item.key === this.selectValue) {
                            selectItem = item
                            return true
                        } else {
                            return false
                        }
                    })

                    this.selectValue = selectItem
                }
            },
            // 初始化
            async init() {
                // 默认值赋值
                this.selectValue = this.frameDatas.select

                // 查询当前code对象
                if (this.frameOptions) {

                    if (Object.keys(this.frameDatas.codeObj).length > 2) {
                        // 下拉框来源：通过数据配置获得
                        this.codeObj = this.frameDatas.codeObj
                    } else {
                        // 下拉框来源：非配置来源(数据源只有codeId和filedCode,其他数据得通过接口获得)
                        this.codeObj = await this.getCodeBy(this.frameDatas.codeObj)
                        this.frameOptions.type = this.codeObj.controlType.controlCode
                    }

                    // 查询下拉框
                    if (this.selectTypes.indexOf(this.codeObj.controlType.controlCode) > -1) {
                        this.getSelectList(this.codeObj)
                    }

                }
            },
            // 选择
            change(select) {
                // 入参数据、入参配置、选中值、当前code对象
                this.$emit('changeSelect', this.frameDatas, this.frameOptions, select, this.codeObj)
            },
            // 清空
            clear() {
                // 自定义清空使用(默认会走change事件)
                this.$emit('clear', this.frameDatas, this.frameOptions, '', this.codeObj)
            },
            // 获取模型分组
            queryModuleCatalog() {
                // 解析moduleCatalogId, 如果传入moduleCatalogId参数则使用传入的值进行查询. 否则使用默认逻辑
                let catalogId = this.frameDatas.codeObj.moduleCatalogId
                if (!this.frameDatas.codeObj.moduleCatalogId) {
                    catalogId = 'cmdb_instance' // 默认主机模型分组
                }
                const moduleCatalog = {
                    // 主机资源模型分组ID
                    cmdb_instance: '96603733-5793-11ea-ab96-fa163e982f89',
                    // 数据字典模型分配ID
                    cmdb_dict: '2aa2924a75fd4255a01dc77b255f1b04',
                    // 业务模型
                    cmdb_business: '54ba8b02ad3748229adb39f9e7fdff38',
                    // IP地址库
                    cmdb_ip_repository: 'e7923392ba7e4822a6266b6ee616ef9c'
                }
                return moduleCatalog[catalogId]
            }
        },
        // updated() {
        //     // 数据变更时
        //     
        // },
        mounted() {
            this.init()
        },

    }
</script>
<style lang="scss" scoped>
</style>
