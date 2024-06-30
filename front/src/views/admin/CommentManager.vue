<template>
  <div>
    <!-- 上半部分 -->
    <div class="panel panel-default">
      <div class="panel-heading">
        <font size="3"> <span class="glyphicon glyphicon-user"></span>
          评论管理
        </font>
      </div>
    </div>
    <!-- 下半部分 -->
    <div class="panel panel-default">
      <!-- 头部 -->
      <div class="panel-heading" style="height: 55px">
        <div class="pull-left">
          <font size="4" style="line-height: 35px">评论管理</font>
        </div>
        <div class="pull-right">
        </div>
      </div>
      <!-- 主列表 -->
      <div class="panel-body">
        <div style="display: flex;justify-content: space-between;">
          <div>
            <el-input clearable v-model="query.novelId" placeholder="请输入小说ID" style="width: 200px"/>
            <el-select clearable v-model="query.status" placeholder="请选择状态" style="width: 200px; margin-left: 10px;">
              <el-option label="待审批" :value="0"></el-option>
              <el-option label="审批通过" :value="1"></el-option>
              <el-option label="审批驳回" :value="-1"></el-option>
            </el-select>
          </div>
          <div>
            <el-button @click="onLoad">查询</el-button>
          </div>
        </div>
        <div style="padding: 10px 0;"></div>
        <el-table
            :data="data"
            border
            style="width: 100%">
          <el-table-column prop="novelId" label="小说ID" align="center" width="100"></el-table-column>
          <el-table-column prop="novelName" label="小说名称" align="center" width="200"></el-table-column>
          <el-table-column prop="content" label="评论内容"  align="center"></el-table-column>
          <el-table-column prop="createTime" label="评论时间"  align="center" width="180"></el-table-column>
          <el-table-column prop="status" label="状态"  align="center">
            <template slot-scope="{row}">
              <span v-if="row.status === 0">待审批</span>
              <span v-if="row.status === 1">审批通过</span>
              <span v-if="row.status === -1">审批驳回</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" align="center" width="240">
            <template slot-scope="{row}">
              <div style="display: flex; justify-content: space-around">
                <div v-if="row.status === 0" @click="approveHandler(row, 1)" style="cursor: pointer; color: #409eff;">审批通过</div>
                <div v-if="row.status === 0" @click="approveHandler(row, -1)" style="cursor: pointer; color: #409eff;">审批驳回</div>
                <div @click="openUpdate(row)" style="cursor: pointer; color: #409eff;">编辑</div>
                <div @click="openDelete(row)" style="cursor: pointer; color: #409eff;">删除</div>
              </div>
            </template>
          </el-table-column>
        </el-table>
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
    <comment-submit :id="currentId" v-if="showSubmit" @close="() => {showSubmit = false; this.onLoad()}"/>
  </div>
</template>

<script>
  import Comment from "../../api/Comment";
  import CommentSubmit from "./comment/CommentSubmit";

  export default {
    name: "CommentManager",
    components: {CommentSubmit},
    data() {
      return {
        data: [],
        query: {},
        page: {
          current: 1,
          size: 5,
          total: 0,
        },
        showSubmit: false,
        currentId: null,
      }
    },
    methods: {
      approveHandler(data, status) {
        let confirmMessage = status === 1 ? '确定审批通过吗?' : '确定审批驳回吗?';
        let successMessage = status === 1 ? '审批通过成功' : '审批驳回成功';
        this.$confirm(confirmMessage,'提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: "warning",
        }).then(() => {
          data.status = status;
          return Comment.update(data.id, data);
        }).then(() => {
          this.$message.success(successMessage);
          this.onLoad();
        })
      },
      openDelete(data) {
        this.$confirm(`确定删除该数据吗?`,'提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: "warning",
        }).then(() => {
          return Comment.delete(data.id);
        }).then(() => {
          this.$message.success("删除成功");
          this.onLoad();
        })
      },
      openUpdate(data) {
        this.currentId = data.id;
        this.showSubmit = true;
      },
      openSave() {
        this.currentId = null;
        this.showSubmit = true;
      },
      onLoad() {
        Comment.page(this.query, this.page).then(res => {
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