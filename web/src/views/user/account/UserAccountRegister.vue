<template>
    <BaseContent>
        <form>
            <div class="mb-3">
                <label for="username" class="form-label">用户名</label>
                <input type="text" class="form-control" id="username" aria-describedby="用户名">

            </div>

            <div class="mb-3">
                <label for="exampleInputPassword1" class="form-label">Password</label>
                <input type="password" class="form-control" id="exampleInputPassword1">
            </div>

            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
    </BaseContent>

</template>


<script>
import BaseContent from '@/components/BaseContent.vue';


export default {
    name: "UserAccountRegister",
    components: { BaseContent }
}

</script>



<style>

</style>