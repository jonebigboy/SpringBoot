<template>
    <BaseContent>
        <div class="row justify-content-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input type="text" v-model="username" class="form-control" id="username" aria-describedby="用户名">

                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input type="password" v-model="password" class="form-control" id="password">
                        <div id="emailHelp" class="text-danger">{{ message }}</div>
                    </div>

                    <div style="text-align:center">
                        <button type="submit" class="btn btn-primary">登录</button>
                    </div>

                </form>
            </div>
        </div>
    </BaseContent>

</template>


<script>
import BaseContent from '@/components/BaseContent.vue';
import router from '@/router';
import { ref } from 'vue';
import { useStore } from 'vuex';



export default {
    name: "UserAccountLogin",
    components: { BaseContent },

    setup() {

        const store = useStore();
        let username = ref("");
        let password = ref("");
        let message = ref("");

        const login = () => {
            message.value = "";

            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success() {
                    console.log(store.state.user.token);
                    store.dispatch("getinfo", {
                        success() {
                            router.push({ name: "home" });
                            console.log(store.state.user);
                        },

                    });
                },
                error() {
                    message.value = "用户名或者密码错误";
                },
            })
        }

        return {
            login,
            store,
            username,
            password,
            message,

        }
    }
}

</script>



<style>

</style>