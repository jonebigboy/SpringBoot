import { createRouter, createWebHistory } from 'vue-router'
import NotFind from '@/views/error/NotFind'
import LeaderBoard from '@/views/leaderboard/LeaderBoard'
import RankList from '@/views/list/RankList'
import UserBot from '@/views/user/bot/UserBot'
import PkIndex from '@/views/pk/PkIndex'
import UserAccountLogin from '@/views/user/account/UserAccountLogin'
import UserAccountRegister from '@/views/user/account/UserAccountRegister'
import store from '@/store'


const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/",
    meta: {
      requireAuth: true,
    }

  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndex,
    meta: {
      requireAuth: true,
    }
  },
  {
    path: "/leaderboard/",
    name: "leaderboard_index",
    component: LeaderBoard,
    meta: {
      requireAuth: true,
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RankList,
    meta: {
      requireAuth: true,
    }
  },
  {
    path: "/user/bot/",
    name: "userbot_index",
    component: UserBot,
    meta: {
      requireAuth: false,
    }

  },
  {
    path: "/user/account/login/",
    name: "UserAccountLogin",
    component: UserAccountLogin,
    meta: {
      requireAuth: false,
    }
  },
  {
    path: "/user/account/register/",
    name: "UserAccountRegister",
    component: UserAccountRegister,
    meta: {
      requireAuth: false,
    }
  },
  {
    path: "/404/",
    name: "nofind",
    component: NotFind,
    meta: {
      requireAuth: false,
    }
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/",
  }

]



const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {

  const jwt_token = localStorage.getItem("jwt_token");
  let valid = true;//表示当前是否有token
  if (jwt_token) {
    store.commit("updateToken", jwt_token);//更新一下token 获取信息查看是否获取成功
    store.dispatch("getinfo", {
      success: () => {
      },
      error: () => {
        alert("token is out time pleace relogin");
        localStorage.removeItem('jwt_token');
        store.dispatch("logout");
        next({ name: "UserAccountLogin" });
      },
    });
  } else {
    valid = false;
  }

  if (to.meta.requireAuth && !store.state.user.is_login) {
    if (valid) {
      next();
    } else {
      next({ name: "UserAccountLogin" });
    }

  } else {
    next();
  }

})




export default router
