import request from '@/utils/request'

// 查询包裹规格列表
export function listSize(query) {
    return request({
        url: '/take/size/list',
        method: 'get',
        params: query
    })
}

// 查询包裹规格详细
export function getSize(sizeId) {
    return request({
        url: '/take/size/' + sizeId,
        method: 'get'
    })
}

// 新增包裹规格
export function addSize(data) {
    return request({
        url: '/take/size',
        method: 'post',
        data: data
    })
}

// 修改包裹规格
export function updateSize(data) {
    return request({
        url: '/take/size',
        method: 'put',
        data: data
    })
}

// 删除包裹规格
export function delSize(sizeId) {
    return request({
        url: '/take/size/' + sizeId,
        method: 'delete'
    })
}
