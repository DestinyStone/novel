import request from './common/Request';

/**
 * 书架接口
 */
export default class Collect {
  // 新增
  static save(data) {
    return request({
      url: '/collect/save',
      method: 'post',
      data: data,
    })
  }
  // 删除
  static delete(novelId) {
    return request({
      url: '/collect/delete',
      method: 'get',
      params: {novelId}
    })
  }
  // 是否存在
  static exist(novelId) {
    return request({
      url: '/collect/exist',
      method: 'get',
      params: {novelId}
    })
  }
  // 返回所有数据
  static page(param, page) {
    return request({
      url: `/collect/page`,
      method: 'get',
      params:{
        ...param,
        ...page,
      }
    })
  }
}