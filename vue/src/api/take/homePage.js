import request from '@/utils/request'

//总订单数 用户总数 配送员数 通知公告数
export function selectHomeCount () {
    return request({
        url: '/home/page/selectHomeCount',
        method: 'get',

    })
}
// 查询订单趋势统计
export function selectOrderTrend () {
    return request({
        url: '/home/page/selectOrderTrend',
        method: 'get',

    })
}
// 查询订单状态分布统计
export function selectOrderStatusChart () {
    return request({
        url: '/home/page/selectOrderStatusChart',
        method: 'get',

    })
}