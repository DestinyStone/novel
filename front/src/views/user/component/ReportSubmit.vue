<template>
  <el-dialog
      :title="title"
      :visible="true"
      width="800px"
      :before-close="handlerClose">
    <div>
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" class="demo-ruleForm">
        <el-form-item label="小说ID" prop="novelId">
          <el-input disabled v-model="form.novelId"></el-input>
        </el-form-item>
        <el-form-item label="小说标题" prop="title">
          <el-input disabled v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="评论用户" prop="account">
          <el-input disabled v-model="form.account"></el-input>
        </el-form-item>
        <el-form-item label="举报评论" prop="content">
          <el-input disabled v-model="form.content"></el-input>
        </el-form-item>
        <el-form-item label="举报原因" prop="remark">
          <el-input type="textarea" v-model="form.remark" :rows="4"></el-input>
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
  import Report from "../../../api/Report";

  export default {
    name: "ReportSubmit",
    props: {
      data: {
        type: Object,
        default() {
          return {}
        }
      }
    },
    computed: {
      title() {
        return "评论举报";
      }
    },
    data() {
      return {
        form: {},
        rules: {
          novelId: [{ required: true, message: '', trigger: 'change' }],
          title: [{ required: true, message: '', trigger: 'change' }],
          account: [{ required: true, message: '', trigger: 'change' }],
          content: [{ required: true, message: '', trigger: 'change' }],
          remark: [{ required: true, message: '请填写举报原因', trigger: 'change' }],
        },
      }
    },
    methods: {

      submitHandler() {
        this.$refs["form"].validate((valid) => {
            if (!valid) {
              return;
            }
          Report.submit(this.form).then(() => {
            this.$message.success("举报成功， 请等待管理员处理");
            this.handlerClose();
          })
        });
      },
      init() {
        this.$set(this.form, 'novelId', Number(this.data.novelId));
        this.$set(this.form, 'title', this.data.title);
        this.$set(this.form, 'content', this.data.content);
        this.$set(this.form, 'account', this.data.account);
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