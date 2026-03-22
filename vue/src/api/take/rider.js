import request from '@/utils/request'

// 查询配送员列表
export function listRider(query) {
    return request({
        url: '/take/rider/list',
        method: 'get',
        params: query
    })
}

// 查询配送员详细
export function getRider(riderId) {
    return request({
        url: '/take/rider/' + riderId,
        method: 'get'
    })
}

// 新增配送员
export function addRider(data) {
    return request({
        url: '/take/rider',
        method: 'post',
        data: data
    })
}

// 修改配送员
export function updateRider(data) {
    return request({
        url: '/take/rider',
        method: 'put',
        data: data
    })
}

// 删除配送员
export function delRider(riderId) {
    return request({
        url: '/take/rider/' + riderId,
        method: 'delete'
    })
}
// 查询用户有没有提交过审核，如果有返回认证状态，没有返回无
export function selectIsAuthToStatus() {
    return request({
        url: '/take/rider/selectIsAuthStatus' ,
        method: 'get'
    })
}

// 当用户重新提交认证后, 将之前的认证信息删除
export function deleteOldAuth() {
    return request({
        url: '/take/rider/deleteOldAuth',
        method: 'delete'
    })
}