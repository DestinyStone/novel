<template>
  <div style="background: #d9cdb6; padding-bottom: 30px;">
    <header-view/>
    <div class="container" id="contentDetail" style="background: #faeed7; box-shadow: 0 2px 20px 0 rgba(0,0,0,.05); margin: 30px auto; padding: 0 100px">
      <div style="position:relative;">
        <div style="font-size: 30px; text-align: center; padding-top: 100px;">{{data.title}}</div>
        <div style="text-align: center; color: #999; padding-top: 10px;">更新时间: {{data.createDate}}</div>
        <div style="position:absolute; bottom: 0px; right: 0; width: 100px;" class="reader_lnkbtn" @click="toHome">目录</div>
      </div>
      <el-divider></el-divider>
      <div style="font-size: 18px;" v-html="data.content">

      </div>
      <div style="display: flex;">
        <div style="width: calc(100%/3); padding: 10px;">
          <div style="height: 40px; line-height: 40px;" class="reader_lnkbtn" @click="toHome">目录</div>
        </div>
        <div style="width: calc(100%/3); padding: 10px;">
          <div style="height: 40px; line-height: 40px;" class="reader_lnkbtn" @click="toTop">上一章</div>
        </div>
        <div style="width: calc(100%/3); padding: 10px;">
          <div style="height: 40px; line-height: 40px;" class="reader_lnkbtn" @click="toBottom">下一章</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import HeaderView from "../common/HeaderView";
  import Novel from "../../api/Novel";
  import NovelContent from "../../api/NovelContent";
  import ReadLog from "../../api/ReadLog";
  export default {
    name: "ContentDetail",
    components: {HeaderView},
    data() {
      return {
        data: {},
        novelData: {},
        list: {},
        scrollMethod: null,
      }
    },
    methods: {
      toTop() {
        let currentId = this.$route.query.id;
        let top = null;
        for(let item of this.list) {
          if (item.id != currentId) {
            top = item;
            continue;
          }
          if (!top) {
            this.$message.warning("没有上一章了哦~");
            return;
          }
          window.location.href = `/#/content?novelId=${this.$route.query.novelId}&id=${top.id}`
          window.location.reload();
        }
      },
      toBottom() {
        let currentId = this.$route.query.id;
        for(let i = 0; i<this.list.length; i++) {
          let item = this.list[i];
          if (item.id != currentId) {
            continue;
          }
          if (i === this.list.length - 1) {
            this.$message.warning("没有下一章了哦~");
            return;
          }
          let top = this.list[i + 1]
          window.location.href = `/#/content?novelId=${this.$route.query.novelId}&id=${top.id}`
          window.location.reload();
        }
      },
      onLoad() {
        Novel.getById(this.$route.query.novelId).then(res => {
          this.novelData = res.data.data;
        })
      },
      onLoadContent() {
        NovelContent.getById(this.$route.query.id).then(res => {
          this.data = res.data.data;
          this.readLogFlag();
        })
        NovelContent.list({novelId: this.$route.query.novelId}).then(res => {
          this.list = res.data.data;
        }).then(() => {
          setTimeout(() => {
            this.onLoadScroll();
          }, 30);
        })
      },
      toHome() {
        window.location.href = `/#/detail?id=${this.$route.query.novelId}`
      },
      readLogFlag() {
        let data = {};
        data.novelId = this.$route.query.novelId;
        data.contentId = this.$route.query.id;
        data.title = this.data.title;

        ReadLog.save(data);
        setInterval(() => {
          ReadLog.save(data);
        }, 1000 * 60)
      },
      onLoadScroll() {
        let key = `novalId:${this.$route.query.novelId},id:${this.$route.query.id}`
        let scrollTop = sessionStorage.getItem(key);
        if (scrollTop && scrollTop !== "0") {
          this.$message.success("已定位到上次浏览位置");
          document.documentElement.scrollTop = scrollTop;
        }

        window.addEventListener('scroll', this.scrollMethod = () =>{
          sessionStorage.setItem(key, document.documentElement.scrollTop)
        });
      }
    },
    created() {
      this.onLoad();
      this.onLoadContent();
    },
    mounted() {
    },
    beforeDestroy() {
      window.removeEventListener("scroll", this.scrollMethod);
    }
  }
</script>

<style scoped>
  .reader_lnkbtn {
    border: 1px solid #b3b3b3;
    border-radius: 2px;
    height: 30px;
    line-height: 30px;
    color: #333;
    cursor: pointer;
    text-align: center;
  }
  .reader_lnkbtn:hover {
    color: #d32f2f;
    border: 1px solid #d32f2f;
  }
</style>