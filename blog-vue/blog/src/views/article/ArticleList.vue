<template>
  <div class="article-list">
    <!-- 标签或分类名 -->
    <div class="banner" :style="cover">
      <h1 class="banner-title animated fadeInDown">{{ title }} - {{ name }}</h1>
    </div>
    <div class="article-list-wrapper">
      <v-row>
        <v-col md="4" cols="12" v-for="item in articleList" :key="item.id">
          <!-- 文章 -->
          <v-card class="animated zoomIn article-item-card">
            <div class="article-item-cover">
              <router-link :to="'/article/' + item.id">
                <!-- 缩略图 -->
                <v-img
                  class="on-hover"
                  width="100%"
                  height="100%"
                  :src="item.articleCover"
                ></v-img>
              </router-link>
            </div>
            <div class="article-item-info">
              <!-- 标题 -->
              <div>
                <router-link :to="'/article/' + item.id">
                  {{ item.articleTitle }}
                </router-link>
              </div>
              <div style="margin-top: 0.375rem">
                <!-- 发表时间 -->
                <v-icon size="20">mdi-clock-outline</v-icon>
                {{ item.gmtCreate | date }}
                <!-- 文章分类 -->
                <router-link
                  :to="'/category/' + item.categoryId"
                  class="float-right"
                >
                  <v-icon>mdi-bookmark</v-icon>
                  {{ item.categoryName }}
                </router-link>
              </div>
            </div>
            <!-- 分割线 -->
            <v-divider></v-divider>
            <!-- 文章标签 -->
            <div class="tag-wrapper">
              <router-link
                :to="'/tag/' + tag.id"
                class="tag-btn"
                v-for="tag in item.tagVOList"
                :key="tag.id"
              >
                {{ tag.tagName }}
              </router-link>
            </div>
          </v-card>
        </v-col>
      </v-row>
      <!-- 无限加载 -->
      <infinite-loading @infinite="infiniteHandler">
        <div slot="no-results"></div>
        <div slot="no-more"></div>
      </infinite-loading>
    </div>
  </div>
</template>

<script>
import { listArticleByQueryParam } from '@/api/article'

export default {
  data () {
    return {
      current: 1,
      size: 10,
      articleList: [],
      name: '',
      title: ''
    }
  },

  created () {
    const path = this.$route.path
    if (path.indexOf('/category') !== -1) {
      this.title = '分类'
    } else if (path.indexOf('/tag') !== -1) {
      this.title = '标签'
    }
  },

  computed: {
    cover () {
      var cover = ''
      this.$store.state.blogInfo.pageList.forEach(item => {
        if (item.pageLabel === 'articleList') {
          cover = item.pageCover
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    }
  },

  methods: {
    infiniteHandler ($state) {
      const categoryId = this.$route.params.categoryId
      const tagId = this.$route.params.tagId
      listArticleByQueryParam(categoryId, tagId, this.current).then(
        ({ data }) => {
          if (data.status && data.data.name) {
            this.name = data.data.name
            document.title = this.title + ' - ' + this.name
          }
          if (data.data.articlePreviewVOList.length) {
            this.current++
            this.articleList.push(...data.data.articlePreviewVOList)
            $state.loaded()
          } else {
            $state.complete()
          }
        }
      )
    }
  }
}
</script>

<style scoped>
@media (min-width: 760px) {
  .article-list-wrapper {
    max-width: 1106px;
    margin: 370px auto 1rem auto;
  }
  .article-item-card:hover {
    transition: all 0.3s;
    box-shadow: 0 4px 12px 12px rgba(7, 17, 27, 0.15);
  }
  .article-item-card:not(:hover) {
    transition: all 0.3s;
  }
  .article-item-card:hover .on-hover {
    transition: all 0.6s;
    transform: scale(1.1);
  }
  .article-item-card:not(:hover) .on-hover {
    transition: all 0.6s;
  }
  .article-item-info {
    line-height: 1.7;
    padding: 15px 15px 12px 18px;
    font-size: 15px;
  }
}
@media (max-width: 759px) {
  .article-list-wrapper {
    margin-top: 230px;
    padding: 0 12px;
  }
  .article-item-info {
    line-height: 1.7;
    padding: 15px 15px 12px 18px;
  }
}
.article-item-card {
  border-radius: 8px !important;
  box-shadow: 0 4px 8px 6px rgba(7, 17, 27, 0.06);
}
.article-item-card a {
  transition: all 0.3s;
}
.article-item-cover {
  height: 220px;
  overflow: hidden;
}
.article-item-card a:hover {
  color: #8e8cd8;
}
.tag-wrapper {
  padding: 10px 15px 10px 18px;
}
.tag-wrapper a {
  color: #fff !important;
}
.tag-btn {
  display: inline-block;
  font-size: 0.725rem;
  line-height: 22px;
  height: 22px;
  border-radius: 10px;
  padding: 0 12px !important;
  background: linear-gradient(to right, #bf4643 0%, #6c9d8f 100%);
  opacity: 0.6;
  margin-right: 0.5rem;
}
</style>
