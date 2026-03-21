import request from '@/utils/request'

// 查询宿舍楼列表
export function listBuilding(query) {
    return request({
        url: '/take/building/list',
        method: 'get',
        params: query
    })
}

// 查询宿舍楼详细
export function getBuilding(buildingId) {
    return request({
        url: '/take/building/' + buildingId,
        method: 'get'
    })
}

// 新增宿舍楼
export function addBuilding(data) {
    return request({
        url: '/take/building',
        method: 'post',
        data: data
    })
}

// 修改宿舍楼
export function updateBuilding(data) {
    return request({
        url: '/take/building',
        method: 'put',
        data: data
    })
}

// 删除宿舍楼
export function delBuilding(buildingId) {
    return request({
        url: '/take/building/' + buildingId,
        method: 'delete'
    })
}
