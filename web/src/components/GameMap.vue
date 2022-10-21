<template>
    <div ref="parent" class="gamemap">
        <canvas ref="ctx"></canvas>
    </div>
</template>


<script>

import { GameMap } from '@/assets/script/GameMap'
import { onMounted, ref } from 'vue';

export default {

    setup() {
        const parent = ref(null);
        const ctx = ref(null);
        onMounted(() => {
            new GameMap(ctx.value.getContext('2d'), parent.value);
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