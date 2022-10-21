<template>
    <div class="playground">
        <GameMap />
    </div>
</template>


<script>
import GameMap from './GameMap.vue';

export default {
    name: "PlayGround",
    components: { GameMap }
}

</script>


<style scoped>
div.playground {
    width: 50vw;
    height: 80vh;

    margin: 40px auto;
}
</style>