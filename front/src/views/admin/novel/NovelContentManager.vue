<template>
  <el-dialog
      title="章节管理"
      :visible="true"
      width="1200px"
      :before-close="handlerClose">
    <div>
      <el-button type="primary" @click="handlerSaveManager">新增</el-button>
      <el-table
          height="300"
          :data="data"
          border
          style="width: 100%; margin-top: 10px;">
        <el-table-column type="index" label="序号" align="center" width="100"></el-table-column>
        <el-table-column prop="title" label="章节" align="center"></el-table-column>
        <el-table-column prop="sort" label="排序号" align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template slot-scope="{row}">
            <div style="display: flex; justify-content: space-around">
              <div @click="openUpdateManager(row)" style="cursor: pointer; color: #409eff;">编辑</div>
              <div @click="openDeleteManager(row)" style="cursor: pointer; color: #409eff;">删除</div>
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
    <novel-content-submit :novel-id="id" :id="currentId" v-if="showSubmit" @close="() => {showSubmit = false; this.onLoad()}"/>
  </el-dialog>
</template>

<script>
  import NovelContent from "../../../api/NovelContent";
  import NovelContentSubmit from "./NovelContentSubmit";
  export default {
    name: "NovelContentManager",
    components: {NovelContentSubmit},
    props: {
      id: {
        type: Number,
      }
    },
    computed: {
      title() {
        if (this.id) {
          return "编辑小说";
        }
        return "新增小说";
      }
    },
    data() {
      return {
        page: {
          current: 1,
          size: 5,
          total: 0,
        },
        data: [],
        currentId: null,
        showSubmit: false,
      }
    },
    methods: {
      openDeleteManager(data) {
        this.$confirm(`确定删除该数据吗?`,'提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: "warning",
        }).then(() => {
          return NovelContent.delete(data.id);
        }).then(() => {
          this.$message.success("删除成功");
          this.onLoad();
        })
      },
      openUpdateManager(data) {
        this.currentId = data.id;
        this.showSubmit = true;
      },
      handlerSaveManager() {
        this.currentId = null;
        this.showSubmit = true;
      },
      onLoad() {
        NovelContent.page({novelId: this.id}, this.page).then(res => {
          let data = res.data.data;
          this.data = data.records;
          this.page.total = data.total;
        })
      },
      handlerClose() {
        this.$emit("close");
      },
    },
    mounted() {
      this.onLoad();
    }
  }
</script>

<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>