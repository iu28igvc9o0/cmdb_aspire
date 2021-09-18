<template>
    <!-- 告警所有状态总数组件 -->
    <div class="yw-alert-status-wrap">
        <section class="yw-alert"
                 v-for="(item,index) in datas"
                 :style="{height:options && options.itemNumber? '25%':'33.3%'}"
                 :key="index">
            <div class="fast-order">
                <p class="fast-img"><img :src="item.icon"
                         alt=""></p>
                <div class="fast-content">
                    <p class="fast-name text-ellipse">{{item.name}}</p>
                    <p class="fast-num text-ellipse"><a href="#"
                           @click="goto(item.status, '')">{{item.num + ''}}</a></p>
                </div>
            </div>
            <div class="fast-details">
                <a class="
           fast-details-item"
                   v-for="(subItem,subIndex) in item.numList"
                   :key="subIndex"
                   @click="goto(item.status, subIndex)">
                    <span class="fast-status"
                          :class=" `bg-${subItem.status}`"></span>
                    <span class="fast-status-num"
                          :class=" `color-${subItem.status}`">{{subItem.num}}</span>
                </a>
            </div>
        </section>
    </div>
</template>

<script>
    export default {
        props: ['datas', 'options'],
        data() {
            return {
                // datas: [
                //  {          name: '已解除',
                //       icon: require('src/assets/theme/dark/img/alert-finish-release.png'),
                //       num: 0,
                //       numList: [
                //         { status: 'red', num: '0' },
                //         { status: 'orange', num: '0' },
                //         { status: 'yellow', num: '0' },
                //         { status: 'blue', num: '0' },
                //       ]
                //     }
                // ],
            }
        },
        methods: {

            goto(status, level) {
                if (this.options && !this.options.link) {
                    return false
                }
                let path = '/mirror_alert/alert/manage'
                if (status === 3 || status === '3' || status === 4 || status === '4') {
                    path = '/mirror_alert/alert_his/his_alert'
                    status = parseInt(status)
                }
                this.$router.push({
                    path: path,
                    query: {
                        alertType: 'main',
                        operation_status: status,
                        alert_level: level === '' ? '' : 5 - level
                    }
                })
            }
        },

    }
</script>
<style lang="scss" scoped>
    .yw-alert-status-wrap {
        width: 100%;
        height: 100%;
        padding: 0 2px;
        .yw-alert {
            display: flex;
            justify-content: center;
            height: 24%;
            padding: 15px 0;
            color: $color-base;
            &:nth-child(even) {
                background: $color-blue;
            }
            .fast-order {
                display: flex;
                align-content: center;
                align-items: center;
                justify-content: center;
                width: 26%;
                text-align: center;
            }
            .fast-img {
                display: inline-block;
                vertical-align: middle;
                width: rem(24);
                height: auto;
                margin-right: 5px;
                img {
                    width: 100%;
                    height: 100%;
                }
            }
            .fast-content {
                display: inline-block;
                vertical-align: middle;
                width: 50%;
                text-align: left;
            }
            .fast-details {
                width: 74%;
                display: flex;
                justify-content: space-around;
                align-items: center;
                flex-wrap: wrap;
                padding-left: 5px;
                border-left: 1px solid $color-tip-blue;
                .fast-details-item {
                    display: inline-block;
                    width: 20%;
                }
            }
            .fast-status {
                display: inline-block;
                vertical-align: middle;
                width: 8px;
                height: 8px;
                border-radius: 100%;
            }
            .fast-status-num {
                width: 70%;
                font-size: $font-14;
            }
            .fast-name {
                font-size: $font-14;
            }
            .fast-num {
                font-size: $font-16;
            }
        }
        &.status-all-wrap {
            .yw-alert {
                height: 33.3%;
                .fast-order {
                    width: 40%;
                    padding-left: 10px;
                }
                .fast-details {
                    width: 60%;
                    padding-left: 25px;
                    .fast-details-item {
                        width: 50%;
                    }
                }
                .fast-img {
                    width: rem(60);
                    height: auto;
                }
                .fast-name {
                    font-size: $font-16;
                }
            }
        }
    }
</style>
