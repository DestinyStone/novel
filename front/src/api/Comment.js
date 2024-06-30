import request from './common/Request';

/**
 * 评论接口
 */
export default class Comment {
  // 新增
  static save(data) {
    return request({
      url: '/comment/save',
      method: 'post',
      data: data,
    })
  }
  // 删除
  static delete(id) {
    return request({
      url: '/comment/delete',
      method: 'get',
      params: {id}
    })
  }
  // 更新
  static update(id, data) {
    return request({
      url: `/comment/update/${id}`,
      method: 'post',
      data: data,
    })
  }
  // 根据id查询数据
  static getById(id) {
    return request({
      url: `/comment/id`,
      method: 'get',
      params: {id}
    })
  }
  static listByNovelId(novelId) {
    return request({
      url: `/comment/list/${novelId}`,
      method: 'get',
    })
  }
  // 返回所有数据
  static page(param, page) {
    return request({
      url: `/comment/page`,
      method: 'get',
      params:{
        ...param,
        ...page,
      }
    })
  }
}
