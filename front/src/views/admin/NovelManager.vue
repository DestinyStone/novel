<template>
  <div>
    <!-- 上半部分 -->
    <div class="panel panel-default">
      <div class="panel-heading">
        <font size="3"> <span class="glyphicon glyphicon-user"></span>
          小说管理
        </font>
      </div>
    </div>
    <!-- 下半部分 -->
    <div class="panel panel-default">
      <!-- 头部 -->
      <div class="panel-heading" style="height: 55px">
        <div class="pull-left">
          <font size="4" style="line-height: 35px">小说管理</font>
        </div>
        <div class="pull-right">
          <el-button type="primary" @click="openSave">新增</el-button>
        </div>
      </div>
      <!-- 主列表 -->
      <div class="panel-body">
        <div style="display: flex;justify-content: space-between;">
          <div>
            <el-select clearable v-model="query.type" placeholder="请选择类型" style="width: 200px; margin-left: 10px;">
              <el-option label="玄幻魔法" value="玄幻魔法"></el-option>
              <el-option label="武侠修真" value="武侠修真"></el-option>
              <el-option label="都市言情" value="都市言情"></el-option>
              <el-option label="历史军事" value="历史军事"></el-option>
              <el-option label="侦探推理" value="侦探推理"></el-option>
              <el-option label="网游动漫" value="网游动漫"></el-option>
              <el-option label="科幻灵异" value="科幻灵异"></el-option>
            </el-select>
            <el-input clearable v-model="query.name" placeholder="请输入小说名称" style="width: 200px; margin-left: 10px;"/>
            <el-input clearable v-model="query.author" placeholder="请输入作者" style="width: 200px; margin-left: 10px;"/>
          </div>
          <div>
            <el-button @click="onLoad">查询</el-button>
          </div>
        </div>
        <div style="padding: 10px 0;"></div>
        <el-table
            height="420"
            :data="data"
            border
            style="width: 100%">
          <el-table-column type="index" label="序号" align="center" width="100"></el-table-column>
          <el-table-column prop="type" label="类型" align="center" width="100"></el-table-column>
          <el-table-column prop="name" label="小说名称" align="center" width="200"></el-table-column>
          <el-table-column prop="author" label="作者" align="center" width="150"></el-table-column>
          <el-table-column prop="state" label="是否连载" align="center" width="100"></el-table-column>
          <el-table-column prop="imgUrl" label="封面" align="center" width="100">
            <template slot-scope="{row}">
              <el-image style="height: 40px;" :src="row.imgUrl"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" align="center" width="200">
            <template slot-scope="{row}">
              <div style="display: flex; justify-content: space-around">
                <div @click="openUpdate(row)" style="cursor: pointer; color: #409eff;">编辑</div>
                <div @click="openDelete(row)" style="cursor: pointer; color: #409eff;">删除</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="管理" fixed="right" align="center" width="100">
            <template slot-scope="{row}">
              <div style="display: flex; justify-content: space-around">
                <div @click="openManager(row)" style="cursor: pointer; color: #409eff;">章节管理</div>
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
    <novel-submit :id="currentId" v-if="showSubmit" @close="() => {showSubmit = false; this.onLoad()}"/>
    <novel-content-manager :id="currentId" v-if="showManager" @close="() => {showManager = false; this.onLoad()}"/>
  </div>
</template>

<script>
  import Novel from "../../api/Novel";
  import NovelSubmit from "./novel/NovelSubmit";
  import NovelContentManager from "./novel/NovelContentManager";

  export default {
    name: "NovelManager",
    components: {NovelContentManager, NovelSubmit},
    data() {
      return {
        page: {
          current: 1,
          size: 5,
          total: 0,
        },
        data: [],
        query: {},
        showSubmit: false,
        currentId: null,
        showManager: false,
      }
    },
    methods: {
      openManager(data) {
        this.currentId = data.id;
        this.showManager = true;
      },
      openDelete(data) {
        this.$confirm(`确定删除该数据吗?`,'提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: "warning",
        }).then(() => {
          return Novel.delete(data.id);
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
        Novel.page(this.query, this.page).then(res => {
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