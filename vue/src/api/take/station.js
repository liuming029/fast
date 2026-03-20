import request from '@/utils/request'
// 查询快递站点列表
export function listStation(query) {
    return request({
        url: '/take/station/list',
        method: 'get',
        params: query
    })
}