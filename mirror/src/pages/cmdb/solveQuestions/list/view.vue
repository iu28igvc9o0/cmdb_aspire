<template>
    <div class="adminSolve">
        <Commonlist :userType="userType" />
    </div>
</template>
<script>
    import Commonlist from '../commonList'
    export default {
        name: 'AdminSolve',
        data() {
            return {
                userType: true
            }
        },
        components: {
            Commonlist
        }
    }
</script>
<style lang="scss" scoped>
    .adminSolve {
        height: 100%;
    }
</style>