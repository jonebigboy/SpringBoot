<template>
    <BaseContent>

        <table class="table table-hover">
            <thead>
                <tr>
                    <th>玩家</th>
                    <th>积分</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td scope="row">
                        <img :src="user.photo" alt="">
                        <span>{{ user.username }}</span>
                    </td>
                    <td>{{ user.rating }}</td>
                </tr>
            </tbody>
        </table>
        <nav aria-label="Page navigation example">
            <ul class="pagination floatend float-end">
                <li class="page-item">
                    <a class="page-link" href="#" @click="click_page(-1)">前一页</a>
                </li>
                <li class="page-item" v-for="page in pages" :key="page.number">
                    <a :class="'page-link ' + page.is_active" href="#" @click="click_page(page.number)">{{
                            page.number
                    }}</a>
                </li>
                <li class="page-item">
                    <a class="page-link" href="#" @click="click_page(-2)">后一页</a>
                </li>
            </ul>
        </nav>

    </BaseContent>

</template>


<script>

import BaseContent from '@/components/BaseContent.vue';
import { ref } from 'vue';
import $ from 'jquery'
import { useStore } from 'vuex';

export default {

    name: "LeaderBoard",
    components: { BaseContent },
    setup() {
        const store = useStore();
        const users = ref([]);
        let current_page = 1;
        let user_number = 0;
        let pages = ref([]);

        const click_page = page => {
            if (page === -1) {
                page = current_page - 1;
            } else if (page === -2) {
                page = current_page + 1;
            }
            let max_page = parseInt(Math.ceil(user_number / 3));
            if (page >= 1 && page <= max_page) {
                reflash(page);
            }
        }


        const updatePage = () => {
            let max_page = parseInt(Math.ceil(user_number / 3));
            let new_page = [];
            for (let i = current_page - 1; i <= current_page + 1; i++) {

                if (i >= 1 && i <= max_page) {
                    new_page.push({
                        number: i,
                        is_active: current_page === i ? "active" : "",
                    });
                }
            }
            pages.value = new_page;


        }


        const reflash = page => {
            current_page = page;

            $.ajax({
                url: "http://127.0.0.1:3000/ranklist/getlist/",
                tupe: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    page,
                },
                success(resp) {

                    users.value = resp.users;
                    user_number = resp.user_number;
                    updatePage();

                }
            });


        }
        reflash(current_page);

        return {
            users,
            pages,
            click_page,

        }
    }


}

</script>



<style scoped>
img {
    width: 4vh;
    border-radius: 50%;
}

th,
td {
    width: 20%;

}
</style>