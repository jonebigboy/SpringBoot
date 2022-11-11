<template>
    <div ref="parent" class="gamemap">
        <canvas ref="ctx" tabindex="0"></canvas>
    </div>
</template>


<script>

import { GameMap } from '@/assets/script/GameMap'
import { onMounted, ref } from 'vue';
import { useStore } from 'vuex';

export default {

    setup() {
        const store = useStore();
        const parent = ref(null);
        const ctx = ref(null);
        onMounted(() => {

            store.commit("updateGameObject", new GameMap(ctx.value.getContext('2d'), parent.value, store.state.pk.gamemap, store));
        });

        return {
            parent,
            ctx,
        };
    },

}
</script>


<style scoped>
div.gamemap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>