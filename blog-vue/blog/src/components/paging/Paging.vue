<template>
  <div class="paging">
    <!-- 上一页按钮 第一页不显示 -->
    <a @click="prePage" v-show="current !== 1" class="ml-1 mr-1">上一页</a>
    <!-- 小于6页直接显示 -->
    <template v-if="totalPage < 6">
      <a
        v-for="i in totalPage"
        :key="i"
        :class="'ml-1 mr-1 ' + isActive(i)"
        @click="changeReplyCurrent(i)"
      >
        {{ i }}
      </a>
    </template>
    <!-- 大于等于6页且在前2页 -->
    <template v-else-if="current < 3">
      <a
        v-for="i in 4"
        :key="i"
        :class="'ml-1 mr-1 ' + isActive(i)"
        @click="changeReplyCurrent(i)"
      >
        {{ i }}
      </a>
      <span class="ml-1 mr-1">···</span>
      <a @click="changeReplyCurrent(totalPage)" class="ml-1 mr-1">
        {{ totalPage }}
      </a>
    </template>
    <!-- 大于等于6页且在3-4页 -->
    <template v-else-if="current < 5">
      <a
        v-for="i in current + 2"
        :key="i"
        :class="'ml-1 mr-1 ' + isActive(i)"
        @click="changeReplyCurrent(i)"
      >
        {{ i }}
      </a>
      <span class="ml-1 mr-1" v-if="current + 2 < totalPage - 1">···</span>
      <a
        @click="changeReplyCurrent(totalPage)"
        v-if="current + 2 < totalPage"
        class="ml-1 mr-1"
      >
        {{ totalPage }}
      </a>
    </template>
    <!-- 大于等于6页且在最后两页 -->
    <template v-else-if="current > totalPage - 2">
      <a @click="changeReplyCurrent(1)" class="ml-1 mr-1">1</a>
      <span class="ml-1 mr-1">···</span>
      <a
        v-for="i in 4"
        :key="i"
        @click="changeReplyCurrent(i + (totalPage - 4))"
        :class="'ml-1 mr-1 ' + isActive(i + (totalPage - 4))"
      >
        {{ i + (totalPage - 4) }}
      </a>
    </template>
    <!-- 大于等于6页且在最后3-4页 -->
    <template v-else-if="current > totalPage - 4">
      <a @click="changeReplyCurrent(1)" class="ml-1 mr-1">1</a>
      <span class="ml-1 mr-1">···</span>
      <a
        v-for="i in totalPage - current + 3"
        :key="i"
        @click="changeReplyCurrent(i + current - 3)"
        :class="'ml-1 mr-1 ' + isActive(i + current - 3)"
      >
        {{ i + current - 3 }}
      </a>
    </template>
    <!-- 大于等于6页且在中间页 -->
    <template v-else>
      <a @click="changeReplyCurrent(1)" class="ml-1 mr-1">1</a>
      <span class="ml-1 mr-1">···</span>
      <a @click="changeReplyCurrent(current - 2)" class="ml-1 mr-1">
        {{ current - 2 }}
      </a>
      <a @click="changeReplyCurrent(current - 1)" class="ml-1 mr-1">
        {{ current - 1 }}
      </a>
      <a class="active ml-1 mr-1">{{ current }}</a>
      <a @click="changeReplyCurrent(current + 1)" class="ml-1 mr-1">
        {{ current + 1 }}
      </a>
      <a @click="changeReplyCurrent(current + 2)" class="ml-1 mr-1">
        {{ current + 2 }}
      </a>
      <span class="ml-1 mr-1">···</span>
      <a @click="changeReplyCurrent(totalPage)" class="ml-1 mr-1">
        {{ totalPage }}
      </a>
    </template>
    <!-- 下一页按钮 最后一页不显示 -->
    <a @click="nextPage" v-show="current !== totalPage" class="ml-1 mr-1">
      下一页
    </a>
  </div>
</template>

<script>
export default {
  data () {
    return {
      current: 1
    }
  },

  props: {
    totalPage: {
      type: Number
    },
    index: {
      type: Number
    },
    commentId: {
      type: Number
    }
  },

  computed: {
    isActive () {
      return function (i) {
        if (i === this.current) {
          return 'active'
        }
      }
    }
  },

  methods: {
    changeReplyCurrent (i) {
      this.current = i
      this.$emit('changeReplyCurrent', this.current, this.index, this.commentId)
    },
    prePage () {
      this.current -= 1
      this.$emit('changeReplyCurrent', this.current, this.index, this.commentId)
    },
    nextPage () {
      this.current += 1
      this.$emit('changeReplyCurrent', this.current, this.index, this.commentId)
    }
  }
}
</script>

<style scoped>
.paging a {
  display: inline-block;
  color: #222;
}
.active {
  color: #00a1d6 !important;
  font-weight: bold;
}
</style>
