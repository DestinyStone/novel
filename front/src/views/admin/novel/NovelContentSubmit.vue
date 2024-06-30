<template>
  <el-dialog
      :title="title"
      append-to-body
      :visible="true"
      width="1000px"
      :before-close="handlerClose">
    <el-form :model="form" :rules="rules" ref="form" label-width="100px" class="demo-ruleForm">
      <el-form-item label="排序号" prop="sort">
        <el-input v-model="form.sort" type="number"></el-input>
      </el-form-item>
      <el-form-item label="章节" prop="title">
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input :rows="14" type="textarea" resize="none" v-model="form.content"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handlerClose">取 消</el-button>
      <el-button type="primary" @click="submitHandler">提 交</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import NovelContent from "../../../api/NovelContent";
  export default {
    name: "NovelContentSubmit",
    props: {
      novelId: {
        type: Number,
      },
      id: {
        type: Number,
      }
    },
    computed: {
      title() {
        if (this.id) {
          return "编辑章节";
        }
        return "新增章节";
      }
    },
    data() {
      return {
        form: {},
        rules: {
          sort: [{ required: true, message: '请填写排序号', trigger: 'change' }],
          content: [{ required: true, message: '请填写内容', trigger: 'change' }],
          title: [{ required: true, message: '请填写章节', trigger: 'change' }],
        },
      }
    },
    methods: {
      submitHandler() {
        this.$refs["form"].validate((valid) => {
          if (!valid) {
            return;
          }
          this.form.novelId = this.novelId;
          if (this.id) {
            NovelContent.update(this.id, this.form).then(() => {
              this.$message.success("操作成功");
              this.handlerClose();
            })
            return;
          }
          NovelContent.save(this.form).then(() => {
            this.$message.success("操作成功");
            this.handlerClose();
          })
        });
      },
      init() {
        if (this.id) {
          NovelContent.getById(this.id).then(res => {
            this.form = res.data.data;
          })
        }
      },
      handlerClose() {
        this.$emit("close");
      },
    },
    mounted() {
      this.init();
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