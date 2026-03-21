// 快递站点 API 接口
import request from '@/utils/request' // 确保这个导入也存在

// 获取快递站点列表
export function listStation(query) {
    return request({
        url: '/take/station/list',
        method: 'get',
        params: query
    })
}

// 根据ID获取快递站点详情
export function getStation(stationId) {
    return request({
        url: `/take/station/${stationId}`,
        method: 'get'
    })
}

// 新增快递站点
export function addStation(data) {
    return request({
        url: '/take/station',
        method: 'post',
        data: data
    })
}

// 修改快递站点（修复：先闭合这个函数）
export function updateStation(data) {
    return request({
        url: '/take/station',
        method: 'put',
        data: data
    })
} // ✅ 关键：把 updateStation 的闭合括号移到这里

// 删除快递站点（修复：移到 updateStation 外部）
export function deleteStation(stationId) {
    return request({
        url: '/take/station/' + stationId,
        method: 'delete'
    })
}