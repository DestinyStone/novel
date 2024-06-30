import request from './common/Request';


export default class Report {
  // 阅读进度
  static submit(data) {
    return request({
      url: '/report/submit',
      method: 'post',
      data: data,
    })
  }
}