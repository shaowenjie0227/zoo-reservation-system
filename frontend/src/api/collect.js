import http from '@/utils/http'

// 收藏相关API
export const collectApi = {
  // 获取我的收藏列表
  getMyCollections(params) {
    return http.get('/collect/page', {
      params: {
        pageNum: params.page,
        pageSize: params.size,
        type: params.type
      }
    })
  },

  // 添加收藏
  addCollect(data) {
    return http.post('/collect/add', data)
  },

  // 取消收藏
  cancelCollect(id) {
    return http.delete('/collect/del', {
      params: { id }
    })
  },

  // 检查是否已收藏
  isCollected(userId, type, targetId) {
    return http.get('/collect/isCollected', {
      params: { userId, type, targetId }
    })
  }
}