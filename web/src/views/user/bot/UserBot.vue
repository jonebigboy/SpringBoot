<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card">
                    <div class="card-body">
                        <img class="img-fluid" :src="$store.state.user.photo" alt="头像" />
                    </div>
                </div>

            </div>
            <div class="col-9">
                <div class="card">
                    <div class="card-header">
                        <span class="Mytitle" style="font-size: 130%">MyBot</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal"
                            data-bs-target="#addBot">添加Bot</button>

                        <!-- modol -->
                        <div class="modal modal-xl" id="addBot" tabindex="-1">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">添加Bot</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="title" class="form-label">标题</label>
                                            <input v-model="botadd.title" type="text" class="form-control" id="title"
                                                placeholder="输入标题">
                                        </div>
                                        <div class="mb-3">
                                            <label for="description" class="form-label">描述</label>
                                            <textarea v-model="botadd.description" type="text" class="form-control"
                                                id="description" placeholder="输入描述" style="height: 100px"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="content" class="form-label">内容</label>
                                            <VAceEditor v-model:value="botadd.content" @init="editorInit" lang="c_cpp"
                                                theme="textmate" style="height: 300px" />
                                        </div>


                                    </div>
                                    <div class="modal-footer">
                                        <span class="message" style="color:red">{{ botadd.message }}</span>
                                        <button type="button" class="btn btn-secondary"
                                            data-bs-dismiss="modal">关闭</button>
                                        <button type="button" class="btn btn-primary" @click="add_bot">添加</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th scope="col">标题</th>
                                    <th scope="col">描述</th>
                                    <th scope="col">创建时间</th>
                                    <th scope="col">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <th scope="row">{{ bot.title }}</th>
                                    <th>{{ bot.description }}</th>
                                    <th>{{ bot.createTime }}</th>
                                    <th>
                                        <button type="button" class="btn btn-success" style="margin-right:10px"
                                            data-bs-toggle="modal" :data-bs-target="'#addBot' + bot.id">修改</button>
                                        <!-- modol -->
                                        <div class="modal modal-xl" :id="'addBot' + bot.id" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">添加Bot</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                            aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label for="title" class="form-label">标题</label>
                                                            <input v-model="bot.title" type="text" class="form-control"
                                                                id="title" placeholder="输入标题">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="description" class="form-label">描述</label>
                                                            <textarea v-model="bot.description" type="text"
                                                                class="form-control" id="description" placeholder="输入描述"
                                                                style="height: 100px"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="content" class="form-label">内容</label>
                                                            <VAceEditor v-model:value="bot.content" @init="editorInit"
                                                                lang="c_cpp" theme="textmate" style="height: 300px" />
                                                        </div>

                                                    </div>
                                                    <div class="modal-footer">
                                                        <span class="message" style="color:red">{{ bot.message }}</span>
                                                        <button type="button" class="btn btn-secondary"
                                                            data-bs-dismiss="modal">关闭</button>
                                                        <button type="button" class="btn btn-primary"
                                                            @click="update_bot(bot)">添加</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <button type="button" class="btn btn-danger"
                                            @click="remove_bot(bot.id)">删除</button>
                                    </th>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<script>
import { reactive, ref } from 'vue';
import $ from 'jquery';
import { useStore } from 'vuex';

import { Modal } from 'bootstrap/dist/js/bootstrap.min.js'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default {
    name: "UserBot",
    components: { VAceEditor },

    setup() {

        const store = useStore();
        let bots = ref([]);

        ace.config.set(
            "basePath",
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

        const botadd = reactive({
            title: "",
            description: "",
            content: "",
            message: "",
        });

        const reflash = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;

                }
            });
        }

        reflash();

        const add_bot = () => {
            botadd.message = "";
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/add/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {

                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,

                },
                success(resp) {
                    if (resp.message === "success") {
                        botadd.title = "";
                        botadd.description = "";
                        botadd.content = "";
                        Modal.getInstance("#addBot").hide();

                        reflash();
                    } else {
                        botadd.message = resp.message;
                    }

                }
            });

        }
        const remove_bot = (bot_id) => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/remove/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    bot_id: bot_id,

                },
                success(resp) {
                    if (resp.message === "success") {

                        reflash();
                    }

                }
            });
        }
        const update_bot = (bot) => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/update/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,

                },
                success(resp) {
                    if (resp.message === "success") {
                        Modal.getInstance('#addBot' + bot.id).hide();
                        reflash();
                    } else {
                        bot.message = resp.message;
                    }


                }
            });
        }



        return {
            bots,
            reflash,
            botadd,
            add_bot,
            remove_bot,
            update_bot,
        };
    }
}

</script>



<style scoped>
.card {
    margin-top: 20px;
}
</style>