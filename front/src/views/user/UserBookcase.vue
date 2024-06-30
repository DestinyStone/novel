<template>
  <div style="height: 100%">
    <header-view/>
    <div class="container">
      <el-divider></el-divider>
      <div style="font-size: 25px; padding: 20px 0; text-align: center">我的书架</div>
      <el-divider></el-divider>
      <book-list :data="data"/>
      <div style="display: flex; justify-content: end; padding: 10px 0;">
        <el-pagination
            background
            :current-page.sync="page.current"
            :page-size="page.size"
            layout="prev, pager, next"
            @current-change="onLoad"
            :total="page.total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
  import BookList from "./component/book-list";
  import HeaderView from "../common/HeaderView";
  import Collect from "../../api/Collect";
  export default {
    name: "UserBookcase",
    components: {HeaderView, BookList},
    data() {
      return {
        data: [],
        page: {
          current: 1,
          size: 10,
          total: 0,
        }
      }
    },
    methods: {
      onLoad() {
        Collect.page({}, this.page).then(res => {
          let data = res.data.data;
          this.data = data.records;
          this.page.total = data.total;
        })
      }
    },
    created() {
      this.onLoad();
    }
  }
</script>

<style scoped>

</style>