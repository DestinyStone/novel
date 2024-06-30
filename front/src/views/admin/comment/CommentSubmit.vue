<template>
  <el-dialog
      :title="title"
      :visible="true"
      width="30%"
      :before-close="handlerClose">
    <div>
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" class="demo-ruleForm">
        <el-form-item label="评论内容" prop="content">
          <el-input v-model="form.content"></el-input>
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
  import Comment from "../../../api/Comment";
  export default {
    name: "CommentSubmit",
    props: {
      id: {
        type: Number,
      }
    },
    computed: {
      title() {
        if (this.id) {
          return "编辑评论";
        }
        return "新增评论";
      }
    },
    data() {
      return {
        form: {},
        rules: {
          content: [{ required: true, message: '请填写评论', trigger: 'change' }],
        },
      }
    },
    methods: {
      submitHandler() {
        this.$refs["form"].validate((valid) => {
            if (!valid) {
              return;
            }
          if (this.id) {
            Comment.update(this.id, this.form).then(() => {
              this.$message.success("操作成功");
              this.handlerClose();
            })
            return;
          }

        });
      },
      init() {

        if (this.id) {
          Comment.getById(this.id).then(res => {
            this.form = res.data.data;
          })
        }
      },
      handlerClose() {
        this.$emit("close");
      }
    },
    mounted() {
      this.init();
    }
  }
</script>

<style scoped>

</style>