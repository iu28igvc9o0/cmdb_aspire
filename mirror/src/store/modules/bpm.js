// bpm数据
export default {
    state: {
        curRouterHash: '', // bpm跳转url
    },
    mutations: {
        setCurRouterHash(state, data) {
            state.curRouterHash = data
        },
    }
}
