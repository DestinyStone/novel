<template>
  <el-dialog
      :title="title"
      :visible="true"
      width="30%"
      :before-close="handlerClose">
    <div>
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" class="demo-ruleForm">
        <el-form-item label="用户名" prop="account">
          <el-input :disabled="title === '编辑用户'" v-model="form.account"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="tel">
          <el-input v-model="form.tel"></el-input>
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
  import User from "../../../api/User";
  export default {
    name: "UserSubmit",
    props: {
      id: {
        type: Number,
      }
    },
    data() {
      return {
        form: {},
        rules: {
          account: [{ required: true, message: '请填写用户名', trigger: 'change' }],
          email: [{ required: true, message: '请填写邮箱', trigger: 'change' }],
          tel: [{ required: true, message: '请填写手机号', trigger: 'change' }],
        },
      }
    },
    computed: {
      title() {
        if (this.id) {
          return "编辑用户";
        }
        return "新增用户";
      }
    },
    methods: {
      submitHandler() {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            if (this.id) {
              User.update(this.id, this.form).then(() => {
                this.$message.success("操作成功");
                this.handlerClose();
              });
              return;
            }

            this.form.password = "123456";
            User.save(this.form).then(() => {
              this.$message.success("新增用户成功， 初始密码为123456");
              this.handlerClose();
            });
          }
        });
      },
      init() {
        if (this.id) {
          User.getById(this.id).then(res => {
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