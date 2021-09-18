import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
export default {
    data() {
        return {
            sysTypeOptions: [{
                label: 'ZABBIX',
                value: 'ZABBIX'
            }, {
                label: '普罗米修斯',
                value: 'PROMETHEUS'
            }, {
                label: '主题',
                value: 'THEME'
            }, {
                label: '主题计算',
                value: 'THEME_CALC'
            }, {
                label: '自动化脚本',
                value: 'SCRIPT'
            }],
            triggerOptions: {
                trigger_level: [
                    // {
                    //   label: '提示',
                    //   value: '1'
                    // },
                    {
                        label: '低',
                        value: '2'
                    }, {
                        label: '中',
                        value: '3'
                    }, {
                        label: '高',
                        value: '4'
                    }, {
                        label: '重大',
                        value: '5'
                    }
                ],
                expressionList: [
                    {
                        value: '=',
                        label: '='
                    }, {
                        value: '<',
                        label: '<'
                    }, {
                        value: '>',
                        label: '>'
                    }, {
                        value: '!=',
                        label: '!='
                    }, {
                        value: 'In',
                        label: 'In'
                    }
                ]
            },
            dimOptions: {
                fieldComparor: [{
                    value: '0',
                    label: '>'
                }, {
                    value: '1',
                    label: '<'
                }, {
                    value: '2',
                    label: '='
                }, {
                    value: '3',
                    label: '包含'
                }],
                DimDateOption: [{
                    value: '0',
                    label: '>'
                }, {
                    value: '1',
                    label: '<'
                }, {
                    value: '2',
                    label: '='
                }],
                dateTypeOpiton: [{
                    value: '0',
                    label: '指标计算当前时间'
                }, {
                    value: '1',
                    label: '指标计算当日开始'
                }, {
                    value: '2',
                    label: '指标计算当日结束'
                }],
                numTypeOption: [{
                    value: '0',
                    label: '+'
                }, {
                    value: '1',
                    label: '-'
                }],
                timeOptions: [{
                    label: '分',
                    value: '2'
                }, {
                    label: '小时',
                    value: '1'
                }, {
                    label: '天',
                    value: '0'
                }],
                timeOptions1: [{
                    label: '分',
                    value: '1'
                }, {
                    label: '小时',
                    value: '2'
                }, {
                    label: '天',
                    value: '3'
                }]
            },
            monTypeOptions: [{
                label: '系统',
                value: '1'
            }, {
                label: '业务',
                value: '2'
            }],
            funTypeOptions: [{
                label: '监控指标项',
                value: '1'
            }, {
                label: '巡检指标项',
                value: '2'
            }],
            typeOptions: ['硬件', '网络', '主机操作系统', '应用'],
            // 机房位置下拉框选项
            room: [],
            // 业务系统列表
            bizSysList: [],
            // 设备类型的下拉框选项
            equipType: [],
        }
    },
    methods: {
        getConfigDictByType() {
            rbProjectDataServiceFactory.getConfigDictByType({ 'type': 'roomId', 'pid': '' }).then((res) => {
                if (res) {
                    this.room = res
                }
            })
        },
        getBusinessSystem() {
            rbProjectDataServiceFactory.getBusinessSystem({ moduleType: 'default_business_system_module_id' }).then((res) => {
                if (res) {
                    this.bizSysList = res
                    this.bizSysList.forEach(item => {
                        item.value = item.id
                        item.name = item.bizSystem
                    })
                }
            })
        },
        getModuleTree() {
            rbProjectDataServiceFactory.getModuleTree().then((res) => {
                res.forEach((obj) => {
                    let arr = []
                    obj.item.forEach((item) => {
                        let obj2 = {
                            value: item.id,
                            label: item.name
                        }
                        arr.push(obj2)
                    })
                    let obj1 = {
                        value: obj.id,
                        label: obj.name,
                        children: arr
                    }
                    this.equipType.push(obj1)
                })
            })
        },
    },
    mounted() {
        this.getConfigDictByType()
        this.getBusinessSystem()
        this.getModuleTree()
    }
}