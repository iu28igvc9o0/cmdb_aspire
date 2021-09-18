<template>
    <div class="userSolve">
        <Commonlist :userType="userType" />
    </div>
</template>
<script>
    import Commonlist from '../commonList'
    export default {
        name: 'UserSolve',
        data() {
            return {
                userType: false
            }
        },
        components: {
            Commonlist
        }
    }
</script>
<style lang="scss" scoped>
    .userSolve {
        height: 100%;
    }
</style>