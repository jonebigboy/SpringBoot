<template>


    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <router-link class="navbar-brand" :to="{name:'home'}">KOB</router-link>

            <div class="collapse navbar-collapse" id="navbarText">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <router-link :class="router_name==='pk_index'? 'nav-link active':'nav-link'"
                            :to="{name:'pk_index'}">对战</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link :class="router_name==='ranklist_index'? 'nav-link active':'nav-link'"
                            :to="{name:'ranklist_index'}">对战列表</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link :class="router_name==='leaderboard_index'? 'nav-link active':'nav-link'"
                            :to="{name:'leaderboard_index'}">排行</router-link>
                    </li>
                </ul>

                <ul class="navbar" style="list-style: none; margin-bottom: 0px;">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            jonebigboy
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <li>
                                <router-link class="dropdown-item" :to="{name:'userbot_index'}">MY MODE</router-link>
                            </li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li><a class="dropdown-item" href="#">退出</a></li>
                        </ul>
                    </li>
                </ul>


            </div>
        </div>
    </nav>


</template>
  
  
<script>
import { computed } from 'vue';
import { useRoute } from 'vue-router';



export default {
    name: "NavBar",

    setup() {
        const route = useRoute();
        let router_name = computed(() => route.name);

        return {
            router_name,
        }

    }

}


</script>

<style scoped>

</style>
  