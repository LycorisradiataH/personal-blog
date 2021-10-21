<template>
  <div class="rightside" :style="isShow">
    <i @click="backTop" class="iconfont rightside-icon icon-backtop" />
  </div>
</template>

<script>
export default {
  data () {
    return {
      isShow: ''
    }
  },

  mounted () {
    window.addEventListener('scroll', this.scrollToTop)
  },

  destroyed () {
    window.removeEventListener('scroll', this.scrollToTop)
  },

  methods: {
    // 返回顶部
    backTop () {
      window.scrollTo({
        behavior: 'smooth',
        top: 0
      })
    },
    // 为了计算距离顶部级高度，当高度大于100显示回到顶部图标，小于100则隐藏
    scrollToTop () {
      const that = this
      const scrollTop =
        window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop
      that.scrollTop = scrollTop
      if (that.scrollTop > 20) {
        that.isShow = 'opacity: 1;transform: translateX(-38px)'
      } else {
        that.isShow = ''
      }
    }
  }
}
</script>

<style scoped>
.rightside {
  z-index: 4;
  position: fixed;
  right: -38px;
  bottom: 85px;
  transition: all 0.5s;
}
.rightside-config-hide {
  transform: translate(35px, 0);
}
.rightside-out {
  animation: rightsideOut 0.3s;
}
.rightside-in {
  transform: translate(0, 0) !important;
  animation: rightsideIn 0.3s;
}
.rightside-icon,
.setting-container {
  display: block;
  margin-bottom: 2px;
  width: 30px;
  height: 30px;
  background-color: #49b1f5;
  color: #fff;
  text-align: center;
  font-size: 16px;
  line-height: 30px;
  cursor: pointer;
}
.rightside-icon:hover,
.setting-container:hover {
  background-color: #ff7242;
}
.setting-container i {
  display: block;
  animation: turn-around 2s linear infinite;
}
@keyframes turn-around {
  0% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(360deg);
  }
}
@keyframes rightsideOut {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(30px, 0);
  }
}
@keyframes rightsideIn {
  0% {
    transform: translate(30px, 0);
  }
  100% {
    transform: translate(0, 0);
  }
}
</style>
