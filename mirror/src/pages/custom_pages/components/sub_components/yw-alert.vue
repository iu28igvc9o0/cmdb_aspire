<template>
    <!-- 边框告警总数组件 -->
    <div class="yw-alert-wrap">
        <section class="yw-alert"
                 :style="activeStyle"
                 v-for="(item,index) in datas"
                 :key="index">
            <p class="fast-img"><img :src="item.icon"
                     alt=""></p>
            <p class="fast-name text-ellipse">{{item.name}}</p>
            <p class="fast-num text-ellipse">{{item.num + ''}}</p>
        </section>
    </div>
</template>

<script>
    export default {
        props: ['datas', 'alertOption'],
        data() {
            return {
                // datas: [
                //   {
                //     name: '待处理',
                //     icon: '',
                //     num: 150,
                //     time: 20
                //   }
                // ],

                // 默认参数
                defaultOption: {
                    color: '#FF1C5C',
                    backgroundImage: 'url(' + require('src/assets/theme/dark/img/alert-border-bg.png') + ')',
                }
            }
        },
        computed: {
            activeStyle() {
                let style = this.defaultOption
                if (this.alertOption) {
                    style = {
                        color: this.alertOption.color ? this.alertOption.color : this.defaultOption.color,
                        backgroundImage: this.alertOption.backgroundImage ? this.alertOption.backgroundImage : this.defaultOption.backgroundImage,
                    }
                }

                return style
            }
        },
        methods: {

        },

    }
</script>
<style lang="scss" scoped>
    .yw-alert-wrap {
        display: flex;
        justify-content: center;
        align-content: center;
        width: 100%;
        height: 100%;
        flex-wrap: wrap;
        .yw-alert {
            display: flex;
            justify-content: space-around;
            color: $color-base;
            width: rem(372);
            height: rem(84);
            line-height: rem(84);
            background-repeat: no-repeat;
            background-size: cover;
            margin: 0 10px rem(24);
            &:first-child {
                margin-top: 0;
            }
            .fast-img {
                display: inline-block;
                vertical-align: middle;
                width: 30%;
                text-align: center;
                img {
                    display: inline-block;
                    vertical-align: middle;
                    width: rem(40);
                    height: auto;
                }
            }
            .fast-name {
                width: 30%;
                font-size: $font-18;
                color: #fff;
                text-align: center;
            }
            .fast-num {
                width: 40%;
                font-size: $font-30;
                color: inherit;
                text-align: left;
                padding-left: 20px;
            }
        }
    }
</style>
