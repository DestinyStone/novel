<template>
  <div id="book-detail" style="height: 100%;">
    <header-view/>
    <div class="container" style="padding-top: 100px;">
      <!-- 书籍详细信息 -->
      <div   class="book-top clearfix">
        <div  class="book-main fl">
          <div class="book-detail clearfix" style="display: flex;">
            <div class="book-img fl">
              <img style="border-radius: 10px;" :src="data.imgUrl" width="200" height="264" >
            </div>
            <div class="book-info" style="padding-left: 20px; position:relative;">
              <div class="book-name" style="font-size: 30px; font-weight: 700;">{{data.name}}</div>
              <div class="book-label" style="display: flex; padding: 10px 0;">
                <div class="state">{{data.state}}</div>
                <div class="state label" style="margin-left: 10px;">{{data.type}}</div>
              </div>
              <div class="nums">
                <span>字数: {{data.number}} </span>
                <span style="margin-left: 10px;">总点击: {{data.views}} </span>
                <span style="margin-left: 10px;">收藏数: {{data.collectCount}} </span>
                <span style="margin-left: 10px;">评分: {{data.score}} </span>
              </div>
              <div style="padding: 15px 0;">
                {{data.brief}}
                <br>
              </div>
              <div style="display: flex">
                <div style="color: #8c8c8c;">我的评分：</div>
                <el-rate
                    :disabled="!isAccessScore"
                    :allow-half="true"
                    :max="10"
                    v-model="score"
                    show-score
                    text-color="#ff9900"
                    score-template="{value}">
                </el-rate>
                <div v-if="isAccessScore" @click="scoreHandler" style="margin-left: 10px; cursor: pointer; background: #E6A23C" class="state">打分</div>
              </div>
              <div style="color: #8c8c8c; margin-top: 5px; ">
                <div v-if="!process">
                  未阅读过当前小说~
                </div>
                <div v-else style="display: flex;">
                  <div>上一次阅读：<span class="detail-btn" style="cursor: pointer">{{process.title}}</span></div>
                  <div style="margin-left: 20px;">阅读进度： <span style="cursor: pointer">{{process.process}}%</span></div>
                </div>
              </div>
              <div class="btn-group" style="position:absolute; bottom: 0; display: flex;">
                <div class="btn read-btn" @click="startRead">开始阅读</div>
                <div @click="collect" v-if="!isCollect" class="btn read-btn" style="background: #fff; border: 1px solid #E6A23C; color: #E6A23C"><i class="el-icon-star-off"/>加入书架</div>
                <div @click="unCollect" v-else class="btn read-btn" style="background: #fff; border: 1px solid #E6A23C; color: #E6A23C"><i class="el-icon-star-on"/>移出书架</div>
              </div>
            </div>
          </div>
          <el-divider></el-divider>
          <div class="book-new-chapter">
            <h4>最新章节</h4>
            <div style="display: flex; justify-content: space-between">
              <div class="tit" @click="toLast">{{data.newest}}</div>
              <div class="time"></div>
            </div>
          </div>
          <el-divider></el-divider>
          <div class="book-new-chapter">
            <h4>全部章节</h4>
            <div style="display: flex; flex-flow: wrap; max-height: 300px; overflow: auto">
              <div class="detail-btn" @click="toLink(item)" style="width: calc(100% / 3); cursor: pointer; padding: 10px 0;" v-for="item in contentData" :key="item.id">
                {{item.title}}
              </div>
            </div>
          </div>
          <el-divider></el-divider>
          <div class="book-new-chapter">
            <h4>评论区</h4>
            <div style="min-height: 400px; padding: 0 20px; max-height: 600px; overflow: auto">
              <div style="margin-bottom: 10px;" v-for="item in commentData" :key="item.id">
                <el-card>
                  <div style="position:relative;">
                    <div style="display: flex; justify-content: space-between">
                      <div>{{item.account}}：{{item.content}}</div>
                      <div style="cursor: pointer" @click="handlerReport(item)">举报</div>
                    </div>
                    <div style="text-align: right; font-size: 12px; color: #8c8c8c">{{item.createTime}}</div>
                  </div>
                </el-card>
              </div>

            </div>
            <div style="display: flex;">
              <el-input v-model="content" placeholder="请输入评论"/>
              <el-button type="primary" style="margin-left: 20px;" @click="sendComment">发表评论</el-button>
            </div>
          </div>
          <div style="height: 100px;"></div>
        </div>
      </div>
    </div>
    <report-submit :data="reportData" v-if="showReport" @close="() => {showReport = false;}"/>
  </div>
</template>

<script>
  import HeaderView from "../common/HeaderView";
  import Novel from "../../api/Novel";
  import NovelContent from "../../api/NovelContent";
  import Collect from "../../api/Collect";
  import ReadLog from "../../api/ReadLog";
  import Comment from "../../api/Comment";
  import Score from "../../api/Score";
  import ReportSubmit from "./component/ReportSubmit";
  export default {
    name: "BookDetail",
    components: {ReportSubmit, HeaderView},
    data() {
      return {
        score: null,
        data: {},
        contentData: [],
        isCollect: false,
        process: {},
        content: "",
        commentData: [],
        isAccessScore: false,
        reportData: {},
        showReport: false,
      }
    },
    methods: {
      handlerReport(item) {
        this.reportData = {};
        this.reportData.novelId = this.$route.query.id;
        this.reportData.title = this.data.name;
        this.reportData.content = item.content;
        this.reportData.account = item.account;
        this.showReport = true;
      },
      scoreHandler() {
        this.$confirm(`您给出的分值为${this.score}分, 提交后不可再次评分， 确定继续提交吗?`,'提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: "warning",
        }).then(() => {
          let data = {};
          data.level = this.score;
          data.novelId = this.$route.query.id;
          return Score.save(data);
        }).then(() => {
          this.isAccessScore = false;
          this.$message.success("提交成功");
        })
      },
      existScore() {
        Score.exist(this.$route.query.id).then(res => {
          let data = res.data.data;
          this.isAccessScore = data === -999;
          if (!this.isAccessScore) {
            this.score = data;
          }
        })
      },
      sendComment() {
        if (!this.content) {
          this.$message.warning("请输入评论");
          return;
        }

        let data = {};
        data.type = 0;
        data.content = this.content;
        data.novelId = this.$route.query.id;
        this.content = "";

        Comment.save(data).then(() => {
          this.$message.success("发表评论成功， 请等待管理员审核");
        })
      },
      getLog() {
        ReadLog.process(this.$route.query.id).then((res) => {
          this.process = res.data.data;
        })
      },
      unCollect() {
        Collect.delete(this.$route.query.id).then(() => {
          this.$message.success("移出书架成功");
          this.isCollect = false;
        })
      },
      collect() {
        let data = {};
        data.novelId = this.$route.query.id;
        Collect.save(data).then(() => {
          this.$message.success("加入书架成功");
          this.isCollect = true;
        })
      },
      toLast() {
        let one = this.contentData[this.contentData.length - 1];
        window.location.href = `/#/content?novelId=${this.$route.query.id}&id=${one.id}`
      },
      startRead() {
        if (this.contentData.length === 0) {
          this.$message.warning("没有发现章节");
          return;
        }
        let one = this.contentData[0];
        window.location.href = `/#/content?novelId=${this.$route.query.id}&id=${one.id}`
      },
      onLoad() {
        Novel.getById(this.$route.query.id).then(res => {
          this.data = res.data.data;
        })
      },
      onLoadContent() {
        NovelContent.list({novelId: this.$route.query.id}).then(res => {
          this.contentData = res.data.data;
        })
      },
      toLink(data) {
        window.location.href = `/#/content?novelId=${this.$route.query.id}&id=${data.id}`
      },
      existCollect() {
        Collect.exist(this.$route.query.id).then(res => {
          let data = res.data.data;
          this.isCollect = data === 1;
        })
      },
      onLoadComment() {
        Comment.listByNovelId(this.$route.query.id).then(res => {
          this.commentData = res.data.data;
        })
      }
    },
    created() {
      this.onLoad();
      this.onLoadContent();
      this.existCollect();
      this.getLog();
      this.onLoadComment();
      this.existScore();

      Novel.view(this.$route.query.id);
    }
  }
</script>

<style scoped>
  #book-detail .state {
    background: #02b389;
    border-radius: 2px;
    color: #fff;
    font-size: 12px;
    display: inline-block;
    padding: 0 5px;
    height: 20px;
    line-height: 20px;
  }
  #book-detail .label {
    background: #d32f2f;
  }
  #book-detail .btn {
    width: 220px;
    height: 36px;
    display: inline-block;
    margin-right: 7px;
  }
  #book-detail .read-btn {
    background: #d32f2f;
    color: #ffffff;
  }
  .detail-btn:hover {
    color: #d32f2f;
  }
  #book-detail .tit {
    cursor: pointer
  }
  #book-detail .tit:hover {
    color: #d32f2f;
  }
  #book-detail .time {
    font-size: 12px;
    color: #999;
    line-height: 25px;
  }
  .book-top {
    display: flex;
  }
  .book-main {
    flex: 1;
  }
</style>