<template>
  <div class="bookbox-wrapper">
    <div class="bookbox">
      <div class="bookimg">
        <a :href="'detail?id=' + data.id" target="_blank">
          <img :src="data.imgUrl">
        </a>
      </div>
      <div class="bookinfo">
        <div class="bookname" @click="toLink" style="display: flex; justify-content: space-between">
          <div>{{data.name}}</div>
          <div v-if="showReadLog" style="color: #8c8c8c; margin-left: 20px; font-size: 14px;">已阅读 {{data.readTime}}分钟~</div>
        </div>
        <div class="bookilnk">
          <span href="" style="padding: 0 10px;">{{data.author}}</span>|
          <span href="" style="padding: 0 10px;">{{data.type}}</span>|
          <span href="" style="padding: 0 10px;">{{data.state}}</span>|
          <span href="" style="padding: 0 10px;">{{data.createDate}}</span>
        </div>
        <div style="display: flex; justify-content: left; flex-flow: wrap;">
          <div style="color: #909399;">标签：</div>
          <div
              style="color: #909399; margin-right: 10px;"
              v-for="tag in data.tagList"
              :key="'bookItem' + tag.id"
          >
            {{tag.name}}
          </div>
        </div>
        <div class="bookintro">{{data.brief}}</div>
        <div class="bookupdate">最新章节： {{data.newest}}</div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "book-item",
    props: {
      showReadLog: {
        type: Boolean,
        default: false,
      },
      data: {
        type: Object,
        default() {
          return {}
        }
      }
    },
    methods: {
      toLink() {
        window.location.href = '/#/detail?id=' + this.data.id
      }
    }
  }
</script>

<style scoped>
  .bookbox-wrapper {
    padding: 10px;
  }
  .bookbox {
    height: 180px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 10px;
  }
  .bookbox .bookimg img {
    width: 120px;
    height: 160px;
    float: left;
    margin-right: 20px;
  }
  .bookname {
    font-size: 20px;
    color: #333;
    line-height: 26px;
    height: 26px;
    cursor: pointer;
    text-align: left;
  }
  .bookname:hover {
    color: #d32f2f;
  }
  .bookbox .bookilnk {
    height: 16px;
    line-height: 16px;
    font-size: 12px;
    color: #909399;
    margin: 10px 0 0px;
    text-indent: -8px;
    text-align: left;
  }
  .bookbox .bookintro {
    margin-top: 15px;
    font-size: 13px;
    color: #666;
    line-height: 20px;
    max-height: 40px;
    overflow: hidden;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    text-align: left;
  }
  .bookbox .bookupdate {
    text-align: left;
    color: #909399;
    margin-top: 15px;
  }
</style>