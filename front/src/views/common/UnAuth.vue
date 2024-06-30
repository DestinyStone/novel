<template>
  <div>
    <header-view/>
    <div class="container" style="font-size: 30px; text-align: center; margin-top: 100px; font-weight: 700">
      用户未登录, 请点击注册或登录按钮
    </div>
  </div>
</template>

<script>
  import HeaderView from "./HeaderView";
  export default {
    name: "UnAuth",
    components: {HeaderView}
  }
</script>

<style scoped>

</style>