<template>
  <div id="home" style="height: 100%;">
    <header-view/>
    <div id="home-bg" style="background-image: url('/images/index/background1.png')">
      <div class="container">
        <novel-query style="padding-top: 10px;" @query="handlerQuery"/>
      </div>
    </div>
    <div class="container items">
      <div>
        <el-collapse value="all">
          <el-collapse-item title="一致性 Consistency" name="standard">
            <template slot="title">
              <div style="font-size: 25px; padding: 20px 0; text-align: left; display: flex; height: 25px; line-height: 25px;">
                <div style="height: 25px; width: 10px; background: #d32f2f;"></div>
                <div style="margin-left: 10px;">个性推荐</div>
              </div>
            </template>
            <book-list style="margin-top: 10px;" :data="standardData"/>
          </el-collapse-item>

          <el-collapse-item title="一致性 Consistency" name="fire">
            <template slot="title">
              <div style="font-size: 25px; padding: 20px 0; text-align: left; display: flex; height: 25px; line-height: 25px;">
                <div style="height: 25px; width: 10px; background: #d32f2f;"></div>
                <div style="margin-left: 10px;">热门榜</div>
              </div>
            </template>
            <book-list style="margin-top: 10px;" :data="fireData"/>
          </el-collapse-item>

          <el-collapse-item title="" name="news">
            <template slot="title">
              <div style="font-size: 25px; padding: 20px 0; text-align: left; display: flex; height: 25px; line-height: 25px;">
                <div style="height: 25px; width: 10px; background: #d32f2f;"></div>
                <div style="margin-left: 10px;">新作榜</div>
              </div>
            </template>
            <book-list style="margin-top: 10px;" :data="newsData"/>
          </el-collapse-item>

          <el-collapse-item title="" name="collect">
            <template slot="title">
              <div style="font-size: 25px; padding: 20px 0; text-align: left; display: flex; height: 25px; line-height: 25px;">
                <div style="height: 25px; width: 10px; background: #d32f2f;"></div>
                <div style="margin-left: 10px;">收藏榜</div>
              </div>
            </template>
            <book-list style="margin-top: 10px;" :data="collectData"/>
          </el-collapse-item>

          <el-collapse-item title="" name="score">
            <template slot="title">
              <div style="font-size: 25px; padding: 20px 0; text-align: left; display: flex; height: 25px; line-height: 25px;">
                <div style="height: 25px; width: 10px; background: #d32f2f;"></div>
                <div style="margin-left: 10px;">评分榜</div>
              </div>
            </template>
            <book-list style="margin-top: 10px;" :data="scoreData"/>
          </el-collapse-item>

          <el-collapse-item title="" name="all">
            <template slot="title">
              <div style="font-size: 25px; padding: 20px 0; text-align: left; display: flex; height: 25px; line-height: 25px;">
                <div style="height: 25px; width: 10px; background: #d32f2f;"></div>
                <div style="margin-left: 10px;">全部小说</div>
              </div>
            </template>
            <book-list style="margin-top: 10px;" :data="allData"/>
            <div style="display: flex; justify-content: end; padding: 10px 0;">
              <el-pagination
                  background
                  :current-page.sync="page.current"
                  :page-size="page.size"
                  layout="prev, pager, next"
                  @current-change="onLoad"
                  :total="page.total">
              </el-pagination>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>

    <div style="background: url(/images/index/background3.png); margin-top: 30px;">
      <div class="container">
        <div class="col-xs-2 "></div>
        <div class="col-xs-6 ">
          <br /> <br /> <font size="20"><strong>Novel & Bar</strong></font>
          <br /> <font size="5"><strong>享受小说，享受生活</strong></font> <br /> <br />
          <br />
        </div>
        <div class="col-xs-3 ">
          <br /> <br />
          <ul></ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import HeaderView from "../common/HeaderView";
  import Novel from "../../api/Novel";
  import NovelQuery from "./component/NovelQuery";
  import BookList from "./component/book-list";
  export default {
    name: "HomeView",
    components: {BookList, NovelQuery, HeaderView},
    data() {
      return {
        query: {},
        allData: [],
        fireData: [],
        newsData: [],
        collectData: [],
        scoreData: [],
        standardData: [],
        page: {
          current: 1,
          size: 6,
          total: 0,
        },
      }
    },
    methods: {
      handlerQuery(query) {
        this.query = query;
        this.onLoad();
      },
      onLoad() {
        Novel.page(this.query, this.page).then(res => {
          let data = res.data.data;
          this.allData = data.records;
          this.page.total = data.total;
        })

        Novel.fireList(this.query).then(res => {
          this.fireData = res.data.data;
        })

        Novel.newsList(this.query).then(res => {
          this.newsData = res.data.data;
        })

        Novel.collectList(this.query).then(res => {
          this.collectData = res.data.data;
        })

        Novel.scoreList(this.query).then(res => {
          this.scoreData = res.data.data;
        })

        Novel.standardList(this.query).then(res => {
          this.standardData = res.data.data;
        })
      }
    },
    created() {
      this.onLoad();
    }
  }
</script>

<style scoped>
  /*main区域居中*/
  .main {
    text-align: center;
    background-color: #fff;
    border-radius: 20px;
    width: 1000px;
    height: 400px;
    margin: 60px auto;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }

  .items {
    text-align: center;
    margin-left: auto;
    margin-right: auto;
  }

  .items img {
    width: 150px;
  }

  /*去除轮播两边阴影*/
  #cl #cr {
    background-image: none;
  }

  .item img {
    width: 100%;
    height: 400px;
    overflow: hidden;
  }
  #home-bg {
    position:relative;
    width: 100%;
    height: 220px;
    background-size: 100% 220px;
    background-repeat: no-repeat;
  }
  #home >>> .el-collapse-item__header {
    height: 80px;
  }
</style>