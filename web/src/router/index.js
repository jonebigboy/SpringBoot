
import { createRouter, createWebHistory } from 'vue-router'
import NotFind from '@/views/error/NotFind'
import LeaderBoard from '@/views/leaderboard/LeaderBoard'
import RankList from '@/views/list/RankList'
import UserBot from '@/views/user/bot/UserBot'
import PkIndex from '@/views/pk/PkIndex'

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/",

  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndex
  },
  {
    path: "/leaderboard/",
    name: "leaderboard_index",
    component: LeaderBoard
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RankList
  },
  {
    path: "/userbot/",
    name: "userbot_index",
    component: UserBot
  },
  {
    path: "/404/",
    name: "nofind",
    component: NotFind
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/",
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes

})

export default router
