<template>
  <div id="novel-query">
    <el-input placeholder="请输入书名" v-model="query.name" class="input-with-select">
      <el-button slot="append" icon="el-icon-search" @click="queryHandler"></el-button>
    </el-input>
    <div class="bg" style="margin-top: 10px;">
      <!-- 作品分类 -->
      <div class="kind">
        <div class="bz">作品分类：</div>
        <div class="nr br sub">
          <div
              @click="handlerChangeType(item)"
              :class="{'store-select': item.value === query.type}"
              class="store" v-for="item in typeOptions"
              :key="item.label">
            {{item.label}}
          </div>
          <br>
        </div>
      </div>

      <!-- 写作进度 -->
      <div class="kind">
        <div class="bz">写作进度：</div>
        <div class="nr br sub">
          <div
              @click="handlerChangeState(item)"
              :class="{'store-select': item.value === query.state}"
              class="store" v-for="item in stateOptions"
              :key="item.label">
            {{item.label}}
          </div>
          <br>
        </div>
      </div>

      <!-- 标签 -->
      <div class="kind">
        <div class="bz">标签：</div>
        <div class="nr br sub">
          <div
              @click="handlerChangeTag(item)"
              class="store" v-for="item in tagOptions"
              :key="item.label">
            <div v-if="query.tag.map(item => item.value).indexOf(item.value) === -1">{{item.label}}</div>
            <el-tag :disable-transitions="true" v-else @close="handleCloseTag(item)" closable>{{item.label}}</el-tag>
          </div>

          <br>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Tag from "../../../api/Tag";

  export default {
    name: "NovelQuery",
    data() {
      return {
        typeOptions: [
          {label: "全部", value: "全部"},
          {label: "玄幻魔法", value: "玄幻魔法"},
          {label: "武侠修真", value: "武侠修真"},
          {label: "都市言情", value: "都市言情"},
          {label: "历史军事", value: "历史军事"},
          {label: "侦探推理", value: "侦探推理"},
          {label: "网游动漫", value: "网游动漫"},
          {label: "科幻灵异", value: "科幻灵异"},
        ],
        stateOptions: [
          {label: "全部", value: "全部"},
          {label: "连载中", value: "连载中"},
          {label: "已完成", value: "已完成"},
        ],
        tagOptions: [],
        query: {
          type: "全部",
          state: "全部",
          tag: []
        }
      }
    },
    methods: {
      queryHandler() {
        let copy = JSON.parse(JSON.stringify(this.query));
        if (copy.type === '全部') {
          copy.type = null;
        }
        if (copy.state === '全部') {
          copy.state = null;
        }
        if (copy.tag) {
          copy.tag = null;
          copy.tags = this.query.tag.map(item => item.value).join(",");
        }
        this.$emit("query", copy);
      },
      handleCloseTag(data) {
        let index = this.query.tag.map(item => item.value).indexOf(data.value)
        this.query.tag.splice(index, 1);
        this.queryHandler();
      },
      handlerChangeTag(item) {
        this.query.tag.push(item);
        this.queryHandler();
      },
      handlerChangeState(item) {
        this.$set(this.query, 'state', item.value);
        this.queryHandler();
      },
      handlerChangeType(item) {
        this.$set(this.query, 'type', item.value);
        this.queryHandler();
      },
      loadTags() {
        Tag.list().then(res => {
          let data = res.data.data;
          this.tagOptions = this.tagOptions.concat(data.map(item => ({
            label: item.name,
            value: item.id
          })));
        })
      },
    },
    created() {
      this.loadTags();
    }
  }
</script>

<style scoped>
.bg {
  background: #f7f7f7;
  border: 1px solid #e6e6e6;
  padding: 12px 20px;
  font-size: 13px;
}
  .kind .bz {
    float: left;
    width: 88px;
    color: #999;
    padding: 5px;
    height: 30px;
    line-height: 30px;
  }
  .bg .store {
    color: #151515;
    font-size: 13px;
    margin: 5px 6px 5px 7px;
    display: inline-block;
    cursor: pointer;
    height: 30px;
    line-height: 30px;
    padding: 0 5px;
  }
  .store-select {
    color: #fff !important;
    background: #d32f2f;
    border-radius: 2px;
  }
  #novel-query >>> .el-tag {
    color: #fff !important;
    background: #d32f2f;
  }
  #novel-query >>> .el-tag >>> .el-tag__close {
    color: #fff !important;
  }
  #novel-query >>> .el-tag >>> .el-tag__close::before:hover {
    color: #000 !important;
    background: #fff !important;
  }
</style>