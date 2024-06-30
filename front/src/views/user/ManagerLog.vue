<template>
  <div class="container">
    <div class="row">
      <div id="left-item-wrapper"></div>
      <!-- 右侧主体 -->
      <div class="col-md-10">
        <!-- 上半部分 -->
        <div class="panel panel-default">
          <div class="panel-heading">
            <font size="3"> <span class="glyphicon glyphicon-user"></span>
              阅读历史
            </font>
          </div>
        </div>
        <!-- 下半部分 -->
        <div class="panel panel-default">
          <!-- 头部 -->
          <div class="panel-heading" style="height: 55px">
            <div class="pull-left">
              <font size="4" style="line-height: 35px">阅读历史</font>
            </div>
            <div class="pull-right">
              <button class="btn btn-success" @click="onLoad">
                <span class="glyphicon glyphicon-refresh"></span>刷&nbsp;新
              </button>
            </div>
          </div>
          <!-- 主列表 -->
          <div class="panel-body">
            <div class="modal-body">
              <book-list :show-read-log="true" :limit="1" :data="data"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ReadLog from "../../api/ReadLog";
  import BookList from "./component/book-list";

  export default {
    name: "ManagerLog",
    components: {BookList},
    data() {
      return {
        data: [],
      }
    },
    methods: {
      onLoad() {
        ReadLog.list().then(res => {
          this.data = res.data.data;
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