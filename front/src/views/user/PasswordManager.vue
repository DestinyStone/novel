<template>
  <div class="container">
    <div class="row">
      <div id="left-item-wrapper"></div>
      <!-- 右侧主体 -->
      <div class="col-md-10">
        <!-- 上半部分 -->
        <div class="panel panel-default">
          <div class="panel-heading">
            <font size="3"> <span class="glyphicon glyphicon-user"></span>
              个人信息修改
            </font>
          </div>
        </div>
        <!-- 下半部分 -->
        <div class="panel panel-default">
          <!-- 头部 -->
          <div class="panel-heading" style="height: 55px">
            <div class="pull-left">
              <font size="4" style="line-height: 35px">个人信息修改</font>
            </div>
            <div class="pull-right">
              <button class="btn btn-success" onclick="window.location.reload()">
                <span class="glyphicon glyphicon-refresh"></span>刷&nbsp;新
              </button>
            </div>
          </div>
          <!-- 主列表 -->
          <div class="panel-body">
            <div class="modal-body">
              <form action="#" onsubmit="return false" method="post">
                <div class="form-group has-feedback" id="current-password-div">
                  <label for="current-password">当前密码</label>
                  <div class="input-group">
                    <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
                    <input id="current-password" name="current-password" class="form-control" maxlength="20" type="password">
                  </div>
                  <span style="color: red; display: none;" class="tips"></span> <span style="display: none;" class=" glyphicon glyphicon-remove form-control-feedback"></span>
                  <span style="display: none;" class="glyphicon glyphicon-ok form-control-feedback"></span>
                </div>
                <div class="form-group has-feedback" id="new-password-div">
                  <label for="new-password">新密码</label>
                  <div class="input-group">
                    <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
                    <input id="new-password" name="new-password" class="form-control" maxlength="20" type="password">
                  </div>
                  <span style="color: red; display: none;" class="tips"></span> <span style="display: none;" class=" glyphicon glyphicon-remove form-control-feedback"></span>
                  <span style="display: none;" class="glyphicon glyphicon-ok form-control-feedback"></span>
                </div>
                <div class="text-center">
                  <button class="btn btn-primary" type="submit" @click="uploadPasswordHandler">确认修改</button>
                  <button class="btn btn-danger" data-dismiss="modal" style="margin-left: 10px;">取&nbsp;&nbsp;消</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import $ from "jquery"
  import User from "../../api/User";
  export default {
    name: "PasswordManager",
    data() {
      return {
        checkRegisterTel: false,
        checkRegisterEmail: false,
      }
    },
    methods: {
      // 成功函数
      success(obj) {
        obj.removeClass('has-error').addClass('has-success');
        obj.find('.tips').hide();
        obj.find('.glyphicon-ok').show();
        obj.find('.glyphicon-remove').hide();
      },
      // 失败函数
      fail(obj, msg) {
        obj.removeClass('has-success').addClass('has-error');
        obj.find('.glyphicon-remove').show();
        obj.find('.glyphicon-ok').hide();
        obj.find('.tips').text(msg).show();
      },
      uploadPasswordHandler() {
        let data = {};
        data.currentPassword = $("#current-password").val();
        data.newPassword = $("#new-password").val();

        if (!data.currentPassword) {
          this.fail($("#current-password-div"))
          return;
        }

        if (!data.newPassword) {
          this.fail($("#new-password-div"))
          return;
        }

        User.updatePassword(data).then(() => {
          this.$message.success("密码修改成功，请重新登录");
          setTimeout(() => {
            window.location.href = "/#/";
          }, 1000);
        })

      },
      init() {
      }
    },
    created() {
      this.init();
    }
  }
</script>

<style scoped>

</style>