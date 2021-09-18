// 自动化模块数据
export default {
    state: {
        scanCycleList: [], // 扫描周期列表
        riskLevelList: [], // 漏洞等级列表
        department1List: [], // 一级部门列表
        bizSystemList: [], // 业务系统列表
        groupList: [], // 分组列表
    },
    mutations: {
        setScanCycleList(state, data) {
            state.scanCycleList = data
        },
        setRiskLevelList(state, data) {
            state.riskLevelList = data
        },
        setDepartment1List(state, data) {
            state.department1List = data
        },
        setBizSystemList(state, data) {
            state.bizSystemList = data
        },
        setGroupList(state, data) {
            state.groupList = data
        },
    }
}
