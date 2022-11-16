<template>
    <BaseContent>
        <div class="card-body">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th scope="col">玩家A</th>
                        <th scope="col">玩家B</th>
                        <th scope="col">对局结果</th>
                        <th scope="col">对局时间</th>
                        <th scope="col">操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="record in records" :key="record.record.id">
                        <td>
                            <img :src="record.a_photo" alt="">
                            <span>{{ record.a_username }}</span>
                        </td>
                        <td>
                            <img :src="record.b_photo" alt="">
                            <span>{{ record.b_username }}</span>
                        </td>
                        <td>{{ record.result }}</td>
                        <td>{{ record.record.createTime }}</td>
                        <td>
                            <button type="button" class="btn btn-success" @click="open_record_content(record.record.id)"
                                style="margin-right:10px">查看</button>
                            <button type="button" class="btn btn-danger"
                                @click="reomve_record(record.record.id)">删除</button>
                        </td>
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
        </div>
    </BaseContent>
</template>


<script>
import BaseContent from '@/components/BaseContent.vue';
import { ref } from 'vue';
import $ from 'jquery';
import { useStore } from 'vuex';
import router from '@/router/index'




export default {
    name: "RankList",
    components: { BaseContent },

    setup() {
        const store = useStore();
        let current_page = 1;
        const records = ref([]);
        let record_number = 0;
        let pages = ref([]);//表示的是当前页码

        const click_page = page => {
            if (page === -1) {
                page = current_page - 1;
            } else if (page === -2) {
                page = current_page + 1;
            }
            let max_page = parseInt(Math.ceil(record_number / 10));

            if (page >= 1 && page <= max_page) {
                reflash(page);
            }
        }

        const update_page = () => {
            let max_page = parseInt(Math.ceil(record_number / 10));
            let new_page = [];
            for (let i = current_page - 2; i <= current_page + 2; i++) {
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
                url: "http://127.0.0.1:3000/record/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    page,
                },
                success(resp) {
                    records.value = resp.record;
                    record_number = resp.records_number;
                    update_page();

                }
            });
        }
        const reomve_record = id => {
            $.ajax({
                url: "http://127.0.0.1:3000/record/remove/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    record_id: id,
                },
                success(resp) {
                    console.log(resp);
                    reflash(current_page);
                }
            });

        }


        const stringTo2D = (map) => {
            let g = [];
            for (let i = 0, k = 0; i < 13; i++) {
                let line = []
                for (let j = 0; j < 14; j++, k++) {
                    if (map[k] === '1') {
                        line.push(1);
                    } else {
                        line.push(0);
                    }
                }
                g.push(line);
            }
            return g;
        }


        const open_record_content = recordId => {

            for (const record of records.value) {

                if (record.record.id === recordId) {

                    store.commit("updateIsRecord", true);
                    console.log(record);
                    store.commit("updateStep", {
                        a_step: record.record.asteps,
                        b_step: record.record.bsteps,
                    });
                    store.commit("updateRecordPlayerInfo", {
                        a_username: record.a_username,
                        a_photo: record.a_photo,
                        b_username: record.b_username,
                        b_photo: record.b_photo,
                    });


                    store.commit("updateGameInfo", {
                        map: stringTo2D(record.record.map),
                        a_id: record.record.aid,
                        a_sx: record.record.asx,
                        a_sy: record.record.asy,
                        b_id: record.record.bid,
                        b_sx: record.record.bsx,
                        b_sy: record.record.bsy,
                    });

                    store.commit("updateRecordLoser", record.record.loser);

                    router.push({
                        name: "ranklist_content",
                        params: {
                            recordId
                        }

                    });
                    break;
                }
            }


        }

        reflash(current_page);

        return {
            records,
            record_number,
            click_page,
            pages,
            open_record_content,
            reomve_record,
        }

    }
}

</script>



<style scoped>
img {
    width: 4vh;
    border-radius: 50%;
}
</style>