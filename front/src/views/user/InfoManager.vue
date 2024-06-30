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
                <div class="form-group has-feedback" id="user-info-tel-div">
                  <label for="user-info-tel">手机号码</label>
                  <div class="input-group">
                    <span class="input-group-addon"><span class="glyphicon glyphicon-phone"></span></span>
                    <input id="user-info-tel" name="tel" class="form-control" placeholder="请输入手机号码" maxlength="11" type="text">
                  </div>
                  <span style="color: red; display: none;" class="tips"></span> <span style="display: none;" class="glyphicon glyphicon-remove form-control-feedback"></span>
                  <span style="display: none;" class="glyphicon glyphicon-ok form-control-feedback"></span>
                </div>

                <div class="form-group has-feedback" id="user-info-email-div">
                  <label for="user-info-email">邮箱</label>
                  <div class="input-group">
                    <span class="input-group-addon"><span class="glyphicon glyphicon-envelope"></span></span>
                    <input id="user-info-email" name="email" class="form-control" type="email" placeholder="例如:123@123.com">
                  </div>
                  <span style="color: red; display: none;" class="tips"></span> <span style="display: none;" class="glyphicon glyphicon-remove form-control-feedback"></span>
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
  import Auth from "../../api/Auth";
  import $ from "jquery"
  import User from "../../api/User";
  export default {
    name: "InfoManager",
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
        data.tel = $("#user-info-tel").val();
        data.email = $("#user-info-email").val();

        if (this.checkRegisterTel) {
          this.success($("#user-info-tel-div"));
        } else {
          this.fail($("#user-info-tel-div"));
        }

        if (this.checkRegisterEmail) {
          this.success($("#user-info-email-div"));
        } else {
          this.fail($("#user-info-email-div"));
        }
        if (this.checkRegisterTel && this.checkRegisterEmail) {
          User.updateInfo(data).then(() => {
            this.$message.success("更新成功");
          })
        }
      },
      init() {
        // 手机号码
        let regTel = /^[0-9]{11}$/;
        $(document).ready(() => {
          $('#user-info-tel-div').find("#user-info-tel").change(() => {
            let value = $("#user-info-tel").val();
            if (regTel.test(value)) {
              this.success($("#user-info-tel-div"));
              this.checkRegisterTel = true;
            } else {
              this.fail($("#user-info-tel-div"), '手机号码只能为11位数字');
              this.checkRegisterTel = false;
            }
          });
        });

        // 邮箱
        let regEmail = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        $(document).ready(() => {
          $('#user-info-email-div').find("#user-info-email").change(() => {
            let value = $("#user-info-email").val();
            if (regEmail.test(value)) {
              this.success($("#user-info-email-div"));
              this.checkRegisterEmail = true;
            } else {
              this.fail($("#user-info-email-div"), '邮箱格式错误,正确格式如:123312@qq.com');
              this.checkRegisterEmail = false;
            }
          });

        });
      }
    },
    created() {
      this.init();
      Auth.current().then(res => {
        let data = res.data.data;
        User.getById(data.id).then(userRes => {
          let user = userRes.data.data;
          $("#user-info-tel").val(user.tel);
          $("#user-info-email").val(user.email);
        });

        this.checkRegisterTel = true;
        this.checkRegisterEmail = true;
      })
    }
  }
</script>

<style scoped>

</style>