<template>
  <div>
    <!-- 上半部分 -->
    <div class="panel panel-default">
      <div class="panel-heading">
        <font size="3"> <span class="glyphicon glyphicon-user"></span>
          管理用户
        </font>
      </div>
    </div>
    <!-- 下半部分 -->
    <div class="panel panel-default">
      <!-- 头部 -->
      <div class="panel-heading" style="height: 55px">
        <div class="pull-left">
          <font size="4" style="line-height: 35px">管理用户</font>
        </div>
        <div class="pull-right">
          <el-button type="primary" @click="openSave">新增</el-button>
        </div>
      </div>
      <!-- 主列表 -->
      <div class="panel-body">
        <div style="display: flex;justify-content: space-between;">
          <div>
            <el-input clearable v-model="query.account" placeholder="请输入用户名" style="width: 200px"/>
            <el-input clearable v-model="query.email" placeholder="请输入邮箱" style="width: 200px; margin-left: 10px;"/>
            <el-select clearable v-model="query.status" placeholder="请选择封禁状态" style="width: 200px; margin-left: 10px;">
              <el-option :value="1" label="启用"></el-option>
              <el-option :value="0" label="禁用"></el-option>
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
          <el-table-column prop="userId" label="用户ID" align="center" width="100"></el-table-column>
          <el-table-column prop="account" label="用户名" width="200" align="center"></el-table-column>
          <el-table-column prop="email" label="邮箱" width="200" align="center"></el-table-column>
          <el-table-column prop="tel" label="手机号" width="150" align="center"></el-table-column>
          <el-table-column prop="status" label="启/禁用" width="150" align="center">
            <template slot-scope="{row}">
              <div>
                <span v-if="row.status === 0">禁用</span>
                <span v-if="row.status === 1">启用</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="注册时间" width="180" align="center"></el-table-column>
          <el-table-column label="操作" fixed="right" align="center" width="240">
            <template slot-scope="{row}">
              <div style="display: flex; justify-content: space-around">
                <div v-if="row.status === 0" @click="openStatus(row, 1)" style="cursor: pointer; color: #409eff;">启用</div>
                <div v-if="row.status === 1" @click="openStatus(row, 0)" style="cursor: pointer; color: #409eff;">禁用</div>
                <div @click="initPassword(row)" style="cursor: pointer; color: #409eff;">初始化密码</div>
                <div @click="openUpdate(row)" style="cursor: pointer; color: #409eff;">编辑</div>
                <div @click="openDelete(row)" style="cursor: pointer; color: #409eff;">删除</div>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <user-submit :id="currentId" v-if="showSubmit" @close="() => {showSubmit = false; this.onLoad()}"/>
  </div>
</template>

<script>
  import User from "../../api/User";
  import UserSubmit from "./user/UserSubmit";
  export default {
    name: "UserManager",
    components: {UserSubmit},
    data() {
      return {
        data: [],
        query: {},
        showSubmit: false,
        currentId: null,
      }
    },
    methods: {
      initPassword(row) {
        this.$confirm(`确定初始化该用户密码吗?`,'提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: "warning",
        }).then(() => {
          row.password = 123456;
          return User.update(row.userId, row);
        }).then(() => {
          this.$message.success("初始化密码成功， 密码为123456");
        })
      },
      openStatus(row, status) {
        let confirmMessage = status === 1 ? '确定启用该用户吗?' : '确定禁用该用户吗';
        let successMessage = status === 1 ? '启用成功' : '禁用成功';
        this.$confirm(confirmMessage,'提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: "warning",
        }).then(() => {
          row.status = status;
          return User.update(row.userId, row);
        }).then(() => {
          this.$message.success(successMessage);
          this.onLoad();
        })
      },
      openDelete(row) {
        this.$confirm(`确定删除该数据吗?`,'提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: "warning",
        }).then(() => {
          return User.delete(row.userId);
        }).then(() => {
          this.$message.success("删除成功");
          this.onLoad();
        })
      },
      openUpdate(data) {
        this.currentId = data.userId;
        this.showSubmit = true;
      },
      openSave() {
        this.currentId = null;
        this.showSubmit = true;
      },
      onLoad() {
        User.list(this.query).then(res => {
          let data = res.data.data;
          this.data = data;

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