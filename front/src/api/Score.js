import request from './common/Request';

/**
 * 书架接口
 */
export default class Score {
  // 新增
  static save(data) {
    return request({
      url: '/score/save',
      method: 'post',
      data: data,
    })
  }
  // 是否存在
  static exist(novelId) {
    return request({
      url: '/score/exist',
      method: 'get',
      params: {novelId}
    })
  }
}