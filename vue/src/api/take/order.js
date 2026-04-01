import request from '@/utils/request'

// 查询订单列表
export function listOrder(query) {
    return request({
        url: '/take/order/list',
        method: 'get',
        params: query
    })
}

// 查询订单详细
export function getOrder(orderId) {
    return request({
        url: '/take/order/' + orderId,
        method: 'get'
    })
}

// 新增订单
export function addOrder(data) {
    return request({
        url: '/take/order',
        method: 'post',
        data: data
    })
}

// 修改订单
export function updateOrder(data) {
    return request({
        url: '/take/order',
        method: 'put',
        data: data
    })
}

// 删除订单
export function delOrder(orderId) {
    return request({
        url: '/take/order/' + orderId,
        method: 'delete'
    })
}
// 查询用户个人的订单列表
export function selectMyOrderList(query) {
    return request({
        url: '/take/order/selectMyOrderList',
        method: 'get',
        params:query
    })
}
// 取消订单
export function cancelOrder (orderId) {
    return request({
        url: '/take/order/cancelOrder/'+orderId,
        method: 'put',

    })
}

// 接单
export function accept (orderId) {
    return request({
        url: '/take/order/accept/'+orderId,
        method: 'put',
    })
}
// 查询配送员个人的接单记录列表
export function selectOrderListByRiderToUserId(query) {
    return request({
        url: '/take/order/selectOrderListByRiderToUserId',
        method: 'get',
        params:query
    })
}