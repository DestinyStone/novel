import request from './common/Request';

/**
 * 小说接口
 */
export default class Novel {
  // 新增
  static save(data) {
    return request({
      url: '/novel/save',
      method: 'post',
      data: data,
    })
  }
  // 删除
  static delete(id) {
    return request({
      url: '/novel/delete',
      method: 'get',
      params: {id}
    })
  }
  // 更新
  static update(id, data) {
    return request({
      url: `/novel/update/${id}`,
      method: 'post',
      data: data,
    })
  }
  // 根据id查询数据
  static getById(id) {
    return request({
      url: `/novel/id`,
      method: 'get',
      params: {id}
    })
  }
  // 返回所有数据
  static page(param, page) {
    return request({
      url: `/novel/page`,
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
      url: `/novel/list`,
      method: 'get',
      params:{
        ...param,
      }
    })
  }

  static fireList(param) {
    return request({
      url: `/novel/list/fire`,
      method: 'get',
      params:{
        ...param,
      }
    })
  }

  static newsList(param) {
    return request({
      url: `/novel/list/news`,
      method: 'get',
      params:{
        ...param,
      }
    })
  }

  static collectList(param) {
    return request({
      url: `/novel/list/collect`,
      method: 'get',
      params:{
        ...param,
      }
    })
  }

  static scoreList(param) {
    return request({
      url: `/novel/list/score`,
      method: 'get',
      params:{
        ...param,
      }
    })
  }

  static standardList(param) {
    return request({
      url: `/novel/list/standard`,
      method: 'get',
      params:{
        ...param,
      }
    })
  }

  static view(id) {
    return request({
      url: `/novel/view`,
      method: 'get',
      params: {id}
    })
  }
}
