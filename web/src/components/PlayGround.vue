<template>
    <div class="row">
        <div class="col-3 text-center">
            <img class="img-fluid" :src="$store.state.user.photo" alt="自己头像">
            <div class="name">{{ $store.state.user.username }}</div>
        </div>
        <div class="col-6">
            <div class="playground">
                <GameMap />
            </div>
        </div>
        <div class="col-3 text-center">
            <img class="img-fluid" :src="$store.state.pk.opponent_photo" alt="敌人头像">
            <div class="name">{{ $store.state.pk.opponent_name }}</div>

        </div>
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


img {
    border-radius: 50%;
    width: 15vw;
    margin-top: 10vh;
}

div.name {
    color: white;
    font-size: 20px;
    font-weight: 500;
    margin-top: 3vh;
}
</style>