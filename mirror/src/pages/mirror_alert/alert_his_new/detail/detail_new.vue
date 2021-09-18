<template>
    <div>
        <rb-mirror-alert-his-detail :detailType="this.$route.query.detailType"
                                    :modeNameList="this.$route.query.modeNameList"
                                    :alertId="this.$route.query.alertId"></rb-mirror-alert-his-detail>
    </div>
</template>
<script>
    import rbMirrorAlertHisDetail from 'src/pages/mirror_alert/common/rb-mirror-alert-his-detail_new.vue'
    export default {
        components: {
            rbMirrorAlertHisDetail
        },
        props: [
        ],
        data () {
            return {
            }
        },
        mounted: function () {
        },
        methods: {
        }
    }
</script>
<style scoped lang="scss">
</style>
