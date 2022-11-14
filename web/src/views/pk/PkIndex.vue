<template>
    <PlayGround v-if="$store.state.pk.status === 'gamestart'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
    <EndScreen v-if="$store.state.pk.loser != 'none'" />
</template>


<script>

import EndScreen from '@/components/EndScreen.vue';
import MatchGround from '@/components/MatchGround.vue';
import PlayGround from '@/components/PlayGround.vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
    name: "PkIndex",
    components: { PlayGround, MatchGround, EndScreen },

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
            store.commit("updateLoser", "none");
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

                    setTimeout(() => {//过2s显示页面
                        store.commit("updateStatus", "gamestart");
                    }, 200);
                    store.commit("updateGameInfo", data.gameInfo);

                } else if (data.event === "move") {
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakers;
                    snake0.set_direction(data.a_d);
                    snake1.set_direction(data.b_d);
                } else if (data.event === "result") {
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakers;
                    console.log(data.loser)
                    if (data.loser === "all" || data.loser === "A") {
                        snake0.state = "die";
                        snake0.color = "white"
                    }
                    if (data.loser === "all" || data.loser === "B") {
                        snake1.state = "die";
                        snake1.color = "white"
                    }

                    store.commit("updateLoser", data.loser);
                    console.log(store.state.pk.loser);

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