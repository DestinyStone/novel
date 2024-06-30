import request from './common/Request';

/**
 * 小说内容接口
 */
export default class NovelContent {
  // 新增
  static save(data) {
    return request({
      url: '/novel/content/save',
      method: 'post',
      data: data,
    })
  }
  // 删除
  static delete(id) {
    return request({
      url: '/novel/content/delete',
      method: 'get',
      params: {id}
    })
  }
  // 更新
  static update(id, data) {
    return request({
      url: `/novel/content/update/${id}`,
      method: 'post',
      data: data,
    })
  }
  // 根据id查询数据
  static getById(id) {
    return request({
      url: `/novel/content/id`,
      method: 'get',
      params: {id}
    })
  }
  // 返回所有数据
  static page(param, page) {
    return request({
      url: `/novel/content/page`,
      method: 'get',
      params:{
        ...param,
        ...page,
      }
    })
  }
  // 返回所有数据
  static list(param) {
    return request({
      url: `/novel/content/list`,
      method: 'get',
      params:{
        ...param,
      }
    })
  }
}
