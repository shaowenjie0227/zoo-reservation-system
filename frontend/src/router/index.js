import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "Layout",
    redirect: "/home",
    component: () => import("../layouts/index.vue"),
    meta: {
      title: "入口",
    },
    children: [
      {
        path: "/home",
        name: "Home",
        component: () => import("@/views/system/home/home.vue"),
        meta: {
          title: "首页",
        },
      },
      {
        path: "/user",
        name: "User",
        component: () => import("@/views/system/user.vue"),
        meta: {
          title: "用户管理",
        },
      },
      {
        path: "/notice",
        name: "Notice",
        component: () => import("@/views/system/notice.vue"),
        meta: {
          title: "公告管理",
        },
      },
      {
        path: "/banner",
        name: "Banner",
        component: () => import("@/views/system/banner.vue"),
        meta: {
          title: "轮播图管理",
        },
      },
      {
        path: "/animaltype",
        name: "AnimalType",
        component: () => import("@/views/system/animaltype.vue"),
        meta: {
          title: "动物物种管理",
        },
      },
      {
        path: "/animal",
        name: "Animal",
        component: () => import("@/views/system/animal.vue"),
        meta: {
          title: "动物信息管理",
        },
      },
      {
        path: "/animallocation",
        name: "AnimalLocation",
        component: () => import("@/views/system/animallocation.vue"),
        meta: {
          title: "动物地点管理",
        },
      },
      {
        path: "/conservationnews",
        name: "ConservationNews",
        component: () => import("@/views/system/conservationnews.vue"),
        meta: {
          title: "生态资讯",
        },
      },
      {
        path: "/front/conservation-news",
        name: "FrontConservationNews",
        component: () => import("@/views/front/conservation-news.vue"),
        meta: {
          title: "生态资讯",
        },
      },
      {
        path: "/report",
        name: "Report",
        component: () => import("@/views/system/report.vue"),
        meta: {
          title: "举报管理",
        },
      },
      {
        path: "/forumpost",
        name: "ForumPost",
        component: () => import("@/views/system/forumpost.vue"),
        meta: {
          title: "论坛帖子管理",
        },
      },
      {
        path: "/postCategory",
        name: "PostCategory",
        component: () => import("@/views/system/postCategory.vue"),
        meta: {
          title: "帖子分类管理",
        },
      },
      {
        path: "/like",
        name: "Like",
        component: () => import("@/views/system/like.vue"),
        meta: {
          title: "点赞管理",
        },
      },
      {
        path: "/collect",
        name: "Collect",
        component: () => import("@/views/system/collect.vue"),
        meta: {
          title: "收藏管理",
        },
      },
      {
        path: "/comment",
        name: "Comment",
        component: () => import("@/views/system/comment.vue"),
        meta: {
          title: "评论管理",
        },
      },
      {
        path: "/travel",
        name: "Travel",
        component: () => import("@/views/system/travel.vue"),
        meta: {
          title: "旅游景点管理",
        },
      },
      {
        path: "/travelGuide",
        name: "TravelGuide",
        component: () => import("@/views/system/travelGuide.vue"),
        meta: {
          title: "旅游攻略管理",
        },
      },
      {
        path: "/travelGuideCategory",
        name: "TravelGuideCategory",
        component: () => import("@/views/system/travelGuideCategory.vue"),
        meta: {
          title: "攻略分类管理",
        },
      },
      {
        path: "/ticket",
        name: "Ticket",
        component: () => import("@/views/system/ticket.vue"),
        meta: {
          title: "门票管理",
        },
      },
      {
        path: "/person",
        name: "Person",
        component: () => import("@/views/common/person.vue"),
        meta: {
          title: "个人信息",
        },
      },
      {
        path: "/ticketOrder",
        name: "TicketOrder",
        component: () => import("../views/system/ticketOrder.vue"),
        meta: {
          title: "门票订单",
        },
      },
    ],
  },
  {
    path: "/front",
    name: "FrontLayout",
    redirect: "/front/home",
    component: () => import("../front-layouts/index.vue"),
    meta: {
      title: "前台首页",
    },
    children: [
      {
        path: "/front/home",
        name: "FrontHome",
        component: () => import("../views/front/home.vue"),
        meta: {
          title: "前台首页",
        },
      },
      {
        path: "/front/person",
        name: "FrontPerson",
        component: () => import("@/views/common/person.vue"),
        meta: {
          title: "个人信息",
        },
      },
      {
        path: "/front/animal-list",
        name: "FrontAnimalList",
        component: () => import("../views/front/AnimalList.vue"),
        meta: {
          title: "动物列表",
        },
      },
      {
        path: "/front/community",
        name: "FrontCommunity",
        component: () => import("../views/front/Community.vue"),
        meta: {
          title: "动物社区",
        },
      },
      {
        path: "/front/posts/detail",
        name: "FrontPostsDetail",
        component: () => import("../views/front/PostDetail.vue"),
        meta: {
          title: "帖子详情",
        },
      },
      {
        path: "/front/location",
        name: "FrontLocation",
        component: () => import("../views/front/Location.vue"),
        meta: {
          title: "动物地点",
        },
      },
      {
        path: "/front/animal-detail",
        name: "FrontAnimalDetail",
        component: () => import("../views/front/AnimalDetail.vue"),
        meta: {
          title: "动物详情",
        },
      },
      {
        path: "/front/location-detail",
        name: "FrontLocationDetail",
        component: () => import("../views/front/LocationDetail.vue"),
        meta: {
          title: "动物地点详情",
        },
      },
      {
        path: "/front/my-collections",
        name: "FrontMyCollections",
        component: () => import("../views/front/MyCollections.vue"),
        meta: {
          title: "我的收藏",
        },
      },
      {
        path: "/front/travel-list",
        name: "FrontTravelList",
        component: () => import("../views/front/TravelList.vue"),
        meta: {
          title: "旅游景点",
        },
      },
      {
        path: "/front/my-likes",
        name: "FrontMyLikes",
        component: () => import("../views/front/MyLikes.vue"),
        meta: {
          title: "我的点赞",
        },
      },
      {
        path: "/front/my-posts",
        name: "FrontMyForumPost",
        component: () => import("../views/front/MyForumPost.vue"),
        meta: {
          title: "我的帖子",
        },
      },
      {
        path: "/front/my-travel-guide",
        name: "FrontMyTravelGuide",
        component: () => import("../views/front/MyTravelGuide.vue"),
        meta: {
          title: "我的攻略",
        },
      },
      {
        path: "/front/my-comments",
        name: "FrontMyComments",
        component: () => import("../views/front/MyComments.vue"),
        meta: {
          title: "我的评论",
        },
      },
      {
        path: "/front/my-ticket-order",
        name: "FrontMyTicketOrder",
        component: () => import("../views/front/MyTicketOrder.vue"),
        meta: {
          title: "我的订单",
        },
      },
      {
        path: "/front/travel-detail",
        name: "FrontTravelDetail",
        component: () => import("../views/front/TravelDetail.vue"),
        meta: {
          title: "旅游景点详情",
        },
      },
      {
        path: "/front/travel-guide-list",
        name: "FrontTravelGuideList",
        component: () => import("../views/front/TravelGuideList.vue"),
        meta: {
          title: "旅游攻略",
        },
      },
      {
        path: "/front/travel-guide-detail",
        name: "FrontTravelGuideDetail",
        component: () => import("@/views/front/TravelGuideDetail.vue"),
        meta: {
          title: "攻略详情",
        },
      },
      {
        path: "/front/conservation-news",
        name: "FrontConservationNews",
        component: () => import("@/views/front/conservation-news.vue"),
        meta: {
          title: "生态资讯",
        },
      },
      {
        path: "/front/conservation-news-detail",
        name: "FrontConservationDetail",
        component: () => import("@/views/front/conservation-news-detail.vue"),
        meta: {
          title: "资讯详情",
        },
      },
      {
        path: "/front/user-report",
        name: "UserReport",
        component: () => import("@/views/front/UserReport.vue"),
        meta: {
          title: "违规举报",
        },
      },
      {
        path: "/front/map",
        name: "FrontMap",
        component: () => import("../components/AMap.vue"),
        meta: {
          title: "地图",
        },
      },
      {
        path: "/front/posts/publish",
        name: "FrontPostsPublish",
        component: () => import("../views/front/AddForumPost.vue"),
        meta: {
          title: "发布帖子",
        },
      },
      {
        path: "/front/travel-guide/publish",
        name: "FrontTravelGuidePublish",
        component: () => import("../views/front/AddTravelGuide.vue"),
        meta: {
          title: "发布攻略",
        },
      },
      {
        path: "/front/ticket-order",
        name: "FrontTicketOrder",
        component: () => import("@/views/front/TicketOrder.vue"),
        meta: {
          title: "门票预订",
        },
      },
    ],
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("../views/common/login.vue"),
    meta: {
      title: "登录",
    },
  },
  {
    path: "/register",
    name: "Register",
    component: () => import("../views/common/register.vue"),
    meta: {
      title: "注册",
    },
  },
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: () => import("../views/common/notFound.vue"),
    meta: {
      title: "404",
    },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 全局前置守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title || "Vue App";
  next();
});

export default router;
