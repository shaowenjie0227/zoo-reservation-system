import http from '@/utils/http'

// 点赞相关API
export const likeApi = {
  // 获取我的点赞列表
  getMyLikes(params) {
    return http.get('/like/page', {
      params: {
        pageNum: params.page,
        pageSize: params.size,
        type: params.type
      }
    })
  },

  // 添加点赞
  addLike(data) {
    return http.post('/like/add', data)
  },

  // 取消点赞
  cancelLike(id) {
    return http.delete('/like/del', {
      params: { id }
    })
  }
}