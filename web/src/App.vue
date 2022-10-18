<template>
  <div class="demo">
    <h1>名称:{{bot_name}}</h1>
    <h1>战斗力:{{bot_number}}</h1>
  </div>

</template>


<script>
import $ from 'jquery';
import { ref } from 'vue';

export default {
  name: "App",


  setup() {
    let bot_name = ref("");
    let bot_number = ref("");

    $.ajax({
      url: "http://127.0.0.1:8080/pk/getinfo/",
      type: "GET",
      success: function (resp) {
        bot_name.value = resp.name;
        bot_number.value = resp.number;
      },
    });

    return {
      bot_name,
      bot_number,
    }
  }


};
</script>

<style>
body {
  background-image: url("@/assets/zed.jpg");
  background-size: cover;
}
</style>
