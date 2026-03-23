import http from "@/utils/http";

// 门票相关API
export const ticketApi = {
  // 分页查询门票
  getTicketPage(params) {
    return http.get("/ticket/page", {
      params: {
        pageNum: params.pageNum,
        pageSize: params.pageSize,
        name: params.name,
        travelId: params.travelId,
        status: params.status,
      },
    });
  },

  // 获取所有门票
  getAllTickets() {
    return http.get("/ticket/list");
  },

  // 根据ID查询门票
  getTicketById(id) {
    return http.get("/ticket/getById", {
      params: { id },
    });
  },

  // 根据景点ID查询门票列表
  getTicketsByTravelId(travelId) {
    return http.get("/ticket/getByTravelId", {
      params: { travelId },
    });
  },

  // 新增门票
  addTicket(data) {
    return http.post("/ticket/add", data);
  },

  // 批量新增门票
  batchAddTickets(data) {
    return http.post("/ticket/batchAdd", data);
  },

  // 编辑门票
  editTicket(data) {
    return http.post("/ticket/edit", data);
  },

  // 删除门票
  deleteTicket(id) {
    return http.get("/ticket/del", {
      params: { id },
    });
  },

  // 批量删除门票
  batchDeleteTickets(ids) {
    return http.post("/ticket/batchDel", ids);
  },
};
