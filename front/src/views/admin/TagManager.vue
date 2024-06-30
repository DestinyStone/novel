<template>
  <div>
    <!-- 上半部分 -->
    <div class="panel panel-default">
      <div class="panel-heading">
        <font size="3"> <span class="glyphicon glyphicon-user"></span>
          分类管理
        </font>
      </div>
    </div>
    <!-- 下半部分 -->
    <div class="panel panel-default">
      <!-- 头部 -->
      <div class="panel-heading" style="height: 55px">
        <div class="pull-left">
          <font size="4" style="line-height: 35px">分类管理</font>
        </div>
        <div class="pull-right">
          <el-button type="primary" @click="openSave">新增</el-button>
        </div>
      </div>
      <!-- 主列表 -->
      <div class="panel-body">
        <div style="display: flex;justify-content: space-between;">
          <div>
            <el-input clearable v-model="query.name" placeholder="请输入标签名称" style="width: 200px"/>
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
          <el-table-column prop="id" label="标签ID" align="center" width="100"></el-table-column>
          <el-table-column prop="name" label="标签名称"  align="center"></el-table-column>
          <el-table-column label="操作" fixed="right" align="center" width="200">
            <template slot-scope="{row}">
              <div style="display: flex; justify-content: space-around">
                <div @click="openUpdate(row)" style="cursor: pointer; color: #409eff;">编辑</div>
                <div @click="openDelete(row)" style="cursor: pointer; color: #409eff;">删除</div>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <tag-submit :id="currentId" v-if="showSubmit" @close="() => {showSubmit = false; this.onLoad()}"/>
  </div>
</template>

<script>
  import Tag from "../../api/Tag";
  import TagSubmit from "./tag/TagSubmit";

  export default {
    name: "TagManager",
    components: {TagSubmit},
    data() {
      return {
        data: [],
        query: {},
        showSubmit: false,
        currentId: null,
      }
    },
    methods: {
      openDelete(data) {
        this.$confirm(`确定删除该数据吗?`,'提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: "warning",
        }).then(() => {
          return Tag.delete(data.id);
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
        Tag.list(this.query).then(res => {
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