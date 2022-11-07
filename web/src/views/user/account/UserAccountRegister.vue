<template>
    <BaseContent>
        <div class="row justify-content-center">
            <div class="col-3">
                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" aria-describedby="用户名">

                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password">
                    </div>
                    <div class="mb-3">
                        <label for="confirm_password" class="form-label">确认密码</label>
                        <input v-model="confirm_password" type="password" class="form-control" id="confirm_password">
                        <div id="emailHelp" class="text-danger">{{ message }}</div>
                    </div>

                    <div style="text-align:center">
                        <button type="submit" class="btn btn-primary">注册</button>
                    </div>

                </form>
            </div>
        </div>
    </BaseContent>

</template>


<script>
import BaseContent from '@/components/BaseContent.vue';


import { ref } from 'vue';
import $ from 'jquery';
import router from '@/router';
export default {
    name: "UserAccountRegister",
    components: { BaseContent },

    setup() {

        let username = ref("");
        let password = ref("");
        let confirm_password = ref("");
        let message = ref("");

        const register = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/register/",
                type: "post",
                data: {
                    username: username.value,
                    password: password.value,
                    confirm_password: confirm_password.value,
                },
                success(resp) {
                    console.log(resp);
                    if (resp.message === "success") {
                        router.push({ name: "UserAccountLogin" });
                    } else {
                        message.value = resp.message;
                    }

                },
                error() {
                    message.value = "出现错误";
                },


            });

        }

        return {
            register,
            username,
            password,
            confirm_password,
            message,
        };
    }

}

</script>



<style>

</style>