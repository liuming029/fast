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
