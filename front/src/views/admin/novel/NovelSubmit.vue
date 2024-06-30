<template>
  <el-dialog
      :title="title"
      :visible="true"
      width="1000px"
      :before-close="handlerClose">
    <div>
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" class="demo-ruleForm">
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%;">
            <el-option label="玄幻魔法" value="玄幻魔法"></el-option>
            <el-option label="武侠修真" value="武侠修真"></el-option>
            <el-option label="都市言情" value="都市言情"></el-option>
            <el-option label="历史军事" value="历史军事"></el-option>
            <el-option label="侦探推理" value="侦探推理"></el-option>
            <el-option label="网游动漫" value="网游动漫"></el-option>
            <el-option label="科幻灵异" value="科幻灵异"></el-option>
            <el-option label="完本" value="完本"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标签" prop="tagList">
          <div>
            <el-select v-model="currentTag" placeholder="请选择标签" style="width: 200px;">
              <el-option v-for="item in tags" :key="'type' + item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-button type="primary" style="margin-left: 10px; width: 100px;" @click="handlerAddTag">添加</el-button>
          </div>
          <div style="border: 1px solid #ccc; border-radius: 10px; min-height: 100px; margin-top: 10px; display: flex; flex-flow: wrap; box-sizing: border-box">
            <el-tag
                style="margin: 5px;"
                @close="handleCloseTag(tag)"
                v-for="tag in form.tagList"
                :key="tag.id"
                closable>
              {{tag.name}}
            </el-tag>
          </div>
        </el-form-item>
        <el-form-item label="小说名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author"></el-input>
        </el-form-item>
        <el-form-item label="是否连载" prop="state">
          <el-radio value="连载中" label="连载中" v-model="form.state"></el-radio>
          <el-radio value="完结" label="完结" v-model="form.state"></el-radio>
        </el-form-item>
        <el-form-item label="描述" prop="brief">
          <el-input v-model="form.brief" type="textarea" :rows="4" resize="none"></el-input>
        </el-form-item>
        <el-form-item label="封面" prop="imgUrl">
          <el-upload
              class="avatar-uploader"
              action="/api/novel/upload"
              :show-file-list="false"
              :on-success="handlerUpload">
            <img v-if="form.imgUrl" :src="form.imgUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handlerClose">取 消</el-button>
      <el-button type="primary" @click="submitHandler">提 交</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import Tag from "../../../api/Tag";
  import Novel from "../../../api/Novel";
  export default {
    name: "NovelSubmit",
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
        form: {
          tagList: [],
        },
        tags: [],
        currentTag: null,
        rules: {
          type: [{ required: true, message: '请选择类型', trigger: 'change' }],
          tagList: [{ required: true, message: '请选择标签', trigger: 'change' }],
          name: [{ required: true, message: '请填写小说名称', trigger: 'change' }],
          author: [{ required: true, message: '请填写作者', trigger: 'change' }],
          state: [{ required: true, message: '请选择连载', trigger: 'change' }],
          brief: [{ required: true, message: '请填写描述', trigger: 'change' }],
          imgUrl: [{ required: true, message: '请上传封面', trigger: 'change' }],
        },
      }
    },
    methods: {
      handleCloseTag(tag) {
        let index = this.form.tagList.map(item => item.id).indexOf(tag.id);
        this.form.tagList.splice(index, 1);
      },
      handlerAddTag() {
        if (!this.currentTag) {
          this.$message.warning("请选择标签");
          return;
        }
        if (!this.form.tagList) {
          this.$set(this.form, 'tagList', []);
        }

        for(let item of this.form.tagList) {
          if (item.id === this.currentTag) {
            this.$message.warning("标签已存在");
            return;
          }
        }

        for(let item of this.tags) {
          if (item.id === this.currentTag) {
            this.form.tagList.push(item);
          }
        }
        this.currentTag = null;
      },
      handlerUpload(res) {
        let data = res.data;
        this.$set(this.form, 'imgUrl', data);
      },
      submitHandler() {
        this.$refs["form"].validate((valid) => {
          if (!valid) {
            return;
          }
          this.form.tags = this.form.tagList.map(item => item.id).join(",");
          if (this.id) {
            Novel.update(this.id, this.form).then(() => {
              this.$message.success("操作成功");
              this.handlerClose();
            })
            return;
          }
          Novel.save(this.form).then(() => {
            this.$message.success("操作成功");
            this.handlerClose();
          })
        });
      },
      init() {
        if (this.id) {
          Novel.getById(this.id).then(res => {
            this.form = res.data.data;
          })
        }
      },
      handlerClose() {
        this.$emit("close");
      },
      loadTags() {
        Tag.list().then(res => {
          let data = res.data.data;
          this.tags = data;
        })
      }
    },
    mounted() {
      this.init();
      this.loadTags();
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