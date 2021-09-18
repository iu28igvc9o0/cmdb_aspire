<template>
    <div>
        <OverviewTable :params="params"></OverviewTable>
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
                params: {
                    type: 'overview',
                    resourceOwner: 'yunwei',
                    month: xMonth(-1, '-')
                }
            }
        },
        computed: {
        },

        methods: {
        },

        mounted() {
        },
    }
</script>

<style lang="scss" scoped>
</style>