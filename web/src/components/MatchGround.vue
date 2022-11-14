<template>
    <div class="playground">
        <div class="row">
            <div class="col-4 text-center">
                <img class="img-fluid" :src="$store.state.user.photo" alt="自己头像">
                <div class="name">{{ $store.state.user.username }}</div>
            </div>
            <div class="col-4 text-center ">
                <div class="selectlist">
                    <select v-model="select_bot" class="form-select">
                        <option value="-1" selected>自己出战</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                            {{ bot.title }}
                        </option>
                    </select>
                </div>
            </div>

            <div class="col-4 text-center">
                <img class="img-fluid" :src="$store.state.pk.opponent_photo" alt="敌人头像">
                <div class="name">{{ $store.state.pk.opponent_name }}</div>
            </div>

        </div>
        <div class="text-center">
            <button type="button" class="btn btn-warning btn-lg" @click="start_matching">{{ match_message }}</button>
        </div>
    </div>
</template>


<script>
import { ref } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';


export default {
    name: "MatchGround",
    components: {},
    setup() {
        let bots = ref([]);
        let match_message = ref("");
        match_message.value = "开始匹配";
        const store = useStore();
        let select_bot = ref("-1");//付初始值就有默认选中的值

        const start_matching = () => {
            if (match_message.value === "开始匹配") {
                store.state.pk.socket.send(JSON.stringify({
                    "event": "start_matching",
                    "bot_id": select_bot.value,
                }));
                match_message.value = "取消";
            } else {
                store.state.pk.socket.send(JSON.stringify({
                    "event": "cancel_matching",
                }));
                match_message.value = "开始匹配";
            }
        }

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



        return {
            match_message,
            start_matching,
            bots,
            select_bot,
        }

    }
}

</script>


<style scoped>
div.playground {
    width: 50vw;
    height: 80vh;
    background-color: rgba(255, 255, 255, 0.5);
    margin: 40px auto;
}

div.text-center {
    padding-top: 10vh;
}

img {
    border-radius: 50%;
    width: 15vw;
}

div.name {
    color: black;
    font-size: 20px;
    font-weight: 500;
    margin-top: 3vh;
}

div>button {
    margin-top: 5vh;
}

.selectlist {
    margin: 10vh auto;
    width: 10vw;
}
</style>