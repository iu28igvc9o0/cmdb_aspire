import Vuex from 'vuex'
import Vue from 'vue'
import homeStore from './potral/home-store'
import customization from './modules/customization'
import model from './modules/model'
import autoOperation from './modules/auto-operation'
import bpm from './modules/bpm'

Vue.use(Vuex)

export default new Vuex.Store({
    // 状态数据按模块分类引入
    modules: {
        homeStore,
        customization,
        model,
        autoOperation,
        bpm
    }
})
