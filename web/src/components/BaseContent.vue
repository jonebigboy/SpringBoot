<template>
    <div class="container glap">
        <div class="card">
            <div class="card-body">
                <slot></slot>
            </div>
        </div>
    </div>

</template>


<script>

export default {
    name: "BaseContent",

}

</script>



<style scoped>
.glap {
    margin-top: 20px;
}
</style>