<template>
    <div v-loading="loading" :element-loading-text="loading_text">
        <OverviewTable :params="params" @setLoadingMethod="loadingMethod"></OverviewTable>
    </div>
</template>

<script>
    import { xMonth } from 'src/assets/js/utility/rb-filters.js'
    export default {
        components: {
            OverviewTable: () => import('../../overviewTable.vue'),
        },
        data() {
            return {
                loading: false,
                loading_text: '加载中',
                params: {
                    type: 'overview',
                    resourceOwner: 'resource',
                    month: ''
                }
            }
        },
        computed: {
        },

        methods: {
            // 加载
            loadingMethod(load){
                this.loading = load.loading
                this.loading_text = load.loading_text
            }
        },

        mounted() {
            this.params.month = xMonth(-1, '-')
        },
    }
</script>

<style lang="scss" scoped>
</style>