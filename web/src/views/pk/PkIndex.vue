<template>
    <PlayGround v-if="$store.state.pk.status === 'gamestart'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
</template>


<script>

import MatchGround from '@/components/MatchGround.vue';
import PlayGround from '@/components/PlayGround.vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
    name: "PkIndex",
    components: { PlayGround, MatchGround },

    setup() {
        let socket = null;
        const store = useStore();

        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
        onMounted(() => {
            socket = new WebSocket(socketUrl);
            store.commit("updateOppnent", {
                name: "未知对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            });
            //链接
            socket.onopen = () => {
                console.log("connect");
                store.commit("updateSocket", socket);
            }
            //接受消息
            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if (data.event === "success_match") {
                    store.commit("updateOppnent", {
                        name: data.opponent_name,
                        photo: data.opponent_photo,
                    });

                    setTimeout(() => {
                        store.commit("updateStatus", "gamestart");
                    }, 2000);
                    store.commit("updateGameMap", data.gameMap);
                }

                console.log(data);
            }
            socket.onclose = () => {
                console.log("disconnect");

            }

        });

        onUnmounted(() => {
            socket.close();
            store.commit("updateStatus", "matching");
            store.commit("updateSocket", null);
        });



        return {

        };
    },
}

</script>



<style>

</style>