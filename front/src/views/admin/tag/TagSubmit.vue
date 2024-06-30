<template>
  <el-dialog
      :title="title"
      :visible="true"
      width="30%"
      :before-close="handlerClose">
    <div>
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" class="demo-ruleForm">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="form.name"></el-input>
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
  export default {
    name: "TagSubmit",
    props: {
      id: {
        type: Number,
      }
    },
    computed: {
      title() {
        if (this.id) {
          return "编辑标签";
        }
        return "新增标签";
      }
    },
    data() {
      return {
        form: {},
        rules: {
          name: [{ required: true, message: '请填写标签名称', trigger: 'change' }],
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
            Tag.update(this.id, this.form).then(() => {
              this.$message.success("操作成功");
              this.handlerClose();
            })
            return;
          }
          Tag.save(this.form).then(() => {
            this.$message.success("操作成功");
            this.handlerClose();
          })
        });
      },
      init() {

        if (this.id) {
          Tag.getById(this.id).then(res => {
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