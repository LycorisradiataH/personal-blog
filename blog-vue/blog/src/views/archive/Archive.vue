<template>
  <div class="archive">
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">归档</h1>
    </div>
    <!-- 归档列表 -->
    <v-card class="blog-container">
      <timeline>
        <timeline-title>目前共计{{ count }}篇文章</timeline-title>
        <timeline-item v-for="item in archiveList" :key="item.id">
          <!-- 日期 -->
          <span class="time">{{ item.gmtCreate | date }}</span>
          <!-- 文章标题 -->
          <router-link
            :to="'/article/' + item.id"
            style="color: #666;text-decoration: none"
          >
            {{ item.articleTitle }}
          </router-link>
        </timeline-item>
      </timeline>
      <!-- 分页按钮 -->
      <v-pagination
        color="#00c4b6"
        v-model="current"
        :length="Math.ceil(count / 10)"
        total-visible="7"
      ></v-pagination>
    </v-card>
  </div>
</template>

<script>
import { Timeline, TimelineItem, TimelineTitle } from 'vue-cute-timeline'
import { listArchive } from '@/api/article'

export default {
  components: {
    Timeline,
    TimelineItem,
    TimelineTitle
  },

  data () {
    return {
      current: 1,
      count: 0,
      archiveList: []
    }
  },

  created () {
    this.listArchive()
  },

  watch: {
    current (value) {
      listArchive(value).then(({ data }) => {
        if (data.status) {
          this.archiveList = data.data.recordList
          this.count = data.data.count
        }
      })
    }
  },

  computed: {
    cover () {
      var cover = ''
      this.$store.state.blogInfo.pageList.forEach(item => {
        if (item.pageLabel === 'archive') {
          cover = item.pageCover
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    }
  },

  methods: {
    listArchive () {
      listArchive(this.current).then(({ data }) => {
        if (data.status) {
          this.archiveList = data.data.recordList
          this.count = data.data.count
        }
      })
    }
  }
}
</script>

<style scoped>
.time {
  font-size: 0.75rem;
  color: #555;
  margin-right: 1rem;
}
</style>
