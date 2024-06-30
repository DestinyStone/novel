<template>
  <div style="display: flex; flex-flow: wrap">
    <book-item :show-read-log="showReadLog" :style="styleCalc" v-for="(item, index) of data" :data="item" :key="index"/>
  </div>
</template>

<script>
  import BookItem from "./book-item";
  export default {
    name: "book-list",
    components: {BookItem},
    props: {
      showReadLog: {
        type: Boolean,
        default: false,
      },
      limit: {
        type: Number,
        default: 1,
      },
      data: {
        type: Array,
        default() {
          return []
        }
      }
    },
    computed: {
      styleCalc() {
        let width = 100 / this.limit;
        return {width: width + '%'};
      }
    },
    data() {
      return {

      }
    }
  }
</script>

<style scoped>

</style>