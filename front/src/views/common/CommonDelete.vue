<template>
  <div id="delete" class="modal">
    <div class="modal-dialog">
      <div class="modal-content">

        <div class="modal-body"><button class="close" data-dismiss="modal"><span>&times;</span></button></div>
        <div class="modal-title"><h1 class="text-center" id="operatorTitle">删除</h1></div>

        <!-- 注册表单 -->
        <div class="modal-body">
          <form action="#" onsubmit="return false" method="post">
            <div class="form-group has-feedback">
              <label for="submit-id">ID</label>
              <div class="input-group">
                <span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
                <input :value="id" id="submit-id" disabled name="id" class="form-control" maxlength="20" type="text">
              </div>
              <span style="color: red; display: none;" class="tips"></span> <span style="display: none;" class=" glyphicon glyphicon-remove form-control-feedback"></span>
              <span style="display: none;" class="glyphicon glyphicon-ok form-control-feedback"></span>
            </div>

            <div class="text-right">
              <button class="btn btn-primary" type="button" @click.stop="handlerDelete">确认删除</button>
              <button class="btn btn-danger" type="button" @click.stop="handlerClose" style="margin-left: 10px;">取&nbsp;&nbsp;消</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import $ from "jquery";

  export default {

    name: "CommonDelete",
    props: {
      id: {
        type: Number,
      }
    },
    methods: {
      handlerDelete() {
        this.$emit("handler");
      },
      init() {
        $('#delete').css("display", "block");
      },
      handlerClose() {
        $('#delete').css("display", "none");
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