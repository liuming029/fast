import request from '@/utils/request'

//总订单数 用户总数 配送员数 通知公告数
export function selectHomeCount () {
    return request({
        url: '/home/page/selectHomeCount',
        method: 'get',

    })
}