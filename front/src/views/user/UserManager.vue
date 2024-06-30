<template>
  <div>
    <div style="height: 80px;">
      <header-view/>
    </div>
    <div class="container">
      <div class="row">
        <div class="col-md-2">
          <div class="panel panel-default">
            <div class="panel-heading">
              <ul class="nav nav-pills nav-stacked">
                <li class="disabled">
                  <a href="#">
                    <font size="3" style="font-weight: bold;">
                      <span class="glyphicon glyphicon-th-large"></span>管理中心
                    </font>
                  </a>
                </li>
              </ul>
            </div>
            <div class="panel-body">
              <ul id="item-urls" class="nav nav-pills nav-stacked">
                <li :class="{'active' : active === 'manager-log'}">
                  <a style="cursor: pointer" @click="() => {active = 'manager-log'}">
                    <span class="glyphicon glyphicon-user"></span> 阅读历史
                  </a>
                </li>
                <li :class="{'active' : active === 'manager-info'}">
                  <a style="cursor: pointer" @click="() => {active = 'manager-info'}">
                    <span class="glyphicon glyphicon-user"></span> 个人信息修改
                  </a>
                </li>
                <li :class="{'active' : active === 'manager-password'}">
                  <a style="cursor: pointer" @click="() => {active = 'manager-password'}">
                    <span class="glyphicon glyphicon-user"></span> 密码修改
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <!-- 右侧主体 -->
        <div class="col-md-10">
          <manager-log v-if="active === 'manager-log'"/>
          <info-manager v-if="active === 'manager-info'"/>
          <password-manager  v-if="active === 'manager-password'"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import HeaderView from "../common/HeaderView";
  import InfoManager from "./InfoManager";
  import PasswordManager from "./PasswordManager";
  import ManagerLog from "./ManagerLog";

  export default {
    name: "UserManager",
    components: {ManagerLog, PasswordManager, InfoManager, HeaderView},
    data() {
      return {
        active: "manager-log",
      }
    }
  }
</script>

<style scoped>

</style>