export const roles = [
  {
    label: "管理员",
    value: "admin",
  },
  {
    label: "用户",
    value: "user",
  },
];

export const menuList = [
  {
    roleFlag: "admin",
    name: "管理员",
    backMenu: [
      {
        name: "首页",
        path: "/home",
        icon: "HomeFilled",
        tableName: "",
        buttons: [],
      },
      {
        name: "个人信息",
        path: "/person",
        icon: "UserFilled",
        tableName: "person",
        buttons: [],
      },
      {
        name: "系统管理",
        path: "/system",
        icon: "Setting",
        children: [
          {
            name: "用户管理",
            path: "/user",
            tableName: "user",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "公告管理",
            path: "/notice",
            tableName: "notice",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "轮播图管理",
            path: "/banner",
            tableName: "banner",
            buttons: ["新增", "编辑", "删除"],
          },
        ],
      },
      {
        name: "信息管理",
        path: "/info",
        icon: "Tickets",
        children: [
          {
            name: "动物物种管理",
            path: "/animaltype",
            tableName: "animaltype",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "动物信息管理",
            path: "/animal",
            tableName: "animal",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "动物地点管理",
            path: "/animallocation",
            tableName: "animallocation",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "论坛帖子管理",
            path: "/forumpost",
            tableName: "forumpost",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "帖子分类管理",
            path: "/postCategory",
            tableName: "postCategory",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "生态保护新闻管理",
            path: "/conservationnews",
            tableName: "conservationnews",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "动物馆管理",
            path: "/travel",
            tableName: "travel",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "动物攻略管理",
            path: "/travelGuide",
            tableName: "travelGuide",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "攻略分类管理",
            path: "/travelGuideCategory",
            tableName: "travelGuideCategory",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "门票管理",
            path: "/ticket",
            tableName: "ticket",
            buttons: ["新增", "编辑", "删除"],
          },
          {
            name: "门票订单管理",
            path: "/ticketOrder",
            tableName: "ticketOrder",
            buttons: ["删除"],
          },
        ],
      },
      {
        name: "互动管理",
        path: "/interact",
        icon: "Connection",
        children: [
          {
            name: "点赞管理",
            path: "/like",
            tableName: "like",
            buttons: ["删除"],
          },
          {
            name: "收藏管理",
            path: "/collect",
            tableName: "collect",
            buttons: ["删除"],
          },
          {
            name: "举报管理",
            path: "/report",
            tableName: "report",
            buttons: ["删除"],
          },
          {
            name: "评论管理",
            path: "/comment",
            tableName: "postComment",
            buttons: ["删除"],
          },
        ],
      },
    ],
  },
];
