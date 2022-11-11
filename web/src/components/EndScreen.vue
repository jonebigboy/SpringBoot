<template>
    <div class="playground">
        <div class="text-center title">
            {{ result }}
        </div>
        <div class="text-center">
            <button type="button" @click="restart" class="btn btn-warning btn-lg">不服</button>
        </div>
    </div>
</template>


<script>
import { ref } from 'vue';
import { useStore } from 'vuex';





export default {
    name: "EndScreen",
    components: {},
    setup() {
        const store = useStore();

        let result = ref("");
        if (store.state.pk.loser === "all") {
            result.value = "draw";
        } else if (store.state.pk.loser === "A" && store.state.pk.a_id == store.state.user.id) {
            result.value = "lose";
        } else if (store.state.pk.loser === "B" && store.state.pk.b_id == store.state.user.id) {
            result.value = "lose";
        } else {
            result.value = "win";
        }

        const restart = () => {
            store.commit("updateStatus", "matching");
            store.commit("updateLoser", "none");
            store.commit("updateOppnent", {
                name: "未知对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            });
        }


        return {
            result,
            restart,

        }
    },
}

</script>


<style scoped>
div.playground {
    width: 40vw;
    height: 40vh;
    background-color: rgba(255, 255, 255, 0.5);

    position: absolute;
    top: 20vh;
    left: 30vw;
}



div.text-center {
    padding-top: 5vh;
}

.title {
    color: white;
    font-size: 10vh;
    font-weight: 500;
}

div>button {}
</style>