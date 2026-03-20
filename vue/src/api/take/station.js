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
