import request from './common/Request';

/**
 * 分类接口
 */
export default class Tag {
  // 新增
  static save(data) {
    return request({
      url: '/tag/save',
      method: 'post',
      data: data,
    })
  }
  // 删除
  static delete(id) {
    return request({
      url: '/tag/delete',
      method: 'get',
      params: {id}
    })
  }
  // 更新
  static update(id, data) {
    return request({
      url: `/tag/update/${id}`,
      method: 'post',
      data: data,
    })
  }
  // 根据id查询数据
  static getById(id) {
    return request({
      url: `/tag/id`,
      method: 'get',
      params: {id}
    })
  }
  // 返回所有数据
  static list(param) {
    return request({
      url: `/tag/list`,
      method: 'get',
      params:{
        ...param,
      }
    })
  }
}
