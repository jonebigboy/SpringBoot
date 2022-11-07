<template>


    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <router-link class="navbar-brand" :to="{ name: 'home' }">KOB</router-link>

            <div class="collapse navbar-collapse" id="navbarText">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <router-link :class="router_name === 'pk_index' ? 'nav-link active' : 'nav-link'"
                            :to="{ name: 'pk_index' }">对战</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link :class="router_name === 'ranklist_index' ? 'nav-link active' : 'nav-link'"
                            :to="{ name: 'ranklist_index' }">对战列表</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link :class="router_name === 'leaderboard_index' ? 'nav-link active' : 'nav-link'"
                            :to="{ name: 'leaderboard_index' }">排行</router-link>
                    </li>
                </ul>

                <ul class="navbar" style="list-style: none; margin-bottom: 0px;" v-if="$store.state.user.is_login">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            {{ username }}
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <li>
                                <router-link class="dropdown-item" :to="{ name: 'userbot_index' }">MY MODE</router-link>
                            </li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li>
                                <router-link class="dropdown-item" :to="{ name: 'UserAccountLogin' }" @click="loginout">
                                    退出
                                </router-link>
                            </li>
                        </ul>
                    </li>
                </ul>

                <ul class="navbar-nav " v-else>
                    <li class="nav-item">
                        <router-link class="nav-link " aria-current="page" :to="{ name: 'UserAccountLogin' }">登录
                        </router-link>
                    </li>
                    <li class="nav-item">
                        <router-link class="nav-link" :to="{ name: 'UserAccountRegister' }">注册</router-link>
                    </li>
                </ul>


            </div>
        </div>
    </nav>


</template>
  
  
<script>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { useStore } from 'vuex';


export default {
    name: "NavBar",

    setup() {
        const store = useStore();
        const route = useRoute();



        let username = computed(() => store.state.user.username);

        let router_name = computed(() => route.name);
        const loginout = () => {
            store.dispatch("loginout");
        }
        return {
            router_name,
            loginout,
            username,
        }

    }

}


</script>

<style scoped>

</style>
  