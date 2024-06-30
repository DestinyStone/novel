import request from './common/Request';

/**
 * 书架接口
 */
export default class ReadLog {
  // 阅读进度
  static process(novelId) {
    return request({
      url: '/read/log/process',
      method: 'get',
      params: {novelId}
    })
  }
  // 新增
  static save(data) {
    return request({
      url: '/read/log/save',
      method: 'post',
      data: data,
    })
  }
  // 返回所有数据
  static list(param) {
    return request({
      url: `/read/log/list`,
      method: 'get',
      params:{
        ...param,
      }
    })
  }
  // 返回所有数据
  static page(param, page) {
    return request({
      url: `/read/log/page`,
      method: 'get',
      params:{
        ...param,
        ...page,
      }
    })
  }
}