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
                <li :class="{'active' : active === 'manager-user'}">
                  <a style="cursor: pointer" @click="() => {active = 'manager-user'}">
                    <span class="glyphicon glyphicon-user"></span> 用户管理
                  </a>
                </li>
                <li :class="{'active' : active === 'manager-tag'}">
                  <a style="cursor: pointer" @click="() => {active = 'manager-tag'}">
                    <span class="glyphicon glyphicon-th-large"></span> 标签管理
                  </a>
                </li>
                <li :class="{'active' : active === 'manager-novel'}">
                  <a style="cursor: pointer" @click="() => {active = 'manager-novel'}">
                    <span class="glyphicon glyphicon glyphicon-leaf"></span> 小说管理
                  </a>
                </li>
                <li :class="{'active' : active === 'manager-comment'}">
                  <a style="cursor: pointer" @click="() => {active = 'manager-comment'}">
                    <span class="glyphicon glyphicon glyphicon-user"></span> 评论管理
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <!-- 右侧主体 -->
        <div class="col-md-10">
          <user-manager v-if="active === 'manager-user'"/>
          <tag-manager v-if="active === 'manager-tag'"/>
          <novel-manager v-if="active === 'manager-novel'"/>
          <comment-manager v-if="active === 'manager-comment'"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import HeaderView from "../common/HeaderView";
  import UserManager from "./UserManager";
  import TagManager from "./TagManager";
  import NovelManager from "./NovelManager";
  import CommentManager from "./CommentManager";
  export default {
    name: "AdminManager",
    components: {CommentManager, NovelManager, TagManager, UserManager, HeaderView},
    data() {
      return {
        active: "manager-user",
      }
    }
  }
</script>

<style scoped>

</style>